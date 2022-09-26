import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.player.*;
import de.unisaarland.cs.se.selab.game.util.BidType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {


    Player testDummy = new Player("Testee",0,0);

    @BeforeEach
    public void resetPlayer () {
        testDummy = new Player("Testee",0,0);
    }

    @Test
    private void testAddBid(){
        testDummy.addBid(BidType.FOOD,1);
        testDummy.addBid(BidType.GOLD,1);
        testDummy.addBid(BidType.MONSTER,1);
        testDummy.blockBids();
        assertTrue(!testDummy.addBid(BidType.MONSTER,1), "Error: added blocked bid");
        assertTrue(!testDummy.addBid(BidType.GOLD,1), "Error: added blocked bid");
        assertTrue(!testDummy.addBid(BidType.FOOD,1), "Error: bidded on the same option twice");
    }


    @Test
    void blockBids() {
        testDummy.addBid(BidType.FOOD,1);
        testDummy.addBid(BidType.GOLD,1);
        testDummy.addBid(BidType.MONSTER,1);
        BidType[] expectedBlockedBids = {BidType.GOLD, BidType.MONSTER};
        testDummy.blockBids();
        assertEquals(2,testDummy.getBlockedBids().length,"Expected 2 blocked bids.");
        assertArrayEquals(expectedBlockedBids,testDummy.getBlockedBids(),"Expected GOLD and MONSTER but was different.");
    }

    @Test
    void clearCurrBids() {
        //TODO: implement this.
    }

    @Test
    void getBid() {
        //TODO: implement this.
    }

    @Test
    void changeEvilnessBy() {
        //TODO: implement this.
    }

    @Test
    void changeGoldBy() {
        //TODO: implement this.
    }

    @Test
    void changeFoodBy() {
        //TODO: implement this.
    }

    @Test
    void addTitle() {
        //TODO: implement this.
    }
}

