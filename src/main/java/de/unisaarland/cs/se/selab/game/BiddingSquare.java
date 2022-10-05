package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiddingSquare {

    private final int[][] biddingSlots = new int[3][8];
    private final Map<BidType, Integer> typeToColumnMap = new HashMap<>();
    private final Map<Integer, BidType> columnToTypeMap = new HashMap<>();
    private final List<BidType> bidTypes = new ArrayList<>(EnumSet.allOf(BidType.class));

    public BiddingSquare() {
        for (final int[] row : biddingSlots) {
            Arrays.fill(row, -1);
        }
        initTypeToColumnMap(typeToColumnMap);
        initColumnToBidTypeMap(columnToTypeMap);

    }

    // map initialization.
    private void initColumnToBidTypeMap(final Map<Integer, BidType> map) {
        int i = 0;
        for (final BidType bt : bidTypes) {
            map.put(i, bt);
            i++;
        }
    }

    // map initialization.
    private void initTypeToColumnMap(final Map<BidType, Integer> map) {
        int i = 0;
        for (final BidType bt : bidTypes) {
            map.put(bt, i);
            i++;
        }
    }

    public int insert(final BidType bidType, final int playerID) {
        // get the uppermost free row
        final int columnId = typeToColumn(bidType);
        for (int row = 0; row < 3; row++) {
            if (biddingSlots[row][columnId] == -1) {
                biddingSlots[row][columnId] = playerID;
                return row;
            }
        }
        return -1;
    }

    /**
     * this method return the entry of the bidding square of a given location
     *
     * @param row slot/row on the square
     * @param column column/BidType or category
     * @return the number that is contained in this slot
     */
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

    public void clearEntries() {
        for (final int[] row : biddingSlots) {
            Arrays.fill(row, -1);
        }
    }



}
