package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Dungeon {

    private Tile[][] grid;
    private final List<Monster> hiredMonsters = new ArrayList<>();
    private final List<Trap> traps = new ArrayList<>();
    private final Deque<Adventurer> adventurerQueue = new LinkedList<>();
    private final Queue<Adventurer> prison = new ArrayDeque<>();
    private Coordinate currBattleGround;
    private final List<Room> rooms = new ArrayList<>();
    private final List<Room> activeRooms = new ArrayList<>();
    private int restingImps;
    private int supervisingImps;
    private int tunnelDiggingImps;
    private int goldMiningImps;
    private int producingImps;


    public Dungeon(final int restingImps, final int gridSideLength) {
        this.restingImps = restingImps;
        this.grid = new Tile[gridSideLength][gridSideLength];
        this.grid[0][0] = new Tile();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * recalculates distances to entrance and returns closest tiles
     */
    public List<Coordinate> getPossibleBattleCoords() {
        return TileFinder.getClosestTiles(grid);
    }


    public void setGrid(final Tile[][] grid) {
        this.grid = grid;
    }

    /**
     * sums all heal values of all adventurers and returns the sum
     */
    public int getTotalHealVal() {
        int res = 0;
        for (final Adventurer adv : adventurerQueue) {
            res += adv.getHealValue();
        }
        return res;
    }

    /**
     * sums all defuse values of all adventurers and returns the sum
     */
    public int getTotalDefuseVal() {
        int res = 0;
        for (final Adventurer adv : adventurerQueue) {
            res += adv.getDefuseValue();
        }
        return res;
    }

    public boolean placeRoom(final int x, final int y, final Room room) {
        final Location loc = room.getPlacementLoc();
        if (canPlaceRoomOn(x, y, loc)) {
            rooms.add(room);
            grid[x][y].addRoom();
            return true;
        } else {
            return false;
        }
    }

    /**
     * overloading of below method to adapt to usage of coordinate class
     */
    public boolean canPlaceRoomOn(final Coordinate coordinate, final Location location) {
        return canPlaceRoomOn(coordinate.getxpos(), coordinate.getypos(), location);
    }

    /**
     * checks if a room can be placed on a given coordinate
     */
    public boolean canPlaceRoomOn(final int x, final int y, final Location location) {

        // check if coordinates are valid (in bounds)
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }

        // check if tile was already dug
        if (grid[x][y] == null) {
            return false;
        }

        // check if tile is conquered
        if (grid[x][y].isConquered()) {
            return false;
        }

        // check if tile already has a room
        if (grid[x][y].hasRoom()) {
            return false;
        }

        // check if the room would be adjacent to an already existing room
        if (TileFinder.coordinateHasAdjacentRooms(x, y, grid)) {
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
                return ((x > 0 && x < grid.length - 1) && (y > 0 && y < grid[0].length - 1));
            }
            case OUTER_RING: {
                return !((x > 0 && x < grid.length - 1) && (y > 0 && y < grid[0].length - 1));
            }
            default:
                return false;
        }
    }

    /**
     * checks if a player has at least one free tile in a given location for placing rooms
     */
    public boolean checkForFreeTilesIn(final Location location) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (canPlaceRoomOn(i, j, location)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if a tile can be dug at the given coordinates
     *
     * @param x row
     * @param y column
     * @return if you can dig here
     */
    private boolean canDig(final int x, final int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }

        if (grid[x][y] == null) {
            return (!TileFinder.wouldCreateSquare(x, y, grid) && TileFinder.hasNeighbor(x, y,
                    grid));
        } else {
            return false;
        }
    }


    /**
     * tries to dig a tile at a given location
     *
     * @param x row
     * @param y column
     * @return success
     */
    public boolean dig(final int x, final int y) {
        if (canDig(x, y)) {
            grid[x][y] = new Tile();
            return true;
        } else {
            return false;
        }
    }

    /**
     * sets a tile of the grid to conquered, if it exists
     */
    public void setTileConquered(final Coordinate xy) {
        final int x = xy.getxpos();
        final int y = xy.getypos();
        if (x >= 0 && x <= grid.length && y >= 0 && y <= grid[0].length) {
            if (grid[x][y] != null) {
                grid[x][y].setConquered();
            }
        }
    }


    public boolean isTileConquered(final Coordinate xy) {
        final int x = xy.getxpos();
        final int y = xy.getypos();
        if (grid[x][y] != null) {
            return grid[xy.getxpos()][xy.getypos()].isConquered();
        } else {
            return false;
        }
    }

    public boolean hasTileRoom(final Coordinate xy) {
        final int x = xy.getxpos();
        final int y = xy.getypos();
        if (grid[x][y] != null) {
            return grid[xy.getxpos()][xy.getypos()].hasRoom();
        } else {
            return false;
        }
    }


    /**
     * tries to send imps to mine gold
     *
     * @return success (enough resting imps available)
     */
    public boolean sendImpsToMineGold(final int amount) {
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

    /**
     * tries to send imps to dig the tunnel
     *
     * @return success (enough resting imps available)
     */
    public boolean sendImpsToDigTunnel(final int amount) {
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

    public int returnImpsFromDigging() {
        int numReturn = 0;

        numReturn += tunnelDiggingImps;
        tunnelDiggingImps = 0;
        numReturn += goldMiningImps;
        goldMiningImps = 0;
        numReturn += supervisingImps;
        supervisingImps = 0;

        restingImps += numReturn;
        return numReturn;
    }

    /**
     * activates a room by its ID
     *
     * @return success
     */
    public boolean activateRoom(final int roomId) {
        final Room roomToActivate = getRoomById(roomId);  // get room
        if (roomToActivate == null) {
            // if player doesn't have room, return false
            return false;
        }

        if (roomToActivate.isActivated()) {
            return false;
        }

        final int neededImps = roomToActivate.getActivationCost();

        if (sendImpsToProduce(neededImps)) {
            // if player has enough resting imps, they are transferred to work
            roomToActivate.activate();
            activeRooms.add(roomToActivate);
            return true;
        } else {
            // not enough imps
            return false;
        }
    }

    public List<Room> getActiveRooms() {
        return activeRooms;
    }

    /**
     * tries to send imps to dig the tunnel NOTE: should never be used without activating a room,
     * therefore private
     *
     * @return if possible (enough resting imps available)
     */
    public boolean sendImpsToProduce(final int amount) {
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

    public int returnImpsFromRoom() {
        int numReturn = 0;
        numReturn += producingImps;
        producingImps = 0;
        restingImps += numReturn;

        return numReturn;
    }

    /**
     * inserts an adventurer to the queue (with respect to the game rules)
     */
    public void insertAdventurer(final Adventurer adv) {
        if (adv.getCharge()) {
            // if warrior, insert in front
            adventurerQueue.addFirst(adv);
        } else {
            // if not a warrior, insert at the end
            adventurerQueue.addLast(adv);
        }
    }

    /**
     * gets the adventurer of the corresponding queue position NOTE: returns null if position empty
     */
    public Adventurer getAdventurer(final int index) {
        if (index >= 0 && index < adventurerQueue.size()) {
            return ((LinkedList<Adventurer>) adventurerQueue).get(index);
        } else {
            return null;
        }
    }

    /**
     * adds a monster to the dungeon
     */
    public void addMonster(final Monster monster) {
        hiredMonsters.add(monster);
    }

    public void addTrap(final Trap trap) {
        traps.add(trap);
    }

    /**
     * returns a list of all hire monsters
     */
    public List<Monster> getHiredMonsters() {
        return hiredMonsters;
    }

    public void setBattleGround(final Coordinate bg) {
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

    public int getNumAvailableMonsters() {
        int res = 0;
        for (final Monster monster : hiredMonsters) {
            if (monster.availableThisYear()) {
                res += 1;
            }

        }

        return res;
    }

    public int getNumAvailableTraps() {
        int res = 0;
        for (final Trap trap : traps) {
            if (trap.isAvailableThisYear()) {
                res += 1;

            }

        }

        return res;
    }

    public Monster getMonsterByID(final int id) {
        for (final Monster monster : hiredMonsters) {
            if (monster.getMonsterID() == id) {
                return monster;
            }
        }
        return null;
    }


    public Trap getTrapByID(final int id) {
        for (final Trap trap : traps) {
            if (trap.getTrapID() == id) {
                return trap;
            }
        }
        return null;
    }

    /**
     * returns the number of unconquered tiles
     */
    public int getNumUnconqueredTiles() {
        int res = 0;
        for (final Tile[] tileRow : grid) {
            for (final Tile tile : tileRow) {
                if (tile != null) {
                    if (!tile.isConquered()) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    /**
     * returns the number of conquered tiles
     */
    public int getNumConqueredTiles() {
        int res = 0;
        for (final Tile[] tileRow : grid) {
            for (final Tile tile : tileRow) {
                if (tile != null) {
                    if (tile.isConquered()) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    /**
     * returns the number of tiles you can mine gold on
     */
    public int getNumGoldMineAbleTiles() {
        int res = 0;
        for (final Tile[] tileRow : grid) {
            for (final Tile tile : tileRow) {
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

    public int getNumAdventurersInQueue() {
        return adventurerQueue.size();
    }

    public Deque<Adventurer> getAdventurerQueue() {
        return adventurerQueue;
    }

    /**
     * gets a room by its ID might return null if room isn't in this dungeon
     */
    public Room getRoomById(final int id) {
        for (final Room room : rooms) {
            if (room.getRoomID() == id) {
                return room;
            }
        }
        return null;
    }

    public int getRestingImps() {
        return restingImps;
    }

    /**
     * this method is used to imprison an adventurer of the queue by its id return == successfully
     * imprisoned NOTE: fails if the adventurer isn't in this player's queue
     */
    public boolean imprison(final int adventurerID) {
        final Adventurer adv = getAdventurerById(adventurerID);
        if (adv == null) {
            return false;
        }
        adventurerQueue.remove(adv);
        prison.add(adv);
        return true;
    }

    /**
     * this method is used to get an adventurer of the queue by its id NOTE: if adv isn't in the
     * queue, this method returns NULL
     */
    public Adventurer getAdventurerById(final int advID) {
        for (final Adventurer adv : adventurerQueue) {
            if (adv.getAdventurerID() == advID) {
                return adv;
            }
        }
        return null;
    }

    //adds more resting imps
    public void addImps(final int imps) {
        if (imps < 0) { // do nothing if illegal argument
            restingImps += 0;
        } else {
            restingImps += imps;
        }
    }

    public int getTunnelDiggingImps() {
        return tunnelDiggingImps;
    }

    public int getSupervisingImps() {
        return supervisingImps;
    }

    public int getGoldMiningImps() {
        return goldMiningImps;
    }

    public Coordinate getCurrBattleGround() {
        return currBattleGround;
    }

    public Adventurer fleeadventureinQueue() {
        return prison.remove();
    }
}

