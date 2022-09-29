import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {


    Player testDummy = new Player("Testee", 0, 0);

    @BeforeEach
    public void resetPlayer() {
        testDummy = new Player("Testee", 0, 0);
    }

    @Test
    void testAddBid() {
        testDummy.addBid(BidType.FOOD, 1);
        testDummy.addBid(BidType.GOLD, 1);
        testDummy.addBid(BidType.MONSTER, 1);
        testDummy.blockBids();
        assertFalse(testDummy.addBid(BidType.MONSTER, 1), "Error: added blocked bid");
        assertFalse(testDummy.addBid(BidType.GOLD, 1), "Error: added blocked bid");
        assertFalse(testDummy.addBid(BidType.FOOD, 1), "Error: bidded on the same option twice");
    }


    @Test
    void blockBids() {
        testDummy.addBid(BidType.FOOD, 1);
        testDummy.addBid(BidType.GOLD, 1);
        testDummy.addBid(BidType.MONSTER, 1);
        BidType[] expectedBlockedBids = {BidType.GOLD, BidType.MONSTER};
        testDummy.blockBids();
        assertEquals(2, testDummy.getBlockedBids().length, "Expected 2 blocked bids.");
        assertArrayEquals(expectedBlockedBids, testDummy.getBlockedBids(),
                "Expected GOLD and MONSTER but was different.");
    }

    @Test
    void clearCurrBids() {
        //TODO: implement this.
    }

    @Test
    void getBid() {
        testDummy.addBid(BidType.FOOD, 0);
        testDummy.addBid(BidType.GOLD, 1);
        assertEquals(BidType.FOOD, testDummy.getBid(0));
        assertEquals(BidType.GOLD, testDummy.getBid(1));
        assertEquals(null, testDummy.getBid(3));
    }

    @Test
    void changeEvilnessBy() {
        assertTrue(testDummy.changeEvilnessBy(8));
        assertTrue(testDummy.changeEvilnessBy(-2));
        assertFalse(testDummy.changeEvilnessBy(11));
        testDummy.changeEvilnessBy(5);
        assertEquals(10, testDummy.getEvilLevel());
    }

    @Test
    void changeGoldBy() {
        assertTrue(testDummy.changeGoldBy(1));
        assertFalse(testDummy.changeGoldBy(-1));
        testDummy.changeGoldBy(1);
        assertEquals(1, testDummy.getGold());
    }

    @Test
    void changeFoodBy() {
        assertTrue(testDummy.changeFoodBy(1));
        assertFalse(testDummy.changeFoodBy(-1));
        testDummy.changeFoodBy(1);
        assertEquals(1, testDummy.getFood());
    }

    @Test
    public void testGetNumPlacedBids() {
        assertEquals(0, testDummy.getNumPlacedBids());
        testDummy.addBid(BidType.FOOD, 0);
        testDummy.addBid(BidType.GOLD, 1);
        assertEquals(2, testDummy.getNumPlacedBids());
    }

    @Test
    void addTitle() {
        //TODO: implement this.
    }
}

