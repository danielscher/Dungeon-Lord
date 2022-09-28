import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.player.Tile;
import de.unisaarland.cs.se.selab.game.util.Location;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EndPhaseTest {

    GameData gd = new GameData();
    GameEndPhase gep = new GameEndPhase(gd);

    //drawn Monsters
    Monster m1 = new Monster(1, 1, 1, 1, Attack.BASIC);
    Monster m2 = new Monster(2, 1, 1, 1, Attack.BASIC);
    //drawn Rooms
    Room r1 = new Room(1, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r2 = new Room(2, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    //drawn Players
    Player p1 = new Player("player1", 1, 1);
    Player p2 = new Player("player2", 2, 2);
    Player p3 = new Player("player3", 3, 3);
    //drawn Dungeon
    Dungeon d1 = new Dungeon();
    Dungeon d2 = new Dungeon();
    Dungeon d3 = new Dungeon();
    //drawn Adventurer
    Adventurer ad1 = new Adventurer(1, 1, 0, 0, 0, false);
    private final boolean b1 = gd.registerPlayer("player1", 1);
    private final boolean b2 = gd.registerPlayer("player2", 2);
    private final boolean b3 = gd.registerPlayer("player3", 3);
    private ArrayList<Integer> darkSeedPlayerIDList;
    private ArrayList<Integer> hallsPlayerIDList;
    private ArrayList<Integer> tunnelPlayerIDList;
    private ArrayList<Integer> monsterPlayerIDList;
    private ArrayList<Integer> impsPlayerIDList;
    private ArrayList<Integer> richesPlayerIDList;
    //riches=food+gold
    private ArrayList<Integer> battlePlayerIDList;
    private ArrayList<Integer> winnerPlayerIDList;
    //set data of p1
    private boolean b11 = p1.changeEvilnessBy(11);
    private boolean b12 = p1.changeGoldBy(3);
    private boolean b13 = p1.changeFoodBy(3);
    //set data of p2
    private boolean b21 = p2.changeEvilnessBy(6);
    private boolean b22 = p2.changeGoldBy(2);
    private boolean b23 = p2.changeFoodBy(3);
    //set data of p3
    private boolean b31 = p3.changeEvilnessBy(3);
    private boolean b32 = p3.changeGoldBy(8);
    private boolean b33 = p3.changeFoodBy(6);

    private void setDungeon() {
        d1 = p1.getDungeon();
        d2 = p2.getDungeon();
        d3 = p3.getDungeon();
        //imprisonAdv
        d1.imprison(1);
        //set Monster
        d1.addMonster(m1);
        d1.addMonster(m2);
        //set Room
        d1.placeRoom(0, 0, r1);
        d1.placeRoom(0, 1, r2);
        //set Imps
        d1.addImps(2);
        //set Tiles
        Tile[][] grid2 = d2.getGrid();
        d2.dig(0, 0);
        d2.dig(0, 1);
        grid2[0][0].setConquered();
        d3.dig(0, 0);
        d3.dig(0, 1);
        d3.dig(1, 1);
    }

    /*after all data
               Evil   Gold  Food  Monster  Room  ConquredTiles  Tiles  ImprisonedAdv  Imps  Score
          p1    11     3     3      2       0       0            0        1             5    4
          p2    6      2     3      0       2       1            2        0             3    8
          p3    3      8     6      0       0       0            3        0             3    9
    */

    @Test
    public void testSetTunnelTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setTunnelTitles();
        assertEquals(3, tunnelPlayerIDList.get(0));
    }

    @Test
    public void testSetMonsterTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setMonsterTitles();
        assertEquals(1, monsterPlayerIDList.get(0));
    }

    @Test
    public void testSetImpsTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setImpsTitles();
        assertEquals(1, impsPlayerIDList.get(0));
    }

    @Test
    public void testSetBattelLordTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setHallsTitles();
        assertEquals(1, battlePlayerIDList.get(0));
    }

    @Test
    public void testSetRichesTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setRichesTitles();
        assertEquals(3, richesPlayerIDList.get(0));
    }

    @Test
    public void testSetDarkSeedTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setDarkSeedTitles();
        assertEquals(1, darkSeedPlayerIDList.get(0));
    }

    @Test
    public void testSetHallsTitles() {
        setDungeon();
        gep.evaluateScores();
        gep.setHallsTitles();
        assertEquals(2, hallsPlayerIDList.get(0));
    }

    @Test
    public void testwinner() {
        setDungeon();
        gep.evaluateScores();
        assertEquals(3, winnerPlayerIDList.get(0));
    }


}
