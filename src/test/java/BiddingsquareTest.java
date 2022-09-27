import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
=======

>>>>>>> 83c99704d1be4bcc0daa8872e806aa4a12c55961
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.BiddingSquare;

import java.util.HashMap;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class BiddingsquareTest {
    BiddingSquare b = new BiddingSquare();
    private int[][] biddingSlots = new int[3][8];
    private Map<BidType, Integer> typeToColumnMap = new HashMap<BidType, Integer>();
    private Map<Integer, BidType> columnToTypeMap = new HashMap<Integer, BidType>();
    boolean a1 = b.insert(BidType.FOOD, 2);
    boolean a2 = b.insert(BidType.FOOD, 3);
    boolean a3 = b.insert(BidType.FOOD, 4);
    boolean a4 = b.insert(BidType.GOLD, 3);
    boolean a5 = b.insert(BidType.TRAP, 4);
    boolean a6 = b.insert(BidType.TRAP, 2);
//biddingslots = {[2,3,-1,-1,-1,4,-1,-1],
//                [3,-1,-1,-1,-1,2,-1,-1],
//                [4,-1,-1,-1,-1,-1,-1,-1]}

    @Test
    public void TestInsert(){
        assertEquals(false, b.insert(BidType.FOOD , 3));
        assertEquals(true, b.insert(BidType.GOLD , 3));
    }

    @Test
    public void TestGetIDByBidSlot(){
        assertEquals( '2', b.getIDByBidSlot(0, 0));
        assertEquals('2', b.getIDByBidSlot(BidType.FOOD, 0));
    }

    @Test
    public void TestTypeToColumn(){
        assertEquals(0, b.typeToColumn(BidType.FOOD) );
    }

    @Test
    public void TestColumnToType(){
        assertEquals(BidType.FOOD, b.columnToType(0));
    }

    @Test
    public void TestCountTrapBids(){
        assertEquals(2, b.countTrapBids());
    }

    @Test
    public void TestOutofIndexGetIDByBidSlot(){
    // to throw exception
    }
}
