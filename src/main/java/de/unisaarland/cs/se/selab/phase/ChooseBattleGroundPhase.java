package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.List;

public class ChooseBattleGroundPhase extends Phase {

    private final Player currPlayer;

    public ChooseBattleGroundPhase(final GameData gd, final Player currPlayer) {
        super(gd);
        this.currPlayer = currPlayer;
    }

    @Override
    public Phase run() throws TimeoutException {
        broadcastNextRound(gd.getTime().getSeason());

        if (currPlayer.getDungeon().getNumUnconqueredTiles() == 0) {
            //if all tiles are conquered
            return new CombatPhase(gd, currPlayer);
        }

        gd.getServerConnection().sendSetBattleGround(
                currPlayer.getCommID()); //send individual event "SetBattleGround"
        gd.getServerConnection().sendActNow(
                currPlayer.getCommID()); //send individual event "ActNow"

        final Action action = gd.getServerConnection().nextAction();
        if (action.getCommID() == currPlayer.getCommID()) {
            action.invoke(this);
        }
        return new CombatPhase(gd, currPlayer);
    }

    @Override
    public void exec(final BattleGroundAction bga) {
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
    }
}
