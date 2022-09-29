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
import de.unisaarland.cs.se.selab.game.util.Title;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void resetData() {
        gd = new GameData();
        gep = new GameEndPhase(gd);
        p1 = new Player("player1", 1, 1);
        p2 = new Player("player2", 2, 2);
        p3 = new Player("player3", 3, 3);
        d1 = new Dungeon();
        d2 = new Dungeon();
        d3 = new Dungeon();
        boolean b1 = gd.registerPlayer("player1", 1);
        boolean b2 = gd.registerPlayer("player2", 2);
        boolean b3 = gd.registerPlayer("player3", 3);
        //set data of p1
        boolean b11 = p1.changeEvilnessBy(11);
        boolean b12 = p1.changeGoldBy(3);
        boolean b13 = p1.changeFoodBy(3);
        //set data of p2
        boolean b21 = p2.changeEvilnessBy(6);
        boolean b22 = p2.changeGoldBy(2);
        boolean b23 = p2.changeFoodBy(3);
        //set data of p3
        boolean b31 = p3.changeEvilnessBy(3);
        boolean b32 = p3.changeGoldBy(8);
        boolean b33 = p3.changeFoodBy(6);
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
        gep.setTunnelTitles();
        assertEquals(Title.THE_TUNNEL_LORD, p3.getTitles().get(0));
    }

    @Test
    public void testSetMonsterTitles() {
        gep.setMonsterTitles();
        assertEquals(Title.THE_MONSTER_LORD, p1.getTitles().get(0));
    }

    @Test
    public void testSetImpsTitles() {
        gep.setImpsTitles();
        assertEquals(Title.THE_LORD_OF_IMPS, p1.getTitles().get(0));
    }

    @Test
    public void testSetBattelLordTitles() {
        gep.setHallsTitles();
        assertEquals(Title.THE_BATTLELORD, p1.getTitles().get(0));
    }

    @Test
    public void testSetRichesTitles() {
        gep.setRichesTitles();
        assertEquals(Title.THE_LORD_OF_RICHES, p3.getTitles().get(0));
    }

    @Test
    public void testSetDarkSeedTitles() {
        gep.setDarkSeedTitles();
        assertEquals(Title.THE_LORD_OF_DARK_DEEDS, p1.getTitles().get(0));
    }

    @Test
    public void testSetHallsTitles() {
        gep.setHallsTitles();
        assertEquals(Title.THE_LORD_OF_HALLS, p2.getTitles().get(0));
    }

    @Test
    public void testwinner() {
        gep.evaluateScores();
        //assertEquals(3, winnerPlayerIDList.get(0));
    }


}
