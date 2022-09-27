import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class EndPhaseTest {

    GameData gd = new GameData();
    GameEndPhase gep = new GameEndPhase(gd);
    Player p1 = new Player("player1", 1, 1);
    //drawn Monsters
    Monster m1 = new Monster(1, 1, 1, 1, Attack.BASIC);
    Monster m2 = new Monster(2, 1, 1, 1, Attack.BASIC);
    //drawn Rooms
    Room r1 = new Room(1, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r2 = new Room(2, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    private ArrayList<Integer> darkSeedPlayerIDList;
    private ArrayList<Integer> hallsPlayerIDList;
    Player p2 = new Player("player2", 2, 2);
    Player p3 = new Player("player3", 3, 3);
    private ArrayList<Integer> tunnelPlayerIDList;
    private ArrayList<Integer> monsterPlayerIDList;
    private ArrayList<Integer> impsPlayerIDList;
    Dungeon d1 = p1.getDungeon();
    Dungeon d2 = p2.getDungeon();
    Dungeon d3 = p3.getDungeon();
    private ArrayList<Integer> richesPlayerIDList;
    //riches=food+gold
    private ArrayList<Integer> battlePlayerIDList;
    private ArrayList<Integer> winnerPlayerIDList;
    private final boolean b1 = gd.registerPlayer("player1", 1);
    private final boolean b2 = gd.registerPlayer("player2", 2);
    private final boolean b3 = gd.registerPlayer("player3", 3);
    //set data of p1
    private final boolean b11 = p1.changeEvilnessBy(11);
    private final boolean b12 = p1.changeGoldBy(3);
    private final boolean b13 = p1.changeFoodBy(3);
    //           ???d1.addMonster(m1);
    //           ???d1.addMonster(m2);
    //set data of p2
    private final boolean b21 = p2.changeEvilnessBy(6);
    private final boolean b22 = p2.changeGoldBy(2);
    private final boolean b23 = p2.changeFoodBy(3);
    //set data of p3
    private final boolean b31 = p3.changeEvilnessBy(3);
    private final boolean b32 = p3.changeGoldBy(8);
    private final boolean b33 = p3.changeFoodBy(6);
    //           ???Phase p = gep.run();


    @Test
    public void testSetTunnelTitles() {
        gep.setTunnelTitles();

    }

    @Test
    public void testSetMonsterTitles() {
        //todo
    }

    @Test
    public void testSetImpsTitles() {
        //todo
    }

    @Test
    public void testSetBattelLordTitles() {
        //todo
    }

    @Test
    public void testSetRichesTitles() {
        gep.setRichesTitles();
        assertEquals(3, richesPlayerIDList.get(0));
    }

    @Test
    public void testSetDarkSeedTitles() {
        gep.setDarkSeedTitles();
        assertEquals(1, darkSeedPlayerIDList.get(0));
    }


}
