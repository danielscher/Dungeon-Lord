package de.unisaarland.cs.se.selab.phase;

import static de.unisaarland.cs.se.selab.comm.BidType.ROOM;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.BuildRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.ArrayList;
import java.util.List;

public class EvalRoomPhase extends Phase {

    List<Integer> commIdsToPlaceRoom = new ArrayList<Integer>();
    private boolean endTurn = false;
    ServerConnection<Action> sc = gd.getServerConnection();


    public EvalRoomPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        eval();

        gd.getTime().nextSeason();
        List<Integer> playerIds = gd.getAllPlayerID().stream().sorted().toList();
        return new ChooseBattleGroundPhase(gd, gd.getPlayerByPlayerId(playerIds.get(0)));
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
            sc.sendActionFailed(bra.getCommID(), "Invalid coordinates to place this room.");
        } else {
            broadcastRoomBuilt(player.getPlayerID(), bra.getRoomID(), bra.getRow(),
                    bra.getCol()); //broadcast room built
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
                sc.sendActionFailed(ara.getCommID(), "The chosen room can't be activated.");
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

    public void blockRetrieveBids() {

    }

    private int[] collectBidWinners() {
        //TODO
        return new int[0]; //collect the playerID
    }
}
