package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.Action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.List;

public class ChooseBattleGroundPhase extends Phase{
    private Player currPlayer;
    private int round;



    public ChooseBattleGroundPhase(GameData gd, Player currPlayer) {
        super(gd);
        this.currPlayer = currPlayer;
    }

    public ChooseBattleGroundPhase(GameData gd, Player currPlayer, int round) {
        super(gd);
        this.currPlayer = currPlayer;
        this.round = round;
    }

    public Phase run() throws TimeoutException {

       broadcastNextRound(round);
       gd.getServerConnection().sendActNow(currPlayer.getCommID());                                 //send individual event "ActNow"

        BattleGroundAction bga = (BattleGroundAction) gd.getServerConnection().nextAction();        //create battlegound action
        exec(bga);

        return new Combatphase(gd);
    }

    public void exec(BattleGroundAction bga) {
        List<int[]> PossibleCoords = currPlayer.getDungeon().getPossibleBattleCoords();
        int[] chosenCoords = bga.getCoords();

        if (!PossibleCoords.contains(chosenCoords)){                                                //invalid coordinates
            gd.getServerConnection().sendActionFailed(currPlayer.getCommID(), "Chosen coordinates are not available.");
        } else {
            currPlayer.getDungeon().setBattleGround(chosenCoords);
        }
    }
}
