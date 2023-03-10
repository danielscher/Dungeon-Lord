package de.unisaarland.cs.se.selab.game.player;

//import de.unisaarland.cs.se.selab.game.util.BidType;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {


    private final String name;
    private final int playerID;
    private final int commID;
    private final Dungeon dungeon;
    private BidType[] currBids = new BidType[3];
    private BidType[] blockedBids = new BidType[2];
    private final List<Title> titles = new ArrayList<>();
    private int points;
    private int evilLevel;
    private int gold;
    private int food;

    public Player(final String name, final int playerID, final int commID, final int initialFood,
            final int initialGold, final int imps, final int initialEvilness,
            final int gridSideLength) {
        this.name = name;
        this.playerID = playerID;
        this.commID = commID;
        this.dungeon = new Dungeon(imps, gridSideLength);
        this.points = 0;
        this.evilLevel = initialEvilness;
        this.gold = initialGold;
        this.food = initialFood;
    }

    /**
     * tries to add a bid to the currBids array
     *
     * @return success?
     */
    public boolean addBid(final BidType type, final int priority) {
        final int bidIndex = priority - 1;
        // priority == 0 means "bid number 1" within the game rules
        if (canAddBid(type, bidIndex)) {
            currBids[bidIndex] = type;
            return true;
        } else {
            return false;
        }
    }

    /**
     * needed for the Event of the bids that are available again
     */
    public BidType[] getBlockedBids() {
        return blockedBids.clone();
    }

    /**
     * blocks bids according to rules
     */
    public void blockBids() {
        blockedBids[0] = currBids[1];
        blockedBids[1] = currBids[2];
    }

    /**
     * clears array of current bids
     */
    public void clearCurrBids() {
        Arrays.fill(currBids, null);
    }

    public void clearBlockedBids() {
        Arrays.fill(blockedBids, null);
    }

    /**
     * gets a bid of the current bids, by the priority of the bid
     */
    public BidType getBid(final int priority) {
        if (priority >= 0 && priority < currBids.length) {
            // only if priority is valid try to return
            return currBids[priority];
        } else {
            // if priority invalid, return null
            return null;
        }
    }

    /**
     * checks if bid can be added
     */
    private boolean canAddBid(final BidType type, final int priority) {
        // check if requested BidType is already in currBids
        for (final BidType bid : currBids) {
            // for each bid of the currBids array
            if (bid == type) {
                // if requested bid is in bid set, deny
                return false;
            }
        }

        // check if requested BidType is in blockedBids array
        for (final BidType bid : blockedBids) {
            // for each bid of the blocked bids
            if (bid == type) {
                // if requested bid is in blocked bid set, deny
                return false;
            }
        }

        // check if requested priority is a valid priority
        if (priority < 0 || priority >= currBids.length) {
            return false;
        }

        // check if there already is a bid with this priority
        /** if (currBids[priority] != null) {
         return false;
         }
         */
        // if it didn't return until now, every requirement is fulfilled, bid can be added
        // return true;
        return (currBids[priority] == null); // improvement suggested by pmd
    }

    /**
     * tries to change evilness positive amounts may fail because of the limit of 15 (return ==
     * false) negative amounts always succeed (return == true), but evilness will not go below 0
     */
    public boolean changeEvilnessBy(final int amount) {
        if (amount > 0) {
            // if evilness increases, check if it exceeds bounds
            if (evilLevel + amount <= 15) {
                evilLevel += amount;
                return true;
            } else {
                return false;
            }
        } else {
            // if evilness decreases, just change until 0
            if (evilLevel + amount < 0) {
                evilLevel = 0;
            } else {
                evilLevel += amount;
            }
            return true;
        }
    }

    /**
     * tries to change the amount of gold
     *
     * @return success
     */
    public boolean changeGoldBy(final int amount) {
        if (gold + amount < 0) {
            return false;
        } else {
            gold += amount;
            return true;
        }
    }

    /**
     * tries to change the amount of food
     *
     * @return success
     */
    public boolean changeFoodBy(final int amount) {
        if (food + amount < 0) {
            return false;
        } else {
            food += amount;
            return true;
        }
    }

    public int getNumPlacedBids() {
        int res = 0;
        for (final BidType bid : currBids) {
            if (bid != null) {
                res++;
            }
        }
        return res;
    }

    public int getEvilLevel() {
        return evilLevel;
    }

    public int getGold() {
        return gold;
    }

    public int getFood() {
        return food;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public void changePointsBy(final int amount) {
        points += amount;
    }

    public void addTitle(final Title title) {
        titles.add(title);
    }

    public List<Title> getTitles() {
        return titles;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCommID() {
        return commID;
    }

    public String getName() {
        return name;
    }
}
