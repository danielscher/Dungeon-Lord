import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.player.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {
    Tile t1 = new Tile();
    Tile t2 = new Tile();

    @BeforeEach
    public void resetTiles() {
        t1 = new Tile();
        t2 = new Tile();
    }

    @Test
    void testIsConqured() {
        t1.setConquered();
        assertTrue(t1.isConquered());
        assertFalse(t2.isConquered());
    }

    @Test
    void testHasRoom() {
        //todo
    }
}
