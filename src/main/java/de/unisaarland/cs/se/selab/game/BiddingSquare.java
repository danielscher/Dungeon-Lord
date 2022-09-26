package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.HashMap;
import java.util.Map;

public class BiddingSquare {

    private final int[][] biddingSlots = new int[3][8];
    private final Map<BidType, Integer> typeToColumnMap = new HashMap<BidType, Integer>();
    private final Map<Integer, BidType> columnToTypeMap = new HashMap<Integer, BidType>();


    public boolean insert(BidType bidType, int playerID) {
        // get the uppermost free row
        int columnId = typeToColumn(bidType);
        boolean res = false;
        for (int row = 0; row < biddingSlots.length; row++) {
            if (biddingSlots[row][columnId] == -1) {
                biddingSlots[row][columnId] = playerID;
                res = true;
                break;
            }

        }
        return res;
    }

    public int getIDByBidSlot(int row, int column) {
        return biddingSlots[row][column];
    }

    public int getIDByBidSlot(BidType bidType, int row) {
        int columnId = typeToColumn(bidType);
        return biddingSlots[row][columnId];

    }

    public int typeToColumn(BidType bidType) {
        return typeToColumnMap.get(bidType);
    }

    public BidType columnToType(int column) {
        return columnToTypeMap.get(column);
    }

    public int countTrapBids() {
        int columnId = typeToColumn(BidType.TRAP);
        int res = 0;
        for (int row = 0; row < biddingSlots.length; row++) {
            if (biddingSlots[row][columnId] != -1) {
                res = res + 1;
            }
        }
        return res;

    }

}
