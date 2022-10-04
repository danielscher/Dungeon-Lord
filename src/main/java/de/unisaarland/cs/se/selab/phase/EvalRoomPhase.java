package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.BuildRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.Comparator;
import java.util.List;

public class EvalRoomPhase extends Phase {

    ServerConnection<Action> sc = gd.getServerConnection();
    private boolean endTurn;

    public EvalRoomPhase(final GameData gd) {
        super(gd);
    }

    @Override
    public Phase run() {
        eval();
        blockAndRetrieveBids();
        returnImps();
        producedGoodsViaRoom();

        if (gd.getTime().getSeason() == 4) {
            gd.getTime().nextSeason();
            return new ChooseBattleGroundPhase(gd, gd.getAllPlayerSortedByID().get(0));
        }

        spreadAdv();
        gd.getTime().nextSeason();
        return new CollectAndPlaceBidPhase(gd);
    }

    private void eval() {
        final BiddingSquare bs = gd.getBiddingSquare();

        for (int i = 0; i < 3; i++) {
            if (bs.getIDByBidSlot(BidType.ROOM, i) != -1) {
                //if there's a valid player id in the square
                final Player p = gd.getPlayerByPlayerId(bs.getIDByBidSlot(BidType.ROOM, i));
                grantRoom(p, i);
            }
        }
    }

    private void grantRoom(final Player player, final int slot) {

        switch (slot) {
            case 0, 1:
                if (player.changeGoldBy(-1)) {
                    // player can afford bid
                    broadcastGoldChanged(-1, player.getPlayerID());
                    sc.sendPlaceRoom(player.getCommID());
                    sc.sendActNow(player.getCommID());
                    requestNextAction(player);
                }
                break;
            case 2:
                if (!gd.getCurrAvailableRooms().isEmpty()) {
                    sc.sendPlaceRoom(player.getCommID());
                    sc.sendActNow(player.getCommID());
                    requestNextAction(player);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Slot Number");
        }
    }

    private void requestNextAction(final Player player) {
        while (!endTurn) {
            try (ServerConnection<Action> sc = gd.getServerConnection()) {
                sc.nextAction().invoke(this);
            } catch (TimeoutException e) {
                kickPlayer(player.getPlayerID());
            }
        }
    }

    @Override
    public void exec(final BuildRoomAction bra) {
        final Player player = gd.getPlayerByCommID(bra.getCommID());
        if (player == null) {
            return;
        }

        Room room = null;
        for (final Room r : gd.getCurrAvailableRooms()) {
            //To find the chosen room in the list of available rooms
            if (r.getRoomID() == bra.getRoomID()) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new IllegalArgumentException("Chosen room is not available");
        }

        final Dungeon d = player.getDungeon();
        final Location loc = room.getPlacementLoc();
        if (!d.checkForFreeTilesIn(loc)) {
            sc.sendActionFailed(bra.getCommID(),
                    "You don't have any free tile to place this room on.");
        } else if (!d.placeRoom(bra.getRow(), bra.getCol(), room)) {
            sc.sendActionFailed(bra.getCommID(), "Invalid coordinates to place this room.");
        } else {
            broadcastRoomBuilt(player.getPlayerID(), bra.getRoomID(), bra.getRow(),
                    bra.getCol()); //broadcast room built
            gd.getCurrAvailableRooms().remove(room);    //remove room from options list
            endTurn = true;
        }
    }

    @Override
    public void exec(final ActivateRoomAction ara) {
        final Player player = gd.getPlayerByCommID((ara.getCommID()));

        if (player == null) { //if player's left the game
            return;
        }

        final Dungeon d = player.getDungeon();
        if (d.getRooms().isEmpty()) {
            sc.sendActionFailed(ara.getCommID(), "You don't have any rooms.");
        } else {
            if (!d.activateRoom(ara.getRoomID())) {
                sc.sendActionFailed(ara.getCommID(), "The chosen room can't be activated.");
            } else {
                final int cost = d.getRoomById(ara.getRoomID()).getActivationCost();
                broadcastImpsChanged(cost, player.getPlayerID());
                broadcastRoomActivated(player.getPlayerID(), ara.getRoomID());
            }
        }
    }

    @Override
    public void exec(final EndTurnAction eta) {
        final Player player = gd.getPlayerByCommID(eta.getCommID());
        if (player == null) { //if player's left the game
            return;
        }
        endTurn = true;
    }

    @Override
    public void exec(final LeaveAction la) {
        endTurn = true; // to prevent any further placing room request from this user
        super.exec(la);
    }

    public void blockAndRetrieveBids() {
        for (final Player player : gd.getAllPlayerSortedByID()) {
            for (final BidType bid : player.getBlockedBids()) {
                broadcastBidRetrieved(bid, player.getPlayerID());
            }
            player.blockBids();
            player.clearCurrBids();
        }
    }

    public void returnImps() {
        for (final Player player : gd.getAllPlayerSortedByID()) {
            final int p = player.getPlayerID();
            final int goldMined = player.getDungeon().getGoldMiningImps();

            if (player.getDungeon().returnImpsFromDigging() > 0) {
                broadcastImpsChanged(player.getDungeon().returnImpsFromDigging(), p);
            }
            if (goldMined > 0) {
                player.changeGoldBy(goldMined);
                broadcastGoldChanged(goldMined, p);
            }
        }
    }

    public void producedGoodsViaRoom() {
        for (final Player player : gd.getAllPlayerSortedByID()) {
            final Dungeon dungeon = player.getDungeon();
            if (!dungeon.getActiveRooms().isEmpty()) {
                final int p = player.getPlayerID();

                if (dungeon.returnImpsFromRoom() > 0) {
                    broadcastImpsChanged(dungeon.returnImpsFromRoom(), p);
                }

                for (final Room r : dungeon.getActiveRooms()) {
                    evalRoomProduction(r, player);
                }
            }
        }
    }


    public void evalRoomProduction(final Room r, final Player player) {
        final int p = player.getPlayerID();
        final Dungeon dungeon = player.getDungeon();

        if (r.getFoodProduction() > 0) {
            player.changeFoodBy(r.getFoodProduction());
            broadcastFoodChanged(r.getFoodProduction(), p);
        }
        if (r.getNiceness() > 0) {
            player.changeEvilnessBy(-r.getNiceness());
            broadcastEvilnessChanged(-r.getNiceness(), p);
        }
        if (r.getGoldProduction() > 0) {
            player.changeGoldBy(r.getGoldProduction());
            broadcastGoldChanged(r.getGoldProduction(), p);
        }
        if (r.getImpProduction() > 0) {
            dungeon.addImps(r.getImpProduction());
            broadcastImpsChanged(r.getImpProduction(), p);
        }
    }

    public void spreadAdv() {
        final List<Player> playersSortByEvilness = gd.getAllPlayerSortedByID();

        playersSortByEvilness.sort(
                Comparator.comparing(Player::getEvilLevel).thenComparing(Player::getPlayerID));
        //Sort Players by Evil Level then Player IDs

        gd.getCurrAvailableAdventurers().sort(Comparator.comparing(Adventurer::getDifficulty)
                .thenComparing(Adventurer::getAdventurerID));
        //Sort Adventurers by difficulty then IDs

        for (final Player p : playersSortByEvilness) {
            //add an adventurer to the corresponding player's dungeon
            final Adventurer adv = gd.getCurrAvailableAdventurers().remove(0);
            p.getDungeon().insertAdventurer(adv);
            broadcastAdventurerArrived(adv.getAdventurerID(), p.getPlayerID());
        }
    }
}
