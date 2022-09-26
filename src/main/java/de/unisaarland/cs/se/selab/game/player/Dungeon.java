package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;

import de.unisaarland.cs.se.selab.game.util.BidType;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.*;

public class Dungeon {

    private Tile[][] grid = new Tile[15][15];
    private List<Monster> hiredMonsters = new ArrayList<Monster>();
    private List<Trap> traps = new ArrayList<Trap>();
    private Deque<Adventurer> adventurerQueue = new ArrayDeque<Adventurer>();
    private Queue<Adventurer> prison = new ArrayDeque<Adventurer>();
    private int[] currAdvPos = new int[2];
    private int[] currBattleGround = new int[2];
    private List<Room> rooms = new ArrayList<Room>();
    private int restingImps, tunnelDiggingImps, goldMiningImps, producingImps;

    /*
    recalculates distances to entrance and returns closest tiles
     */
    public List<int[]> getPossibleBattleCoords() {
        clearDistances();
        calcDistToEntrance(0, 0, 0);
        return findClosestUnconqueredTiles();
    }

    /*
    finds the tiles which have the smallest distance value
    requires distance calculation to be done first
     */
    private List<int[]> findClosestUnconqueredTiles() {
        int shortestDist = -1;
        List<int[]> res = new ArrayList<int[]>();

        // iterate over grid...
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != null) {
                    // if tile exists
                    if (!grid[i][j].isConquered()) {
                        // if tile isn't conquered...
                        if (shortestDist == -1) {
                            // if no unconquered tile found yet, take this
                            shortestDist = grid[i][j].getDistanceToEntrance();
                            res.add(new int[]{i, j});
                        } else if (shortestDist == grid[i][j].getDistanceToEntrance()) {
                            // if this tile has same distance than previous closest tiles, add to list
                            res.add(new int[]{i, j});
                        } else if (shortestDist > grid[i][j].getDistanceToEntrance()) {
                            // if tile is closer than previous, clear list and add this
                            shortestDist = grid[i][j].getDistanceToEntrance();
                            res.clear();
                            res.add(new int[]{i, j});
                        }
                    }

                }
            }
        }
        return res; // return collection of the closest tiles
    }

    /*
    calculates the distances to the entrance
    call this method with entrance coordinates and n = 0
    requires all distance values to be -1 before execution
     */
    private void calcDistToEntrance(int x, int y, int n) {
        if (grid[x][y] != null) {
            // only try to calculate if tile exists
            if (grid[x][y].getDistanceToEntrance() > n
                    || grid[x][y].getDistanceToEntrance() == -1) {
                // if this tile hasn't been calculated yet or was reached using more steps than now...
                grid[x][y].setDistanceToEntrance(n);  // change distance to needed steps

                // now check nearby tiles with incremented step counter (n+1)
                // if-statements prevent exceeding the array bounds
                if (x > 0) {
                    calcDistToEntrance(x - 1, y, n + 1);
                }
                if (y > 0) {
                    calcDistToEntrance(x, y - 1, n + 1);
                }
                if (x < grid.length - 1) {
                    calcDistToEntrance(x + 1, y, n + 1);
                }
                if (y < grid[0].length - 1) {
                    calcDistToEntrance(x, y + 1, n + 1);
                }

            }
        }
    }

    /*
    sets the distance value of all tiles to the default value of -1
     */
    private void clearDistances() {
        for (Tile[] tileRow : grid) {  // for each tile row of grid
            for (Tile tile : tileRow) {  // for each tile of tile row
                if(tile != null) {
                    tile.setDistanceToEntrance(-1);  // set distance to default value
                }
            }
        }
    }

    /*
    sums all heal values of all adventurers and returns the sum
     */
    public int getTotalHealVal() {
        int res = 0;
        for (Adventurer adv :
                adventurerQueue) {
            res += adv.getHealValue();
        }
        return res;
    }

    /*
    sums all defuse values of all adventurers and returns the sum
     */
    public int getTotalDefuseVal() {
        int res = 0;
        for (Adventurer adv :
                adventurerQueue) {
            res += adv.getDefuseValue();
        }
        return res;
    }

    /*
    checks if a room can be placed on a given coordinate
     */
    public boolean canPlaceRoomOn(int x, int y, Location location) {

        // check if coordinates are valid (in bounds)
        if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }

        // check if tile was already dug
        if(grid[x][y] == null) {
            return false;
        }

        if(grid[x][y].isConquered()) {
            return false;
        }

        if(grid[x][y].hasRoom()) {
            return false;
        }

       switch (location) {
            // for return see specification
           case UPPER_HALF -> {
               return (y <= grid[0].length/2 - 1);
           }
           case LOWER_HALF -> {
               return (y > grid[0].length/2 - 1);
           }
           case INNER_RING -> {
               return ((x != 0 || x != grid.length - 1) && (y != 0 || y != grid[0].length - 1));
           }
           case OUTER_RING -> {
               return ((x == 0 || x == grid.length - 1) && (y == 0 || y == grid[0].length - 1));
           }
       }
        return false;
    }




}

