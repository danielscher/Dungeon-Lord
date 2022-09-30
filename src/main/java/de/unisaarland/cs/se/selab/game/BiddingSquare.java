package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BiddingSquare {

    private final int[][] biddingSlots = new int[3][8];
    private final Map<BidType, Integer> typeToColumnMap = new HashMap<>();
    private final Map<Integer, BidType> columnToTypeMap = new HashMap<>();

    public BiddingSquare() {
        Arrays.fill(this.biddingSlots, -1);
    }

    public int insert(final BidType bidType, final int playerID) {
        // get the uppermost free row
        int slot = -1;
        final int columnId = typeToColumn(bidType);
        for (int row = 0; row < 3; row++) {
            if (biddingSlots[row][columnId] == -1) {
                biddingSlots[row][columnId] = playerID;
                slot = row;
                return slot;
            }
        }
        return slot;
    }

    public int getIDByBidSlot(final int row, final int column) {
        return biddingSlots[row][column];
    }

    public int getIDByBidSlot(final BidType bidType, final int row) {
        final int columnId = typeToColumn(bidType);
        return biddingSlots[row][columnId];
    }

    public int typeToColumn(final BidType bidType) {
        return typeToColumnMap.get(bidType);
    }

    public BidType columnToType(final int column) {
        return columnToTypeMap.get(column);
    }

    public int countTrapBids() {
        final int columnId = typeToColumn(BidType.TRAP);
        int res = 0;
        for (int row = 0; row < 3; row++) {
            if (biddingSlots[row][columnId] != -1) {
                res = res + 1;
            }
        }
        return res;

    }

}
