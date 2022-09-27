import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.game.player.Tile;
import org.junit.jupiter.api.Test;

public class TileTest {

    Tile t1 = new Tile();
    Tile t2 = new Tile();

    @Test
    public void TestIsConqured() {
        t1.setConquered();
        assertTrue(t1.isConquered());
        assertEquals(false, t2.isConquered());
    }

    @Test
    public void TestHasRoom() {
        //todo
    }
}
