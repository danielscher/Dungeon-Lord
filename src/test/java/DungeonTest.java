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
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DungeonTest {

    Dungeon dg = new Dungeon();
    private Tile[][] grid = new Tile[15][15];
    Monster m1 = new Monster(1, 1, 1, 1, Attack.BASIC);
    Trap t1 = new Trap(1, 1, 1, Attack.BASIC);
    Room r1 = new Room(1, 4, 1, 1, 1, 1, Location.LOWER_HALF);
    Adventurer ad1 = new Adventurer(1, 1, 1, 2, 0, false);
    Adventurer ad2 = new Adventurer(1, 1, 1, 0, 2, false);
    Adventurer ad3 = new Adventurer(1, 1, 1, 0, 0, true);

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
    public void testGetPossibleBattleCoords() {
        List<int[]> coords = new ArrayList<int[]>();
        int[] arr = {0, 2};
        coords.add(arr);
        fillFirstRow(grid);
        dg.setGrid(grid);
        List<Coordinate> actualCoords = dg.getPossibleBattleCoords();
        assertTrue(coords.contains(actualCoords));
    }

    @Test
    void getTotalHealVal() {
        addAdventurer();
        assertEquals(2, dg.getTotalHealVal());
    }

    @Test
    void getTotalDefuseVal() {
        addAdventurer();
        assertEquals(2, dg.getTotalDefuseVal());
    }

    @Test
    void canPlaceRoomOn() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.canPlaceRoomOn(0, 2, UPPER_HALF));
        assertFalse(dg.canPlaceRoomOn(0, 0, UPPER_HALF));
        assertFalse(dg.canPlaceRoomOn(2, 0, LOWER_HALF));
    }

    @Test
    public void testplaceRoom() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.placeRoom(0, 2, r1));
        assertFalse(dg.placeRoom(0, 0, r1));
    }

    @Test
    public void testcheckForFreeTilesIn() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.checkForFreeTilesIn(UPPER_HALF));
        assertFalse(dg.checkForFreeTilesIn(LOWER_HALF));
    }

    @Test
    public void testdig() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertTrue(dg.dig(1, 2));
        assertFalse(dg.dig(1, 0));
    }

    @Test
    public void testisTileConquered() {
        final Coordinate coords1 = new Coordinate(0, 2);
        final Coordinate coords2 = new Coordinate(0, 0);
        assertFalse(dg.isTileConquered(coords1));
        assertTrue(dg.isTileConquered(coords2));
    }

    @Test
    public void testhasTileRoom() {
        final Coordinate coords1 = new Coordinate(0, 2);
        final Coordinate coords2 = new Coordinate(0, 0);
        fillFirstRow(grid);
        dg.setGrid(grid);
        boolean b1 = dg.placeRoom(0, 2, r1);
        assertFalse(dg.hasTileRoom(coords2));
        assertTrue(dg.hasTileRoom(coords1));
    }

    @Test
    public void testImps() {
        dg.addImps(3);
        assertEquals(6, dg.getNumImps());
        assertTrue(dg.sendImpsToProduce(1));
        assertFalse(dg.sendImpsToProduce(7));
        assertTrue(dg.sendImpsToDigTunnel(1));
        assertFalse(dg.sendImpsToDigTunnel(7));
        assertTrue(dg.sendImpsToMineGold(1));
        assertFalse(dg.sendImpsToMineGold(7));
        boolean b1 = dg.sendImpsToDigTunnel(1);
        assertEquals(5, dg.getRestingImps());
        dg.returnImps();
        assertEquals(6, dg.getRestingImps());
    }

    @Test
    public void testactivateRoom() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        dg.placeRoom(0, 2, r1);
        assertFalse(dg.activateRoom(1));
        assertFalse(dg.activateRoom(2));
        dg.addImps(3);
        assertTrue(dg.activateRoom(1));
    }

    @Test
    public void testAdventurer() {
        dg.insertAdventurer(ad1);
        dg.insertAdventurer(ad3);
        assertTrue(ad3 == dg.getAdventurer(0));
        assertTrue(ad1 == dg.getAdventurer(1));
        assertTrue(null == dg.getAdventurer(2));
        assertEquals(ad1, dg.getAdventurerById(1));
    }

    @Test
    public void testaddMonster() {
        //todo
    }

    @Test
    public void testaddTrap() {
        //todo
    }

    @Test
    public void testgetNumRooms() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        boolean b1 = dg.placeRoom(0, 2, r1);
        assertEquals(1, dg.getNumRooms());
    }

    @Test
    public void testgetNumUnconqueredTiles() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertEquals(1, dg.getNumUnconqueredTiles());
    }

    @Test
    public void testgetNumConqueredTiles() {
        fillFirstRow(grid);
        dg.setGrid(grid);
        assertEquals(2, dg.getNumConqueredTiles());
    }

    @Test
    public void testgetNumGoldMineAbleTiles() {
        //todo
    }

    @Test
    public void testgetNumImprisonedAdventurers() {
        //todo
    }

    @Test
    public void testgetRoomById() {
        //todo
    }

    @Test
    public void testimprison() {
        //todo
    }

}
