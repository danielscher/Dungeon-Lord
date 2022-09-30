import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import org.junit.jupiter.api.Test;

class BiddingsquareTest {
    BiddingSquare bs = new BiddingSquare();

    private void resetBiddingSquare() {
        bs = new BiddingSquare();
        bs.insert(BidType.FOOD, 2);
        bs.insert(BidType.FOOD, 3);
        bs.insert(BidType.FOOD, 4);
        bs.insert(BidType.GOLD, 3);
        bs.insert(BidType.TRAP, 4);
        bs.insert(BidType.TRAP, 2);
        //  biddingslots = {[2,3,-1,-1,-1,4,-1,-1],
        //                  [3,-1,-1,-1,-1,2,-1,-1],
        //                  [4,-1,-1,-1,-1,-1,-1,-1]}
    }

    /*
    @Test
    void testInsert() {
        resetBiddingSquare();
        assertFalse(bs.insert(BidType.FOOD, 3));
        assertTrue(bs.insert(BidType.GOLD, 3));
    }

    @Test
    void testGetIDByBidSlot() {
        resetBiddingSquare();
        assertEquals('2', bs.getIDByBidSlot(0, 0));
        assertEquals('2', bs.getIDByBidSlot(BidType.FOOD, 0));
    }

    @Test
    void testTypeToColumn() {
        resetBiddingSquare();
        assertEquals(0, bs.typeToColumn(BidType.FOOD));
    }

    @Test
    void testColumnToType() {
        resetBiddingSquare();
        assertEquals(BidType.FOOD, bs.columnToType(0));
    }

    @Test
    void testCountTrapBids() {
        resetBiddingSquare();
        assertEquals(2, bs.countTrapBids());
    }
    */
}
