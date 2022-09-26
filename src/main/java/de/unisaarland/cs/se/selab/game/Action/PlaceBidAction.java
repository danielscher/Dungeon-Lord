package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.comm.BidType;

public class PlaceBidAction extends Action {

    private BidType bid;
    private int slot;

    public PlaceBidAction(int commID, BidType bid, int slot) {
        super(commID);
        this.bid = bid;
        this.slot = slot;
    }

    public BidType getBid() {
        return bid;
    }

    public int getSlot() {
        return slot;
    }
}
