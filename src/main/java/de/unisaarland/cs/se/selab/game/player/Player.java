package de.unisaarland.cs.se.selab.game.player;

//import de.unisaarland.cs.se.selab.game.util.BidType;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.game.util.Title;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {

    private String name;
    private int playerID, commID;
    private Dungeon dungeon;
    private BidType[] currBids = new BidType[3];
    private BidType[] blockedBids = new BidType[3];
    private List<Title> titles = new ArrayList<Title>();
    private int points;
    private int evilLevel, gold, food;

    public Player(String name, int playerID, int commID) {
        this.name = name;
        this.playerID = playerID;
        this.commID = commID;
        this.dungeon = new Dungeon();
        this.points = 0;
        this.evilLevel = 5;  // 5 is standard level (see specification)
        this.gold = 0;
        this.food = 0;
    }

    public Player() {
        // TODO: remove when we have no more dummy methods in other classes (GameData)
    }

    /*
    tries to add a bid to the currBids array
    return == success??
     */
    public boolean addBid(BidType type, int priority) {
        // priority == 0 means "bid number 1" within the game rules
        if (canAddBid(type, priority)) {
            currBids[priority] = type;
            return true;
        } else {
            return false;
        }
    }

    /*
    needed for the Event of the bids that are available again
     */
    public BidType[] getBlockedBids() {
        return blockedBids;
    }

    /*
        blocks bids according to rules
         */
    public void blockBids() {
        blockedBids[0] = currBids[1];
        blockedBids[1] = currBids[2];
    }

    /*
    clears array of current bids
     */
    public void clearCurrBids() {
        Arrays.fill(currBids, null);
    }

    /*
    gets a bid of the current bids, by the priority of the bid
     */
    public BidType getBid(int priority) {
        if (priority >= 0 && priority < currBids.length) {
            // only if priority is valid try to return
            return currBids[priority];
        } else {
            // if priority invalid, return null
            return null;
        }
    }

    /*
    checks if bid can be added
     */
    private boolean canAddBid(BidType type, int priority) {
        // check if requested BidType is already in currBids
        for (BidType bid : currBids) {
            // for each bid of the currBids array
            if (bid == type) {
                // if requested bid is in bid set, deny
                return false;
            }
        }

        // check if requested BidType is in blockedBids array
        for (BidType bid : blockedBids) {
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
        if (currBids[priority] != null) {
            return false;
        }

        // if it didn't return until now, every requirement is fulfilled, bid can be added
        return true;
    }

    /*
    tries to change evilness
    positive amounts may fail because of the limit of 15 (return == false)
    negative amounts always succeed (return == true), but evilness will not go below 0
     */
    public boolean changeEvilnessBy(int amount) {
        if (amount > 0) {
            // if evilness increases, check if it exceeds bounds
            if (evilLevel + amount > 15) {
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

    /*
    tries to change the amount of gold
    return == success?
     */
    public boolean changeGoldBy(int amount) {
        if (gold + amount < 0) {
            return false;
        } else {
            gold += amount;
            return true;
        }
    }

    /*
    tries to change the amount of food
    return == success?
     */
    public boolean changeFoodBy(int amount) {
        if (food + amount < 0) {
            return false;
        } else {
            food += amount;
            return true;
        }
    }

    public int getNumPlacedBids() {
        int res = 0;
        for (BidType bid : currBids) {
            if(bid != null) {
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

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void addTitle(Title title) {
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
}
