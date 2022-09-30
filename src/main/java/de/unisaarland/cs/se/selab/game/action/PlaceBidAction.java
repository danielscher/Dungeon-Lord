package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.phase.Phase;

public class PlaceBidAction extends Action {

    private final BidType bid;
    private final int slot;

    public PlaceBidAction(final int commID, final BidType bid, final int slot) {
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

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
