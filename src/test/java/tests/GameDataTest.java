package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class GameDataTest {

    GameData gd;
    Player p1 = new Player("Plyer1", 1, 1, 3, 15);


    private void resetGameData() {
        final Path configPath = readFile("configuration.json");
        gd = new GameData(new AltConfig(configPath, 123),
                new ServerConnection<>(8080, 5000, new ActionFactoryImplementation()));
    }

    @Test
    void testGetPlayerByCommID() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerByCommID(1), p1);
    }

    @Test
    void testGetPlayerByPlayerId() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerByPlayerId(1), p1);
    }

    @Test
    void testGetPlayerIdByCommID() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getPlayerIdByCommID(1), 1);
    }

    @Test
    void testGetCommIDByPlayerId() {
        resetGameData();
        gd.registerPlayer("Player1", 1);
        assertEquals(gd.getCommIDByPlayerId(1), 1);
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

    private Path readFile(final String fileName) {
        return Path.of(Objects.requireNonNull(getClass().getResource(fileName)).getPath());
    }
}
