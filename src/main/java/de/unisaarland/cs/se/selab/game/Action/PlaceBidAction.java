package de.unisaarland.cs.se.selab.game.Action;

public class PlaceBidAction extends Action{
    private BidType bid;
    private int bidNum;

    public PlaceBidAction(int commID, BidType bid, int bidNum) {
        super(commID);
        this.bid = bid;
        this.bidNum = bidNum;
    }

    public BidType getBid() {
        return bid;
    }

    public int getBidNum() {
        return bidNum;
    }
}
