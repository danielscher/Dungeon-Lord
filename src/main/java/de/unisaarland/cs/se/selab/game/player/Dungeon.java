package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Dungeon {

    private Tile[][] grid = new Tile[15][15];
    private List<Monster> hiredMonsters = new ArrayList<Monster>();
    private List<Trap> traps = new ArrayList<Trap>();
    private LinkedList<Adventurer> adventurerQueue = new LinkedList<Adventurer>();
    private Queue<Adventurer> prison = new ArrayDeque<Adventurer>();
    private Coordinate currAdvPos;
    private Coordinate currBattleGround;
    private List<Room> rooms = new ArrayList<Room>();
    private int restingImps;
    private int supervisingImps;
    private int tunnelDiggingImps;
    private int goldMiningImps;
    private int producingImps;

    public Dungeon() {
        restingImps = 3;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    /*
    recalculates distances to entrance and returns closest tiles
     */
    public List<Coordinate> getPossibleBattleCoords() {
        clearDistances();
        calcDistToEntrance(0, 0, 0);
        return findClosestUnconqueredTiles();
    }

    /*
    finds the tiles which have the smallest distance value
    requires distance calculation to be done first
     */
    private List<Coordinate> findClosestUnconqueredTiles() {
        int shortestDist = -1;
        List<Coordinate> res = new ArrayList<Coordinate>();

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
    calculates the distances to the entrance
    call this method with entrance coordinates and n = 0
    requires all distance values to be -1 before execution
     */
    private void calcDistToEntrance(int x, int y, int n) {
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
                if (tile != null) {
                    tile.setDistanceToEntrance(-1);  // set distance to default value
                }
            }
        }
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public void setGrid(Tile[][] grid) {
        this.grid = grid;
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

    public boolean placeRoom(int x, int y, Room room) {
        Location loc = room.getPlacementLoc();
        if (canPlaceRoomOn(x, y, loc)) {
            rooms.add(room);
            grid[x][y].addRoom();
            return true;
        } else {
            return false;
        }
    }


    /*
    checks if a room can be placed on a given coordinate
     */
    public boolean canPlaceRoomOn(int x, int y, Location location) {

        // check if coordinates are valid (in bounds)
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }

        // check if tile was already dug
        if (grid[x][y] == null) {
            return false;
        }

        if (grid[x][y].isConquered()) {
            return false;
        }

        if (grid[x][y].hasRoom()) {
            return false;
        }

        switch (location) {
            // for return see specification
            case UPPER_HALF: {
                return (y <= grid[0].length / 2 - 1);
            }
            case LOWER_HALF: {
                return (y > grid[0].length / 2 - 1);
            }
            case INNER_RING: {
                return ((x != 0 || x != grid.length - 1) && (y != 0 || y != grid[0].length - 1));
            }
            case OUTER_RING: {
                return ((x == 0 || x == grid.length - 1) && (y == 0 || y == grid[0].length - 1));
            }
            default:
                return false;
        }
    }

    /*
    checks if a player has at least one free tile in a given location for placing rooms
     */
    public boolean checkForFreeTilesIn(Location location) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (canPlaceRoomOn(i, j, location)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    checks if a tile can be dug at the given coordinates
     */
    private boolean canDig(int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }

        return (grid[x][y] == null);
    }

    /*
    tries to dig a tile at a given location
    return == success
     */
    public boolean dig(int x, int y) {
        if (canDig(x, y)) {
            grid[x][y] = new Tile();
            return true;
        } else {
            return false;
        }
    }


    public boolean isTileConquered(int x, int y) {
        return grid[x][y].isConquered();
    }

    public boolean hasTileRoom(int x, int y) {
        return grid[x][y].hasRoom();
    }


    //returns all imps doing tasks
    public void returnImps() {
        restingImps += tunnelDiggingImps;
        tunnelDiggingImps = 0;
        restingImps += goldMiningImps;
        goldMiningImps = 0;
        restingImps += producingImps;
        producingImps = 0;
        restingImps += supervisingImps;
        supervisingImps = 0;
    }

    /*
    tries to send imps to mine gold
    return == possible (enough resting imps available)
     */
    public boolean sendImpsToMineGold(int amount) {
        if (amount < 0) {
            return false;
        }
        if (amount > restingImps) {
            return false;
        }
        if (amount < 4) {
            restingImps -= amount;
            goldMiningImps += amount;
            return true;
        }
        //for more than 4 imps send one to supervise
        if (amount >= 4 && restingImps > amount) {
            restingImps -= amount + 1;
            goldMiningImps += amount;
            supervisingImps += 1;
            return true;
        } else {
            return false;
        }
    }

    /*
    tries to send imps to dig the tunnel
    return == possible (enough resting imps available)
     */
    public boolean sendImpsToDigTunnel(int amount) {
        if (amount < 0) {
            return false;
        }
        if (amount > restingImps) {
            return false;
        }

        if (amount < 4) {
            restingImps -= amount;
            tunnelDiggingImps += amount;
            return true;
        }
        //for more than 4 imps send one to supervise
        if (amount >= 4 && restingImps > amount) {
            restingImps -= amount + 1;
            tunnelDiggingImps += amount;
            supervisingImps += 1;
            return true;
        } else {
            return false;
        }
    }

    /*
    activates a room by its ID
    return == success?
     */
    public boolean activateRoom(int roomId) {
        Room roomToActivate = getRoomById(roomId);  // get room
        if (roomToActivate == null) {
            // if player doesnt have room, return false
            return false;
        }

        if (roomToActivate.isActivated()) {
            return false;
        }

        int neededImps = roomToActivate.getActivationCost();

        if (sendImpsToProduce(neededImps)) {
            // if player has enough resting imps, they are transferred to work
            roomToActivate.activate();
            return true;
        } else {
            // not enough imps
            return false;
        }
    }

    /*
    tries to send imps to dig the tunnel
    return == possible (enough resting imps available)
    NOTE: should never be used without activating a room, therefore private
     */
    public boolean sendImpsToProduce(int amount) {
        if (amount < 0) {
            return false;
        }
        if (amount > restingImps) {
            return false;
        }
        restingImps -= amount;
        producingImps += amount;
        return true;
    }

    /*
    inserts an adventurer to the queue (with respect to the game rules)
     */
    public void insertAdventurer(Adventurer adv) {
        if (adv.getCharge()) {
            // if warrior, insert in front
            adventurerQueue.addFirst(adv);
        } else {
            // if not a warrior, insert at the end
            adventurerQueue.addLast(adv);
        }
    }

    /*
    gets the adventurer of the corresponding queue position
    NOTE: returns null if position empty
     */
    public Adventurer getAdventurer(int index) {
        if (index >= 0 && index < adventurerQueue.size()) {
            return adventurerQueue.get(index);
        } else {
            return null;
        }
    }

    /*
    adds a monster to the dungeon
     */
    public void addMonster(Monster monster) {
        hiredMonsters.add(monster);
    }

    /*
    returns a list of all hire monsters
     */
    public List<Monster> getHiredMonsters() {
        return hiredMonsters;
    }

    public void setBattleGround(Coordinate bg) {
        currBattleGround = bg;
    }

    public int getNumImps() {
        int res = restingImps;
        res += tunnelDiggingImps;
        res += producingImps;
        res += goldMiningImps;
        return res;
    }

    public int getNumRooms() {
        return rooms.size();
    }

    /*
    returns the number of unconquered tiles
     */
    public int getNumUnconqueredTiles() {
        int res = 0;
        for (Tile[] tileRow : grid) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    if (!tile.isConquered()) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    /*
    returns the number of conquered tiles
     */
    public int getNumConqueredTiles() {
        int res = 0;
        for (Tile[] tileRow : grid) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    if (tile.isConquered()) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    /*
    returns the number of tiles you can mine gold on
     */
    public int getNumGoldMineAbleTiles() {
        int res = 0;
        for (Tile[] tileRow : grid) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    if (!tile.isConquered() && !tile.hasRoom()) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    public int getNumImprisonedAdventurers() {
        return prison.size();
    }

    /*
    gets a room by its ID
    might return null if room isn't in this dungeon
     */
    public Room getRoomById(int id) {
        for (Room room : rooms) {
            if (room.getRoomID() == id) {
                return room;
            }
        }
        return null;
    }

    public int getRestingImps() {
        return restingImps;
    }

    /*
    this method is used to imprison an adventurer of the queue by its id
    return == successfully imprisoned
    NOTE: fails if the adventurer isn't in this player's queue
     */
    public boolean imprison(int adventurerID) {
        Adventurer adv = getAdventurerById(adventurerID);
        if(adv == null) {
            return false;
        }
        adventurerQueue.remove(adv);
        prison.add(adv);
        return true;
    }

    /*
    this method is used to get an adventurer of the queue by its id
    NOTE: if adv isn't in the queue, this method returns NULL
     */
    public Adventurer getAdventurerById(int advID) {
        for (Adventurer adv : adventurerQueue) {
            if (adv.getAdventurerID() == advID) {
                return adv;
            }
        }
        return null;
    }

    //adds more resting imps
    public void addImps(int imps) {
        if (imps < 0) { // do nothing if illegal argument
            restingImps += 0;
        } else {
            restingImps += imps;
        }
    }
}

