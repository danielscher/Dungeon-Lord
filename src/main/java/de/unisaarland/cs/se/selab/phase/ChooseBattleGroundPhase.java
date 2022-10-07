package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.List;

public class ChooseBattleGroundPhase extends Phase {

    private final Player currPlayer;
    private boolean battleGroundChosen;

    public ChooseBattleGroundPhase(final GameData gd, final Player currPlayer) {
        super(gd);
        this.currPlayer = currPlayer;
    }

    @Override
    public Phase run() {
        if (gd.getAllPlayerID().isEmpty()) {
            return null; // abort game if no players left
        }

        broadcastNextRound(gd.getTime().getSeason() - 4);

        if (currPlayer.getDungeon().getNumUnconqueredTiles() == 0) {
            //if all tiles are conquered
            return new CombatPhase(gd, currPlayer);
        }

        gd.getServerConnection().sendSetBattleGround(
                currPlayer.getCommID()); //send individual event "SetBattleGround"
        gd.getServerConnection().sendActNow(
                currPlayer.getCommID()); //send individual event "ActNow"

        if (!battleGroundChosen) {
            //loop ask for the next action until get the bga from the current player
            try {
                gd.getServerConnection().nextAction().invoke(this);
            } catch (TimeoutException e) {
                kickPlayer(currPlayer.getPlayerID());
                battleGroundChosen = true;
            }
        }

        if (gd.getAllPlayerID().isEmpty()) {
            return null; // abort game if no players left
        }

        return new CombatPhase(gd, currPlayer);
    }

    @Override
    public void gotInvalidActionFrom(final int commID) {
        if (commID == currPlayer.getCommID()) {
            gd.getServerConnection().sendActNow(commID);
        }
    }

    @Override
    public void exec(final BattleGroundAction bga) {

        if (bga.getCommID() != currPlayer.getCommID()) {
            gd.getServerConnection().sendActionFailed(bga.getCommID(),
                    "It's not your turn to choose a battle ground yet");
            return;
        }
        final List<Coordinate> possibleCoords = currPlayer.getDungeon().getPossibleBattleCoords();
        final Coordinate chosenCoords = new Coordinate(bga.getRow(), bga.getCol());
        if (!possibleCoords.contains(
                chosenCoords)) { //invalid coordinates
            gd.getServerConnection().sendActionFailed(currPlayer.getCommID(),
                    "Chosen coordinates are not available.");
        } else {
            currPlayer.getDungeon().setBattleGround(chosenCoords);
            broadcastBattleGroundSet(currPlayer.getPlayerID(), bga.getRow(), bga.getCol());
        }
        battleGroundChosen = true;
    }

    @Override
    public void exec(final LeaveAction la) {
        battleGroundChosen = true;
        super.exec(la);
    }
}
