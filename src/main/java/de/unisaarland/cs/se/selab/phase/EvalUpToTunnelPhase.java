package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.Action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.Action.DigTunnelAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class EvalUpToTunnelPhase extends Phase{
    public Phase run(){
        //TODO
        return new EvalUpToMonsterPhase();
    }

    private void eval(){
        //TODO
        //iterate over BiddingSquare
    }

    private void grant(Player player, int bidtype, int slot){
        //TODO
    }

    private void exec(DigTunnelAction dta){
        //TODO
    }

    private void exec(ActivateRoomAction ara){
        //TODO
    }

    private int[] collectBidWinners(){
        //TODO
        return new int[0]; //collect the playerID
    }
}
