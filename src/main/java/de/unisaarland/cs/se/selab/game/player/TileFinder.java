package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.Collection;
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
    private static int inspectTile(final Tile tile, final Collection<Coordinate> coordinates,
            final int distance, final int x, final int y) {
        int res = distance;
        if (!tile.isConquered()) {
            // if tile isn't conquered...
            if (distance == -1) {
                // if no unconquered tile found yet
                // take this
                res = tile.getDistanceToEntrance();
                coordinates.add(new Coordinate(x, y));
            } else if (distance == tile.getDistanceToEntrance()) {
                // if this tile has same distance
                // as previous closest tiles, add to list
                coordinates.add(new Coordinate(x, y));
            } else if (distance > tile.getDistanceToEntrance()) {
                // if tile is closer than previous, clear list and add this
                res = tile.getDistanceToEntrance();
                coordinates.clear();
                coordinates.add(new Coordinate(x, y));
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

    /*
    this method check if digging at a given location would create a square
    NOTE: relies on valid coordinates
    X = given tiles, 0 = tiles to check
    a square happens when at least one of the "0" tiles are
     */
    public static boolean wouldCreateSquare(final int x, final int y, final Tile[][] grid) {
        boolean foundSquare = false; // this variable hold if at least one square would be created
        if (x > 0 && y > 0) {
            // check square to the upper left direction
            /*
            ----- x axis -----> (y axis goes from top to bottom)
            00
            0X
             */
            boolean squareHasNullTile = grid[x - 1][y] == null; // this variable holds if the
            // theoretical square has at least one null tile, which means it isn't a real square
            // to avoid
            // above check is left
            squareHasNullTile |= grid[x - 1][y - 1] == null; // upper-left
            squareHasNullTile |= grid[x][y - 1] == null; // above
            foundSquare |= !squareHasNullTile;
        }
        if (x < grid.length - 1 && y < grid[0].length - 1) {
            // check square to lower right direction
            /*
            X0
            00
             */
            boolean squareHasNullTile = grid[x + 1][y] == null; // right
            squareHasNullTile |= grid[x + 1][y + 1] == null; // lower-right
            squareHasNullTile |= grid[x][y + 1] == null; // below
            foundSquare |= !squareHasNullTile;
        }
        if (x > 0 && y < grid[0].length - 1) {
            // check square to lower left direction
            /*
            0X
            00
             */
            boolean squareHasNullTile = grid[x - 1][y] == null; // left
            squareHasNullTile |= grid[x - 1][y + 1] == null; // lower-left
            squareHasNullTile |= grid[x][y + 1] == null; // below
            foundSquare |= !squareHasNullTile;
        }
        if (x < grid.length - 1 && y < 0) {
            // checks square to upper right direction
            /*
            00
            x0
             */
            boolean squareHasNullTile = grid[x][y - 1] == null; // above
            squareHasNullTile |= grid[x + 1][y - 1] == null; // upper-right
            squareHasNullTile |= grid[x + 1][y] == null; // right
            foundSquare |= !squareHasNullTile;
        }
        return foundSquare;
    }
}
