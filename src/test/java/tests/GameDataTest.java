package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;
import org.junit.jupiter.api.Test;

class GameDataTest {

    GameData gd = new GameData();
    Player p1 = new Player("Plyer1", 1, 1);


    private void resetGameData() {
        gd = new GameData();
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
}
