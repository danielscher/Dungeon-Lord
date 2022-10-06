package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.unisaarland.cs.se.selab.comm.BidType;
import org.junit.jupiter.api.Test;

class BidTypeTest {

    /**
     * tests the valueOf(...) method of the BidType enum with valid inputs
     */
    @Test
    void valueOfValidString() {
        assertEquals(BidType.FOOD, BidType.valueOf("FOOD"),
                "valueOf(\"FOOD\") failed");
        assertEquals(BidType.GOLD, BidType.valueOf("GOLD"),
                "valueOf(\"GOLD\") failed");
        assertEquals(BidType.IMPS, BidType.valueOf("IMPS"),
                "valueOf(\"IMPS\") failed");
        assertEquals(BidType.MONSTER, BidType.valueOf("MONSTER"),
                "valueOf(\"MONSTER\") failed");
        assertEquals(BidType.NICENESS, BidType.valueOf("NICENESS"),
                "valueOf(\"NICENESS\") failed");
        assertEquals(BidType.ROOM, BidType.valueOf("ROOM"),
                "valueOf(\"ROOM\") failed");
        assertEquals(BidType.TRAP, BidType.valueOf("TRAP"),
                "valueOf(\"TRAP\") failed");
        assertEquals(BidType.TUNNEL, BidType.valueOf("TUNNEL"),
                "valueOf(\"TUNNEL\") failed");
    }

    @Test
    void stringTest() {
        assertEquals("FOOD", BidType.FOOD.toString());
        assertEquals("GOLD", BidType.GOLD.toString());
        assertEquals("IMPS", BidType.IMPS.toString());
        assertEquals("MONSTER", BidType.MONSTER.toString());
        assertEquals("NICENESS", BidType.NICENESS.toString());
        assertEquals("ROOM", BidType.ROOM.toString());
        assertEquals("TRAP", BidType.TRAP.toString());
        assertEquals("TUNNEL", BidType.TUNNEL.toString());
    }

    /**
     * this test inputs invalid strings to the valueOf method expected behaviour: no return value,
     * throws exception
     */
    @Test
    void valueOfInvalidStringTest() {
        BidType bid = BidType.TUNNEL;
        IllegalArgumentException illegalArgumentException = null;

        try {
            bid = BidType.valueOf("FOO");
        } catch (IllegalArgumentException e) {
            illegalArgumentException = e;
        }
        // since we are supposed to get an exception, it shouldn't be null
        assertNotNull(illegalArgumentException, "got no exception");
        // since there was no valid return of "valueOf(...)", assert initial value
        assertEquals(BidType.TUNNEL, bid, "got unexpected bid");
        illegalArgumentException = null;

        try {
            bid = BidType.valueOf("food");
        } catch (IllegalArgumentException e) {
            illegalArgumentException = e;
        }
        // since we are supposed to get an exception, it shouldn't be null
        assertNotNull(illegalArgumentException, "got no exception");
        // since there was no valid return of "valueOf(...)", assert initial value
        assertEquals(BidType.TUNNEL, bid, "got unexpected bid");
        illegalArgumentException = null;

        try {
            bid = BidType.valueOf("imps");
        } catch (IllegalArgumentException e) {
            illegalArgumentException = e;
        }
        // since we are supposed to get an exception, it shouldn't be null
        assertNotNull(illegalArgumentException, "got no exception");
        // since there was no valid return of "valueOf(...)", assert initial value
        assertEquals(BidType.TUNNEL, bid, "got unexpected bid");

    }
}
