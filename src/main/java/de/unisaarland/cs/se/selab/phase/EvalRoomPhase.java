package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.BuildRoomAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class EvalRoomPhase extends Phase {

    public EvalRoomPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //TODO
        return null;
    }

    private void eval() {
        //TODO
        //iterate over BidSquare
    }

    private void grant(Player player, int bidtype, int slot) {
        //TODO
    }

    private void exec(BuildRoomAction bra) {
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
