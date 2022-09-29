package de.unisaarland.cs.se.selab.phase;

import static de.unisaarland.cs.se.selab.comm.BidType.ROOM;
import static de.unisaarland.cs.se.selab.comm.BidType.TUNNEL;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EvalRoomPhase extends Phase {

    private boolean endTurn = false;
    ServerConnection<Action> sc = gd.getServerConnection();
    BiddingSquare bs = gd.getBiddingSquare();


    public EvalRoomPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        eval();
        blockAndRetrieveBids();
        returnImps();
        getProducedGoods();

        if (gd.getTime().getSeason() == 4) {
            gd.getTime().nextSeason();
            for (Player p : gd.getAllPlayerSortedByID()) {
                //for all players, sorted by player ID, to choose battleground
                return new ChooseBattleGroundPhase(gd, p);
            }
        }

        spreadAdv();
        gd.getTime().nextSeason();
        return new CollectAndPlaceBidPhase(gd);
    }

    private void eval() throws TimeoutException {
        BiddingSquare bs = gd.getBiddingSquare();

        for (int i = 0; i < 3; i++) {
            if (bs.getIDByBidSlot(ROOM, i) != -1) {
                //if there's a valid player id in the square
                Player p = gd.getPlayerByPlayerId(bs.getIDByBidSlot(ROOM, i));
                grantRoom(p, i);
            }
        }
    }

    private void grantRoom(Player player, int slot) throws TimeoutException {

        switch (slot) {
            case 0:
            case 1:
                if (player.changeGoldBy(-1)) {
                    // player can afford bid
                    broadcastGoldChanged(-1, player.getPlayerID());
                    sc.sendPlaceRoom(player.getCommID());
                    sc.sendActNow(player.getCommID());
                    while (!endTurn) {
                        sc.nextAction().invoke(this);
                    }
                }
                break;
            case 2:
                if (!gd.getCurrAvailableRooms().isEmpty()) {
                    sc.sendPlaceRoom(player.getCommID());
                    sc.sendActNow(player.getCommID());
                    while (!endTurn) {
                        sc.nextAction().invoke(this);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid Slot Number");
        }
    }

    public void exec(BuildRoomAction bra) {
        Player player = gd.getPlayerByCommID(bra.getCommID());
        if (player == null) {
            return;
        }

        Room room = null;
        for (Room r : gd.getCurrAvailableRooms()) {
            //To find the chosen room in the list of available rooms
            if (r.getRoomID() == bra.getRoomID()) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new IllegalArgumentException("Chosen room is not available");
        }

        Dungeon d = player.getDungeon();
        Location loc = room.getPlacementLoc();
        if (!d.checkForFreeTilesIn(loc)) {
            sc.sendActionFailed(bra.getCommID(),
                    "You don't have any free tile to place this room on.");
        } else if (!d.placeRoom(bra.getRow(), bra.getCol(), room)) {
            sc.sendActionFailed(bra.getCommID(),
                    "Invalid coordinates to place this room.");
        } else {
            broadcastRoomBuilt(player.getPlayerID(),
                    bra.getRoomID(), bra.getRow(), bra.getCol()); //broadcast room built
            gd.getCurrAvailableRooms().remove(room);    //remove room from options list
            endTurn = true;
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

    public void exec(EndTurnAction eta) {
        endTurn = true;
    }

    @Override
    public void exec(LeaveAction la) {
        endTurn = true; // to prevent any further placing room request from this user
        super.exec(la);
    }

    public void blockAndRetrieveBids() {
        for (Player player : gd.getAllPlayerSortedByID()) {
            for (BidType bid : player.getBlockedBids()) {
                broadcastBidRetrieved(bid, player.getPlayerID());
            }
            player.blockBids();
            player.clearCurrBids();
        }
    }

    public void returnImps() {
        for (Player player : gd.getAllPlayerSortedByID()){
            int p = player.getPlayerID();

            if (player.getDungeon().returnImpsFromDigging() > 0){
                broadcastImpsChanged(player.getDungeon().returnImpsFromDigging(), p);
            }
            if (player.getDungeon().getGoldMiningImps() > 0){
                broadcastGoldChanged(player.getDungeon().getGoldMiningImps(), p);
                player.getDungeon().clearGoldMiningImps();
            }
        }
    }

    public void getProducedGoods() {
        for (Player player : gd.getAllPlayerSortedByID()) {
            if (!player.getDungeon().getActiveRooms().isEmpty()) {
                int p = player.getPlayerID();

                if (player.getDungeon().returnImpsFromRoom() > 0) {
                    broadcastImpsChanged(player.getDungeon().returnImpsFromRoom(), p);
                }

                for (Room r : player.getDungeon().getActiveRooms()) {
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
                        player.getDungeon().addImps(r.getImpProduction());
                        broadcastImpsChanged(r.getImpProduction(), p);
                    }
                }
            }
        }
    }

    public void spreadAdv() {
        List<Player> playersSortByEvilness = gd.getAllPlayerSortedByID();

        /*Collections.sort(playersSortByEvilness, new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                return p1.getEvilLevel() - p2.getEvilLevel();
            }
        });*/

        Collections.sort(playersSortByEvilness,
                Comparator.comparing(Player::getEvilLevel).thenComparing(Player::getPlayerID));
        //Sort Players by Evil Level then Player IDs

        Collections.sort(gd.getCurrAvailableAdventurers(),
                Comparator.comparing(Adventurer::getDifficulty)
                                .thenComparing(Adventurer::getAdventurerID));
        //Sort Adventurers by difficulty then IDs

        for (int i = 0; i < playersSortByEvilness.size(); i++) {
            //add an adventurer to the corresponding player's dungeon
            Player p = playersSortByEvilness.get(i);
            Adventurer adv = gd.getCurrAvailableAdventurers().get(i);
            p.getDungeon().insertAdventurer(adv);
            broadcastAdventurerArrived(adv.getAdventurerID(), p.getPlayerID());
        }

        gd.clearAdventurers();  //clear the adventurer list
    }
}
