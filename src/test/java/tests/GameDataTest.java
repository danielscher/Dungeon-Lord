package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GameDataTest {

    GameData gd;


    private void resetGameData() {
        final Path configPath = Path.of("src\\main\\resources\\configuration.json");
        gd = new GameData(new AltConfig(configPath, 123),
                null);
    }


    @Test
    void testGetPlayerIdByCommID() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerIdByCommID(1), 0);
    }

    @Test
    void testGetCommIDByPlayerId() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getCommIDByPlayerId(0), 1);
    }

    @Test
    void testCheckIfRegistered() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertTrue(gd.checkIfRegistered(1));
        assertFalse(gd.checkIfRegistered(2));
    }

    @Test
    void testGetCommIDSet() {
        //todo
    }

    @Test
    void testGetNextStartPlayer() {
        //todo
    }
}
