import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Tile;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DungeonTest {

    Dungeon dg = new Dungeon();
    private Tile[][] grid = new Tile[15][15];

    private void fillFirstRow(Tile[][] grid) {
        for (int i = 0; i < 3; i++) {
            grid[0][i] = new Tile();
            if (i < 2) {
                grid[0][i].setConquered();
            }
        }
    }

    ;

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
        //TODO: implement this.
    }

    @Test
    void getTotalDefuseVal() {
        //TODO: implement this.
    }

    @Test
    void canPlaceRoomOn() {
        //TODO: implement this.
    }
}
