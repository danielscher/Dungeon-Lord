package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.Action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.Action.DigTunnelAction;
import de.unisaarland.cs.se.selab.game.Action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.Action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class EvalUpToMonsterPhase extends Phase{

    public Phase run(){
        //TODO
        return new EvalRoomPhase();
    }

    private void eval(){
        //TODO
        //iterate over BiddingSquare
    }

    private void grant(Player player, int bidtype, int slot){
        //TODO
    }

    private void exec(HireMonsterAction hma){
        //TODO
    }

    private void exec(ActivateRoomAction ara){
        //TODO
    }

    private void exec(EndTurnAction eta){
        //TODO
    }

    private int[] collectBidWinners(){
        //TODO
        return new int[0]; //collect the playerID
    }
}
