package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import de.unisaarland.cs.se.selab.phase.GameEndPhase;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class EndPhaseTestTie {

    GameData gdd;
    GameEndPhase gepp;
    Dungeon d00;
    Dungeon d01;
    Dungeon d02;
    Dungeon d03;

    //drawn Rooms, player 2 and player 3 has same amount of rooms
    Room r20 = new Room(0, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r21 = new Room(1, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r32 = new Room(2, 1, 1, 1, 1, 1, Location.LOWER_HALF);
    Room r33 = new Room(3, 1, 1, 1, 1, 1, Location.LOWER_HALF);

    //set player 2 and player has same amount of imps so that they both be imps lord
    //drawn Players
    Player p00;
    Player p01;
    Player p02;
    Player p03;
    // p2 and p3 are winner, and share the title of imps and rooms,=2+2+2*2=8

    // +other titles

    private void initializeTestData() {
        final Path configPath = Path.of("src\\main\\resources\\configuration.json");
        final AltConfig altConfig = new AltConfig(configPath, 42);
        gdd = new GameData(altConfig,
                null);
        gepp = new GameEndPhase(gdd);
        gdd.registerPlayer("player00", 0);
        gdd.registerPlayer("player01", 1);
        gdd.registerPlayer("player02", 2);
        gdd.registerPlayer("player03", 3);

        p00 = gdd.getPlayerByCommID(0);
        p01 = gdd.getPlayerByCommID(1);
        p02 = gdd.getPlayerByCommID(2);
        p03 = gdd.getPlayerByCommID(3);

        d00 = new Dungeon(3, 4);
        d01 = new Dungeon(3, 4);
        d02 = new Dungeon(3, 4);
        d03 = new Dungeon(3, 4);
        d00 = p00.getDungeon();
        d01 = p01.getDungeon();
        d02 = p02.getDungeon();
        d03 = p03.getDungeon();

        //set Room
        d02.getRooms().add(r20);
        d02.getRooms().add(r21);
        d03.getRooms().add(r32);
        d03.getRooms().add(r33);
        //set Imps
        d00.addImps(0);
        d01.addImps(0);
        d02.addImps(5);
        d03.addImps(5);
    }

    @Test
    void testSetAllTitles() {
        this.initializeTestData();
        gepp.setAllTitle();
        gepp.setAllPoints();
        assertEquals(5, p00.getTitles().size());
        assertEquals(5, p01.getTitles().size());
        assertEquals(6, p02.getTitles().size());
        assertEquals(6, p03.getTitles().size());
        assertEquals(10, p00.getPoints());
        assertEquals(16, p02.getPoints());
        assertEquals(16, p03.getPoints());
        assertEquals(2, gepp.getWinnerPlayerIdList().size());
    }
}
