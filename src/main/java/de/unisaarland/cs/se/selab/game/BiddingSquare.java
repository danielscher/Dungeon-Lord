package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.BidType;

import java.util.Map;

public class BiddingSquare {
    private int [][] biddingSlots; // row=slot, column=bidtype, [3][8]
    private Map<BidType,Integer> typeToColumnMap;
    private Map<Integer,BidType> columnToTypeMap;

    public boolean insert(BidType FOOD, int playerID){
        return false;
        // insert playerID to the uppermost free row
    }

    public int getIDByBidSlot(int row,int column){
        return -1;
    }

    public int getIDByBidSlot(BidType FOOD,int column){
        return -1;
    }

    private int typeToColumn(BidType FOOD){
        return -1;
    }

    private BidType columnToType(int column){

        //return NULL;
    }

    public int countTrapBids(){
        return 0;
    }

}
