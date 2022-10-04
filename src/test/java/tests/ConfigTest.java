package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import de.unisaarland.cs.se.selab.game.Config;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.ParserMessage;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.io.IOException;
import java.util.Random;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class ConfigTest extends Config {

    private static final String MYPATH = "src/test/resources/configuration.json";

    // "C:\\Users\\forgo\\group35\\src\\main\\resources\\configuration.json";
    private static final String MYPATHFAIL = "src/test/resources/config_broken.json";
    // "C:\\Users\\forgo\\group35\\src\\main\\resources\\config_broken.json";
    private static final Random R = new Random(42);


    Config cfg = new Config();


    @Test
    void testParseOk() {
        cfg.setConfigFilePath(MYPATH);
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final ParserMessage res = cfg.getParserResult();
        assertEquals(ParserMessage.SUCCESSD, res);
    }

    @Test
    void testParseNotOk() {
        cfg.setConfigFilePath(MYPATHFAIL);
        try {
            cfg.parse(MYPATHFAIL, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
    }


    @Test
    void testGetter() {
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final int trapNum = cfg.getAllTraps().size();
        assertEquals(51, trapNum);

        final int roomNum = cfg.getAllRooms().size();
        assertEquals(16, roomNum);

        final int adventurerNum = cfg.getAllAdventurers().size();
        assertEquals(32, adventurerNum);

        final int monsterNum = cfg.getAllMonsters().size();
        assertEquals(24, monsterNum);

        final int maxYear = cfg.getMaxYear();
        assertEquals(2, maxYear);

        final int maxPlayer = cfg.getMaxPlayers();
        assertEquals(4, maxPlayer);

        final int dungeonSize = cfg.getDungeonSidelength();
        assertEquals(5, dungeonSize);

        final int initFood = cfg.getInitFood();
        assertEquals(3, initFood);

        final int initGold = cfg.getInitGold();
        assertEquals(3, initGold);

        final int initImps = cfg.getInitImps();
        assertEquals(3, initImps);

    }

    @Test
    void testConvertAttack() {
        final Attack a = cfg.convertStringToAttack("BASIC");
        assertEquals(Attack.BASIC, a);
    }

    @Test
    void testConvertLocationFailed() {
        try {
            cfg.convertStringToLocation("42");
        } catch (IllegalArgumentException e) {
            final Location l = cfg.convertStringToLocation("OUTER_RING");
            assertEquals(Location.OUTER_RING, l);
        }
    }

    @Test
    void testDeleteMonsters() {
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final int before = cfg.getAllMonsters().size();
        cfg.drawMonsters();
        final int after = cfg.getAllMonsters().size();
        assertEquals(after, (before - 3));
    }

    @Test
    void testDeleteAdventurers() {
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final int before = cfg.getAllAdventurers().size();
        cfg.drawAdventurers(5);
        final int after = cfg.getAllAdventurers().size();
        assertEquals(after, (before - 5));
    }

    @Test
    void testDeleteTraps() {
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final int before = cfg.getAllTraps().size();
        cfg.drawTraps(1);
        final int after = cfg.getAllTraps().size();
        assertEquals(after, (before - 1));
    }

    @Test
    void testDeleteRooms() {
        try {
            cfg.parse(MYPATH, R);
        } catch (IOException | JSONException e) {
            final ParserMessage res = cfg.getParserResult();
            assertNotEquals(ParserMessage.SUCCESSD, res);
        }
        final int before = cfg.getAllRooms().size();
        cfg.drawRooms();
        final int after = cfg.getAllRooms().size();
        assertEquals(after, (before - 2));
    }

}
