package tests;

import static de.unisaarland.cs.se.selab.game.util.Location.LOWER_HALF;
import static de.unisaarland.cs.se.selab.game.util.Location.UPPER_HALF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Tile;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DungeonTest {

    Dungeon dg = new Dungeon(3, 15);
    private final Tile[][] grid = new Tile[15][15];
    Monster m1 = new Monster(1, 1, 1, 1, Attack.BASIC);
    Trap t1 = new Trap(1, 1, 1, Attack.BASIC);
    Room r2 = new Room(2, 4, 1, 1, 1, 1, UPPER_HALF);

    Adventurer ad1 = new Adventurer(1, 1, 1, 2, 0, false);
    Adventurer ad2 = new Adventurer(2, 1, 1, 0, 2, false);
    Adventurer ad3 = new Adventurer(3, 1, 1, 0, 0, true);


    private void resetDungeon() {
        dg = new Dungeon(3, 15);
    }

    private void fillFirstRow(Tile[][] grid) {
        for (int i = 0; i < 3; i++) {
            grid[0][i] = new Tile();
            if (i < 2) {
                grid[0][i].setConquered();
            }
        }
    }

    private void addAdventurer() {
        dg.insertAdventurer(ad1);
        dg.insertAdventurer(ad2);
        dg.insertAdventurer(ad3);
    }

    @Test
    void testGetPossibleBattleCoords() {
        resetDungeon();
        final List<Coordinate> coords = new ArrayList<>();
        final Coordinate coord = new Coordinate(0, 2);
        coords.add(coord);
        fillFirstRow(grid);
        dg.setGrid(grid);
        final List<Coordinate> actualCoords = dg.getPossibleBattleCoords();
        assertEquals(coords, actualCoords);
    }

    @Test
    void testgetTotalHealVal() {
        resetDungeon();
        addAdventurer();
        assertEquals(2, dg.getTotalHealVal());
    }

    @Test
    void testgetTotalDefuseVal() {
        resetDungeon();
        addAdventurer();
        assertEquals(2, dg.getTotalDefuseVal());
    }

    @Test
    void testcanPlaceRoomOn() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.canPlaceRoomOn(0, 2, UPPER_HALF));
        assertFalse(dg.canPlaceRoomOn(0, 0, UPPER_HALF));
        assertFalse(dg.canPlaceRoomOn(2, 0, LOWER_HALF));
    }

    @Test
    void testplaceRoom() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.placeRoom(0, 2, r2));
        assertFalse(dg.placeRoom(0, 0, r2));
    }

    @Test
    void testcheckForFreeTilesIn() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.checkForFreeTilesIn(UPPER_HALF));
        assertFalse(dg.checkForFreeTilesIn(LOWER_HALF));
    }

    @Test
    void testdig() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.dig(1, 2));
        assertTrue(dg.dig(1, 0));
    }

    @Test
    void testisTileConquered() {
        resetDungeon();
        final Coordinate coords1 = new Coordinate(0, 2);
        final Coordinate coords2 = new Coordinate(0, 0);
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertFalse(dg.isTileConquered(coords1));
        assertTrue(dg.isTileConquered(coords2));
    }

    @Test
    void testhasTileRoom() {
        resetDungeon();
        final Coordinate coords1 = new Coordinate(0, 2);
        final Coordinate coords2 = new Coordinate(0, 0);
        fillFirstRow(grid);
        dg.setGrid(grid);
        dg.placeRoom(0, 2, r2); // replaced r1 by r2
        assertFalse(dg.hasTileRoom(coords2));
        assertTrue(dg.hasTileRoom(coords1));
    }

    @Test
    void testImps() {
        resetDungeon();
        dg.addImps(3);
        // asserting success/failure of sending imps to do something
        assertEquals(6, dg.getNumImps());
        assertTrue(dg.sendImpsToProduce(1));
        assertFalse(dg.sendImpsToProduce(7));
        assertTrue(dg.sendImpsToDigTunnel(1));
        assertFalse(dg.sendImpsToDigTunnel(7));
        assertTrue(dg.sendImpsToMineGold(1));
        assertFalse(dg.sendImpsToMineGold(7));

        // return previously sent imps
        dg.returnImpsFromDigging();
        dg.returnImpsFromRoom();

        dg.sendImpsToDigTunnel(1);
        assertEquals(5, dg.getRestingImps());
        /*
        dg.returnImps();
        assertEquals(6, dg.getRestingImps());
         */
        // the method returnImps is discontinued
    }

    @Test
    void testactivateRoom() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        dg.placeRoom(0, 2, r2);
        // swapped r1 to r2, since r1 has restriction LOWER_HALF but coordinates are UPPER_HALF
        assertFalse(dg.activateRoom(1)); // expect false because room not present
        assertFalse(dg.activateRoom(2)); // expect false because of lack of imps
        dg.addImps(3);
        assertTrue(dg.activateRoom(2)); // swapped 1 to 2
        assertEquals(r2, dg.getRoomById(2)); // swapped 1 to 2
    }

    @Test
    void testAdventurer() {
        resetDungeon();
        dg.insertAdventurer(ad1);
        dg.insertAdventurer(ad3);
        assertEquals(ad3, dg.getAdventurer(0));
        assertEquals(ad1, dg.getAdventurer(1));
        assertEquals(null, dg.getAdventurer(2));
        assertEquals(ad1, dg.getAdventurerById(1));
    }

    @Test
    void testaddMonster() {
        resetDungeon();
        dg.addMonster(m1);
        assertEquals(m1, dg.getHiredMonsters().get(0));
    }

    @Test
    void testTrap() {
        resetDungeon();
        dg.addTrap(t1);
        assertEquals(t1, dg.getTrapByID(1));
    }

    @Test
    void testgetNumRooms() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        dg.placeRoom(0, 2, r2); // swapped r1 to r2 because of placement restriction
        assertEquals(1, dg.getNumRooms());
    }

    @Test
    void testgetNumUnconqueredTiles() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertEquals(1, dg.getNumUnconqueredTiles());
    }

    @Test
    void testgetNumConqueredTiles() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertEquals(2, dg.getNumConqueredTiles());
    }

    @Test
    void testgetNumGoldMineAbleTiles() {
        resetDungeon();
        fillFirstRow(grid);
        dg.setGrid(grid); // now dungeon has 2 conquered tiles, 1 unconquered tile
        assertEquals(1, dg.getNumGoldMineAbleTiles());
    }

    @Test
    void testImprisonedAdventurers() {
        resetDungeon();
        dg.insertAdventurer(ad1);
        dg.insertAdventurer(ad2);
        dg.insertAdventurer(ad3);
        dg.imprison(1);
        assertEquals(1, dg.getNumImprisonedAdventurers());
    }
}
