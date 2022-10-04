package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GameDataTest {

    GameData gd;


    private void resetGameData() {
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
