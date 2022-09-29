package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.Set;

public class CollectAndPlaceBidPhase extends Phase {

    ServerConnection<Action> sc = gd.getServerConnection();

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

        for (int i = 0; i < 3; i++) {
            //go through players to get their first bids, insert,
            // then 2nd bids of players, insert
            for (Player p : gd.getAllPlayerSortedByID()) {
                boolean inserted = gd.getBiddingSquare().insert(p.getBid(i), p.getPlayerID());
                if (!inserted) {
                    throw new IllegalStateException("Slot occupied");
                } else {
                    broadcastBidPlaced(p.getBid(i), p.getPlayerID(), i);
                }
            }
        }
        return new EvalUpToTunnelPhase(gd);
    }

    public void exec(PlaceBidAction pba) {
        Player currPlayer = gd.getPlayerByCommID(pba.getCommID());
        boolean bidAdded = currPlayer.addBid(pba.getBid(), pba.getSlot());
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
}
