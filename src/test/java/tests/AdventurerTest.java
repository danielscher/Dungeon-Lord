package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdventurerTest {
    private Adventurer a1  = new Adventurer(1, 1, 2, 1, 1, true);

    @BeforeEach
    void resetAdv() {
        a1 = new Adventurer(1, 1, 2, 1, 1, true);
    }

    @Test
    void testdamage1() {
        assertEquals(1, a1.damagehealthby(3));
        assertEquals(0, a1.getHealthPoints());
    }

    @Test
    void testdamage2() {
        assertEquals(-1, a1.damagehealthby(1));
        assertEquals(1, a1.getHealthPoints());
    }

    @Test
    void testHeal1() {
        a1.damagehealthby(2);
        assertEquals(0, a1.healBy(2));
        assertEquals(2, a1.getHealthPoints());
    }

    @Test
    void testHeal2() {
        a1.damagehealthby(2);
        assertEquals(1, a1.healBy(3));
        assertEquals(2, a1.getHealthPoints());
    }

    @Test
    void testHeal3() {
        a1.damagehealthby(2);
        assertEquals(0, a1.healBy(0));
        assertEquals(0, a1.getHealthPoints());
    }
}
