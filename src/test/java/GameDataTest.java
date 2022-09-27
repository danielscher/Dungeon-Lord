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
import org.junit.jupiter.api.Test;

public class GameDataTest {

    GameData g = new GameData();
    Player p1 = new Player("Plyer1", 1, 1);


    private Map<Integer, Integer> commIDToPlayerIdMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> playerIdToCommIDMap = new HashMap<Integer, Integer>();
    private Map<Integer, Player> idToPlayerMap = new HashMap<Integer, Player>();
    private TimeStamp time = new TimeStamp();
    //bidding square
    private List<Adventurer> currAvailableAdventurers = new ArrayList<Adventurer>();
    private List<Monster> currAvailableMonsters = new ArrayList<Monster>();
    private List<Trap> currAvailableTraps = new ArrayList<Trap>();
    private List<Room> currAvailableRooms = new ArrayList<Room>();
    //Server connection
    //config
    private int lastPlayerToStartBidding, idCounter;

    @Test
    public void testGetPlayerByCommID() {
        g.registerPlayer("Player1", 1);
        assertEquals(g.getPlayerByCommID(1), p1);
    }

    @Test
    public void testGetPlayerByPlayerId() {
        g.registerPlayer("Player1", 1);
        assertEquals(g.getPlayerByPlayerId(1), p1);
    }

    @Test
    public void testGetPlayerIdByCommID() {
        g.registerPlayer("Player1", 1);
        assertEquals(g.getPlayerIdByCommID(1), 1);
    }

    @Test
    public void testGetCommIDByPlayerId() {
        g.registerPlayer("Player1", 1);
        assertEquals(g.getCommIDByPlayerId(1), 1);
    }

    @Test
    public void testCheckIfRegistered() {
        g.registerPlayer("Player1", 1);
        assertEquals(true, g.checkIfRegistered(1));
        assertEquals(false, g.checkIfRegistered(2));
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
