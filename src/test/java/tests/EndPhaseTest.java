package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import de.unisaarland.cs.se.selab.game.util.Title;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class EndPhaseTest {

    GameData gd;
    GameEndPhase gep;

    //drawn Monsters
    Monster m1 = new Monster(1, 1, 1, 1, Attack.BASIC);
    Monster m2 = new Monster(2, 1, 1, 1, Attack.BASIC);
    //drawn Rooms
    Room r1 = new Room(1, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r2 = new Room(2, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    //drawn Players
    Player p1;
    Player p2;
    Player p3;

    Player p4;
    //drawn Adventurer


    private void resetData() {
        URI fileIdentifier;
        try {
            fileIdentifier = getClass().getClassLoader().getResource("configuration.json")
                    .toURI();
        } catch (URISyntaxException e) {
            assertNull(e,
                    "the exception isn't really supposed to be null,"
                            + " but it should also not be thrown here");
            return;
        }

        final Path configPath = Path.of(fileIdentifier);
        final AltConfig altConfig = new AltConfig(configPath, 123);
        assertTrue(altConfig.parse(), "parsing the config failed");
        gd = new GameData(altConfig,
                null);
        gep = new GameEndPhase(gd);
        gd.registerPlayer("player1", 1);
        gd.registerPlayer("player2", 2);
        gd.registerPlayer("player3", 3);
        p1 = gd.getPlayerByCommID(1);
        p2 = gd.getPlayerByCommID(2);
        p3 = gd.getPlayerByCommID(3);
        //set data of p1
        p1.changeEvilnessBy(8);
        p1.changeGoldBy(3);
        p1.changeFoodBy(3);
        //set data of p2
        p2.changeEvilnessBy(6);
        p2.changeGoldBy(2);
        p2.changeFoodBy(3);
        //set data of p3
        p3.changeEvilnessBy(3);
        p3.changeGoldBy(8);
        p3.changeFoodBy(6);
        final Dungeon d1 = p1.getDungeon();
        final Dungeon d2 = p2.getDungeon();
        final Dungeon d3 = p3.getDungeon();
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
        //!!!!!final Tile[][] grid2 = d2.getGrid();
        d2.dig(0, 1);
        //!!!!!grid2[0][0].setConquered();
        d3.dig(0, 1);
        d3.dig(0, 2);
        d3.dig(0, 3);
    }
    /*after all data
               Evil   Gold  Food  Monster  Room  ConquredTiles  Tiles  ImprisonedAdv  Imps  Score
          p1    11     3     3      2       0       0            0        1             5    4
          p2    6      2     3      0       2       1            2        0             3    8
          p3    3      8     6      0       0       0            3        0             3    9
    */



    @Test
    void testSetTunnelTitles() {
        resetData();
        gep.setTunnelTitles();
        assertEquals(Title.THE_TUNNEL_LORD, p3.getTitles().get(0));
    }

    @Test
    void testSetMonsterTitles() {
        resetData();
        gep.setMonsterTitles();
        assertEquals(Title.THE_MONSTER_LORD, p1.getTitles().get(0));
    }

    @Test
    void testSetImpsTitles() {
        resetData();
        gep.setImpsTitles();
        assertEquals(Title.THE_LORD_OF_IMPS, p1.getTitles().get(0));
    }

    @Test
    void testSetBattelLordTitles() {
        resetData();
        gep.setBattleLordTitles();
        assertEquals(Title.THE_BATTLELORD, p3.getTitles().get(0));
    }

    @Test
    void testSetRichesTitles() {
        resetData();
        gep.setRichesTitles();
        assertEquals(Title.THE_LORD_OF_RICHES, p3.getTitles().get(0));
        assertEquals(Title.THE_LORD_OF_RICHES, p4.getTitles().get(0));
        // assertEquals(Title.THE_LORD_OF_RICHES, p1.getTitles().get(0)); //WRONG!!!!!
        assertEquals(8, p3.getGold());
        assertEquals(8, p4.getGold());
        assertEquals(3, p1.getGold());
    }

    @Test
    void testSetDarkSeedTitles() {
        resetData();
        gep.setDarkSeedTitles();
        assertEquals(Title.THE_LORD_OF_DARK_DEEDS, p1.getTitles().get(0));
    }

    @Test
    void testSetHallsTitles() {
        resetData();
        gep.setHallsTitles();
        assertEquals(Title.THE_LORD_OF_HALLS, p2.getTitles().get(0));
    }
}
