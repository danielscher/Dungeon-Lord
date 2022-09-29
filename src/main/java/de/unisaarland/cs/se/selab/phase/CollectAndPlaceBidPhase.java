package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.List;

public class CollectAndPlaceBidPhase extends Phase {

    ServerConnection<Action> sc = gd.getServerConnection();
    Player firstBidder = gd.getAllPlayerSortedByID().get(0);

    public CollectAndPlaceBidPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {

        if (gd.getTime().getSeason() > 1) {
            broadcastNextRound(gd.getTime().getSeason());
        } else if (gd.getTime().getYear() > 1) {
            broadcastNextYear(gd.getTime().getYear());
            for (Player p : gd.getAllPlayerSortedByID()) {
                for (BidType b : p.getBlockedBids()) {
                    broadcastBidRetrieved(b, p.getPlayerID());
                }
                p.clearBlockedBids();
            }
        }

        gd.drawEntities();
        if (!gd.getCurrAvailableAdventurers().isEmpty()) {
            broadcastAdventurerDrawn(gd.getCurrAvailableAdventurers().size());
        }
        broadcastMonsterDrawn(3);
        broadcastRoomDrawn(2);

        broadcastBiddingStarted();
        broadcastActNow();

        while (!checkIfAllBidsChosen()) {
            sc.nextAction().invoke(this);
        }

        for (int i = 0; i < 3; i++) {   //go through priorities to insert bids into biddingsquare
            goThruPlayers(i);
        }

        return new EvalUpToTunnelPhase(gd);
    }

    public void exec(PlaceBidAction pba) {
        Player player = gd.getPlayerByCommID(pba.getCommID());

        if (player == null) { //if player's left the game
            return;
        }
        boolean bidAdded = player.addBid(pba.getBid(), pba.getSlot());
        if (!bidAdded) {
            sc.sendActionFailed(pba.getCommID(),
                                    "can't choose bid " + pba.getBid().toString());
        }
    }

    public void exec(ActivateRoomAction ara) {
        Player player = gd.getPlayerByCommID((ara.getCommID()));

        if (player == null) { //if player's left the game
            return;
        }
        if (player.getDungeon().getRooms().isEmpty()) {
            sc.sendActionFailed(ara.getCommID(), "You don't have any rooms.");
        } else {
            if (!player.getDungeon().activateRoom(ara.getRoomID())) {
                sc.sendActionFailed(ara.getCommID(),
                        "The chosen room can't be activated.");
            } else {
                int cost = player.getDungeon().getRoomById(ara.getRoomID()).getActivationCost();
                broadcastImpsChanged(cost, player.getPlayerID());
                broadcastRoomActivated(player.getPlayerID(), ara.getRoomID());
            }
        }
    }

    private boolean checkIfAllBidsChosen() {
        for (Player p : gd.getAllPlayerSortedByID()) {
            if (p.getNumPlacedBids() != 3) {
                return false;
            }
        }
        return true;
    }

    //go through players to get their 1st priority, insert
    //then 2nd, insert
    private void goThruPlayers(int priority) {
        BiddingSquare bs = gd.getBiddingSquare();
        List<Player> players = gd.getAllPlayerSortedByID();

        for (Player p : players) {
            if (p.getPlayerID() >= firstBidder.getPlayerID()) {
                //loop through the bidders right of the curr firstbidder
                BidType bid = p.getBid(priority);
                int slot = bs.insert(bid, p.getPlayerID());
                if (slot >= 0) {
                    broadcastBidPlaced(bid, p.getPlayerID(), slot);
                }
            }
        }
        for (Player p : players) {
            if (p.getPlayerID() < firstBidder.getPlayerID()) {
                //loop through the bidders left of the curr firstbidder
                BidType bid = p.getBid(priority);
                int slot = bs.insert(bid, p.getPlayerID());
                if (slot >= 0) {
                    broadcastBidPlaced(bid, p.getPlayerID(), slot);
                }
            }
        }
        firstBidder = nextFirstBidder();
    }

    public Player nextFirstBidder() {
        List<Player> players = gd.getAllPlayerSortedByID();
        int pos = players.indexOf(firstBidder);
        //get the position of the current first bidder
        if (pos == players.size() - 1) {
            //if current first bidder if the last one of the player list
            firstBidder = players.get(0);
            //set the first bidder to the first of list
        } else {
            firstBidder = players.get(pos + 1);
            //set the first bidder to the next of list
        }
        return firstBidder;
    }
}


