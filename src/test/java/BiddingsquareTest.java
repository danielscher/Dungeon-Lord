import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BiddingsquareTest {
    BiddingSquare bs = new BiddingSquare();

    @BeforeEach
    private void resetBiddingSquare() {
        bs = new BiddingSquare();
        final boolean a1 = bs.insert(BidType.FOOD, 2);
        final boolean a2 = bs.insert(BidType.FOOD, 3);
        final boolean a3 = bs.insert(BidType.FOOD, 4);
        final boolean a4 = bs.insert(BidType.GOLD, 3);
        final boolean a5 = bs.insert(BidType.TRAP, 4);
        final boolean a6 = bs.insert(BidType.TRAP, 2);
        //  biddingslots = {[2,3,-1,-1,-1,4,-1,-1],
        //                  [3,-1,-1,-1,-1,2,-1,-1],
        //                  [4,-1,-1,-1,-1,-1,-1,-1]}
    }

    @Test
    public void testInsert() {
        assertEquals(false, bs.insert(BidType.FOOD, 3));
        assertEquals(true, bs.insert(BidType.GOLD, 3));
    }

    @Test
    public void testGetIDByBidSlot() {
        assertEquals('2', bs.getIDByBidSlot(0, 0));
        assertEquals('2', bs.getIDByBidSlot(BidType.FOOD, 0));
    }

    @Test
    public void testTypeToColumn() {
        assertEquals(0, bs.typeToColumn(BidType.FOOD));
    }

    @Test
    public void testColumnToType() {
        assertEquals(BidType.FOOD, bs.columnToType(0));
    }

    @Test
    public void testCountTrapBids() {
        assertEquals(2, bs.countTrapBids());
    }
}
