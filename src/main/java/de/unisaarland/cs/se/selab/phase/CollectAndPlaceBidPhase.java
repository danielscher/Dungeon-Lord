package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.Set;

public class CollectAndPlaceBidPhase extends Phase {


    public CollectAndPlaceBidPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        ServerConnection<Action> sc = gd.getServerConnection();
        Set<Integer> commIDs = gd.getCommIDSet();

        broadcastActNow();

        while (!checkIfAllBidsChosen()) {
            sc.nextAction().invoke(this);
        }

        for (int i = 0; i < 3; i++) {
            //the sequence of inserting bid on bidding square: go through players
            // to get their first bids, insert, then 2nd bids of players, insert
            for (Integer commID : commIDs) {
                Player p = gd.getPlayerByCommID(commID);
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
        ServerConnection<Action> sc = gd.getServerConnection();
        Player currPlayer = gd.getPlayerByCommID(pba.getCommID());
        boolean bidAdded = currPlayer.addBid(pba.getBid(), pba.getSlot());
        if (!bidAdded) {
            sc.sendActionFailed(pba.getCommID(),
                                    "can't choose bid " + pba.getBid().toString());
        }
    }

    public void exec(ActivateRoomAction ara) {
        Player player = gd.getPlayerByCommID((ara.getCommID()));
        ServerConnection<Action> sc = gd.getServerConnection();

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
        Set<Integer> commIDs = gd.getCommIDSet();
        for (Integer commID : commIDs) {
            Player p = gd.getPlayerByCommID(commID);
            if (p.getNumPlacedBids() != 3) {
                return false;
            }
        }
        return true;
    }
}
