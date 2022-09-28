package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.List;

public class ChooseBattleGroundPhase extends Phase {

    private Player currPlayer;
    private int round;

    public ChooseBattleGroundPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {

        broadcastNextRound(round);
        gd.getServerConnection().sendSetBattleGround(
                currPlayer.getCommID()); //send individual event "SetBattleGround"
        gd.getServerConnection().sendActNow(
                currPlayer.getCommID()); //send individual event "ActNow"

        Action action = gd.getServerConnection().nextAction();
        if (action.getCommID() == currPlayer.getCommID()) {
            action.invoke(this);
        }
        return new Combatphase(gd);
    }

    public void exec(BattleGroundAction bga) {
        List<Coordinate> possibleCoords = currPlayer.getDungeon().getPossibleBattleCoords();
        Coordinate chosenCoords = bga.getCoords();
        if (!possibleCoords.contains(
                chosenCoords)) { //invalid coordinates
            gd.getServerConnection().sendActionFailed(currPlayer.getCommID(),
                    "Chosen coordinates are not available.");
        } else {
            currPlayer.getDungeon().setBattleGround(chosenCoords);
        }
    }
}
