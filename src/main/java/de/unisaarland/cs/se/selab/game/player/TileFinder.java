package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;

/*
this is a helper class to find the closest tile to the entrance of a dungeon grid
 */

public class TileFinder {
    private TileFinder()  {
        // do not instantiate this class
    }

    /*
     * this method returns a List of tile coordinates
     * which have the smallest distance to the entrance
     */
    public static List<Coordinate> getClosestTiles(final Tile [][] grid) {
        clearDistances(grid);
        calcDistToEntrance(0, 0, 0, grid);
        return findClosestUnconqueredTiles(grid);
    }


    /*
     * finds the tiles which have the smallest distance value
     * NOTE: requires distance calculation to be done first
     */
    private static List<Coordinate> findClosestUnconqueredTiles(final Tile [][] grid) {
        int shortestDist = -1;
        List<Coordinate> res = new ArrayList<>();

        // iterate over grid...
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != null) {
                    // if tile exists
                    if (!grid[i][j].isConquered()) {
                        // if tile isn't conquered...
                        if (shortestDist == -1) {
                            // if no unconquered tile found yet
                            // take this
                            shortestDist = grid[i][j].getDistanceToEntrance();
                            res.add(new Coordinate(i, j));
                        } else if (shortestDist == grid[i][j].getDistanceToEntrance()) {
                            // if this tile has same distance
                            // as previous closest tiles, add to list
                            res.add(new Coordinate(i, j));
                        } else if (shortestDist > grid[i][j].getDistanceToEntrance()) {
                            // if tile is closer than previous, clear list and add this
                            shortestDist = grid[i][j].getDistanceToEntrance();
                            res.clear();
                            res.add(new Coordinate(i, j));
                        }
                    }
                }
            }
        }
        return res; // return collection of the closest tiles
    }

    /*
     * clears distance values of all tiles
     */
    private static void clearDistances(final Tile [][] grid) {
        for (Tile[] tileRow : grid) {
            for (Tile tile : tileRow) {
                tile.setDistanceToEntrance(-1);
            }
        }
    }

    /*
    calculates the distances to the entrance
    call this method with entrance coordinates and n = 0
    requires all distance values to be -1 before execution
     */
    private  static void calcDistToEntrance(final int x, final int y, final int n,
            final Tile [][] grid) {
        if (grid[x][y] != null) {
            // only try to calculate if tile exists
            if (grid[x][y].getDistanceToEntrance() > n
                    || grid[x][y].getDistanceToEntrance() == -1) {
                // if this tile hasn't been calculated yet
                // or was reached using more steps than now...
                grid[x][y].setDistanceToEntrance(n);  // change distance to needed steps

                // now check nearby tiles with incremented step counter (n+1)
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
    }
}
