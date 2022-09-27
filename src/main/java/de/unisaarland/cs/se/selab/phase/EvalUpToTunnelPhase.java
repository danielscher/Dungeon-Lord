package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.DigTunnelAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class EvalUpToTunnelPhase extends Phase {

    public EvalUpToTunnelPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //TODO
        return null;
    }

    private void eval() {
        //TODO
        //iterate over BiddingSquare
    }

    private void grant(Player player, int bidtype, int slot) {
        //TODO
    }

    private void exec(DigTunnelAction dta) {
        //TODO
    }

    private void exec(ActivateRoomAction ara) {
        //TODO
    }

    private int[] collectBidWinners() {
        //TODO
        return new int[0]; //collect the playerID
    }
}
