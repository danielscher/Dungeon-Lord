import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import de.unisaarland.cs.se.selab.phase.Phase;
import org.junit.jupiter.api.Test;

public class EndPhaseTest {
    Player p1 = new Player("player1",1,1);
    Player p2 = new Player("player2",2,2);
    Player p3 = new Player("player3",3,3);
    GameData gd1 = new GameData();
    boolean b1 = gd1.registerPlayer("player1",1);
    boolean b2 = gd1.registerPlayer("player2",2);
    boolean b3 = gd1.registerPlayer("player3",3);
    Dungeon d1 = p1.getDungeon();
    Dungeon d2 = p2.getDungeon();
    Dungeon d3 = p3.getDungeon();
    Monster m1 = new Monster(1,1,1,1, Attack.BASIC);

    Monster m2 = new Monster(1,1,1,1, Attack.BASIC);
    Room r1 = new Room(1,1,1,1,1,1, Location.LOWER_HALF);
    Room r2 = new Room(1,1,1,1,1,1, Location.LOWER_HALF);




}
