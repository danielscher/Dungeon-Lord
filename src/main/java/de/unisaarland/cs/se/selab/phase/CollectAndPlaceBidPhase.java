package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.List;

public class CollectAndPlaceBidPhase extends Phase {

    ServerConnection<Action> sc = gd.getServerConnection();

    public CollectAndPlaceBidPhase(final GameData gd) {
        super(gd);
    }

    private void broadcastAdventurersMonstersAndRooms() {
        if (!gd.getCurrAvailableAdventurers().isEmpty()) {
            for (final Adventurer adventurer : gd.getCurrAvailableAdventurers()) {
                broadcastAdventurerDrawn(adventurer.getAdventurerID());
            }
        }

        if (!gd.getCurrAvailableMonsters().isEmpty()) {
            for (final Monster monster : gd.getCurrAvailableMonsters()) {
                broadcastMonsterDrawn(monster.getMonsterID());
            }
        }

        if (!gd.getCurrAvailableRooms().isEmpty()) {
            for (final Room room : gd.getCurrAvailableRooms()) {
                broadcastRoomDrawn(room.getRoomID());
            }
        }
    }

    @Override
    public Phase run() {
        if (gd.getTime().getSeason() == 1) {
            broadcastNextYear(gd.getTime().getYear());
        }
        broadcastNextRound(gd.getTime().getSeason());

        gd.drawEntities();

        broadcastAdventurersMonstersAndRooms();

        broadcastBiddingStarted();
        broadcastActNow();

        while (!checkIfAllBidsChosen()) {
            try {
                gd.getServerConnection().nextAction().invoke(this);
            } catch (TimeoutException e) {
                return null; // aborts the game like stated in the specification
            }
        }

        if (gd.getNumCurrPlayers() == 0) {
            return null;
        }

        for (int i = 0; i < 3; i++) {   //go through priorities to insert bids into biddingsquare
            goThruPlayers(i);
        }

        return new EvalUpToTunnelPhase(gd);
    }

    @Override
    public void exec(final PlaceBidAction pba) {
        final Player player = gd.getPlayerByCommID(pba.getCommID());

        if (player == null) { //if player's left the game
            return;
        }
        final boolean bidAdded = player.addBid(pba.getBid(), pba.getSlot());
        if (!bidAdded) {
            sc.sendActionFailed(pba.getCommID(),
                    "can't choose bid " + pba.getBid().toString());
        }
    }

    @Override
    public void exec(final ActivateRoomAction ara) {
        final Player player = gd.getPlayerByCommID((ara.getCommID()));

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
                final int cost = player.getDungeon()
                        .getRoomById(ara.getRoomID()).getActivationCost();
                broadcastImpsChanged(cost, player.getPlayerID());
                broadcastRoomActivated(player.getPlayerID(), ara.getRoomID());
            }
        }
    }

    private boolean checkIfAllBidsChosen() {
        for (final Player p : gd.getAllPlayerSortedByID()) {
            if (p.getNumPlacedBids() != 3) {
                return false;
            }
        }
        return true;
    }

    //go through players to get their 1st priority, insert
    //then 2nd, insert
    private void goThruPlayers(final int priority) {
        final BiddingSquare bs = gd.getBiddingSquare();
        final List<Player> players = gd.getAllPlayerSortedByID();

        for (final Player p : players) {
            if (p.getPlayerID() >= gd.getFirstBidder()) {
                //loop through the bidders right of the curr firstbidder
                final BidType bid = p.getBid(priority);
                bs.insert(bid, p.getPlayerID());
                broadcastBidPlaced(bid, p.getPlayerID(), priority + 1);
            }
        }
        for (final Player p : players) {
            if (p.getPlayerID() < gd.getFirstBidder()) {
                //loop through the bidders left of the curr firstbidder
                final BidType bid = p.getBid(priority);
                bs.insert(bid, p.getPlayerID());
                broadcastBidPlaced(bid, p.getPlayerID(), priority + 1);
            }
        }
        gd.setFirstBidder();
    }
}


