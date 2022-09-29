package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;

/*
this is a helper class to find the closest tile to the entrance of a dungeon grid
 */

public final class TileFinder {

    private TileFinder() {
        // do not instantiate this class
    }

    /*
     * this method returns a List of tile coordinates
     * which have the smallest distance to the entrance
     */
    public static List<Coordinate> getClosestTiles(final Tile[][] grid) {
        clearDistances(grid);
        calcDistToEntrance(0, 0, 0, grid);
        return findClosestUnconqueredTiles(grid);
    }


    /*
     * finds the tiles which have the smallest distance value
     * NOTE: requires distance calculation to be done first
     */
    private static List<Coordinate> findClosestUnconqueredTiles(final Tile[][] grid) {
        int shortestDist = -1;
        final List<Coordinate> res = new ArrayList<>();

        // iterate over grid...
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                shortestDist = inspectTile(grid[i][j], res, shortestDist, i, j);
            }
        }
        return res; // return collection of the closest tiles
    }

    /*
     * inspects a tile, and updates the list of the closest tiles accordingly
     * return value is newest smallest distance found
     */
    private static int inspectTile(final Tile tile, final List<Coordinate> collection,
            final int distance, final int x, final int y) {
        int res = distance;
        if (!tile.isConquered()) {
            // if tile isn't conquered...
            if (distance == -1) {
                // if no unconquered tile found yet
                // take this
                res = tile.getDistanceToEntrance();
                collection.add(new Coordinate(x, y));
            } else if (distance == tile.getDistanceToEntrance()) {
                // if this tile has same distance
                // as previous closest tiles, add to list
                collection.add(new Coordinate(x, y));
            } else if (distance > tile.getDistanceToEntrance()) {
                // if tile is closer than previous, clear list and add this
                res = tile.getDistanceToEntrance();
                collection.clear();
                collection.add(new Coordinate(x, y));
            }
        }
        return res;
    }

    /*
     * clears distance values of all tiles
     */
    private static void clearDistances(final Tile[][] grid) {
        for (final Tile[] tileRow : grid) {
            for (final Tile tile : tileRow) {
                tile.setDistanceToEntrance(-1);
            }
        }
    }

    /*
    calculates the distances to the entrance
    call this method with entrance coordinates and n = 0
    requires all distance values to be -1 before execution
     */
    private static void calcDistToEntrance(final int x, final int y, final int n,
            final Tile[][] grid) {
        if (grid[x][y] != null) {
            // only try to calculate if tile exists
            if (grid[x][y].getDistanceToEntrance() > n
                    || grid[x][y].getDistanceToEntrance() == -1) {
                // if this tile hasn't been calculated yet
                // or was reached using more steps than now...
                grid[x][y].setDistanceToEntrance(n);  // change distance to needed steps

                // now check nearby tiles with incremented step counter (n+1)
                visitNeighbors(x, y, n, grid);
            }
        }
    }

    /*
    calls calcDistToEntrance again on all adjacent tiles
     */
    private static void visitNeighbors(final int x, final int y, final int n, final Tile[][] grid) {
        // if-statements prevent exceeding the array bounds
        if (x > 0) {
            calcDistToEntrance(x - 1, y, n + 1, grid);
        }
        if (y > 0) {
            calcDistToEntrance(x, y - 1, n + 1, grid);
        }
        if (x < grid.length - 1) {
            calcDistToEntrance(x + 1, y, n + 1, grid);
        }
        if (y < grid[0].length - 1) {
            calcDistToEntrance(x, y + 1, n + 1, grid);
        }
    }
}
