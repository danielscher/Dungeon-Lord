package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;

public class CollectAndPlaceBidPhase extends Phase {

    public CollectAndPlaceBidPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //    return NULL;
    }

    private void eval() {
        //to_do
        //iterates over Bidding Square
    }

    private void grant(Player p, int r, int c) {
        //to_do
        //grants a single bid
    }

    private void exec(PlaceBidAction pba) {
        //to_do
    }

    private void exec(ActivateRoomAction ara) {
        //to_do
    }

    private boolean checkIfAllBidsChosen() {
        //to_do
        return false;
    }

    private void blockBids() {
        //to_to
    }
}
