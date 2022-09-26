import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.comm.BidType;
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

public class GameDataTest extends GameData{
    GameData g = new GameData();
    Player p1 = new Player();


    private Map<Integer, Integer> commIDToPlayerIDMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> playerIDToCommIDMap = new HashMap<Integer, Integer>();
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

    @Test void TestGetPlayerByCommID(){
        g.registerPlayer("Player1",1);
        assertEquals(g.getPlayerByCommID(1), p1);
    }
    /*
    @Test void TestGetPlayerByPlayerID(){
        g.addPlayer(p1,1);
        assertEquals(g.getPlayerByPlayerID(1),p1);
    }
    */


}
