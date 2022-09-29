import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.TimeStamp;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameDataTest {

    GameData gd = new GameData();
    Player p1 = new Player("Plyer1", 1, 1);

    @BeforeEach
    public void resetGameData() {
        gd = new GameData();
    }

    @Test
    public void testGetPlayerByCommID() {
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerByCommID(1), p1);
    }

    @Test
    public void testGetPlayerByPlayerId() {
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerByPlayerId(1), p1);
    }

    @Test
    public void testGetPlayerIdByCommID() {
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerIdByCommID(1), 1);
    }

    @Test
    public void testGetCommIDByPlayerId() {
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getCommIDByPlayerId(1), 1);
    }

    @Test
    public void testCheckIfRegistered() {
        gd.registerPlayer("Player1", 1);
        assertEquals(true, gd.checkIfRegistered(1));
        assertEquals(false, gd.checkIfRegistered(2));
    }

    @Test
    public void testGetCommIDSet() {
        //todo
    }

    @Test
    public void testGetNextStartPlayer() {
        //todo
    }
}
