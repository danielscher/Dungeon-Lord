package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.DigTunnelAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class EvalUpToTunnelPhase extends Phase {

    List<Integer> commIdsToDigTunnel = new ArrayList<Integer>();
    private boolean gotEndTurn = false;
    private boolean handledTunnelAction = false;
    private Coordinate lastDugTile = null;

    public EvalUpToTunnelPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        eval();
        return new EvalUpToTunnelPhase(gd);
    }

    /*
    iterates over bidding square and grants the bids
     */
    private void eval() {
        BiddingSquare biddingSquare = gd.getBiddingSquare();
        for (int i = 0; i < 3; i++) {
            // for BidTypes from FOOD to TUNNEL
            for (int j = 0; j < 3; j++) {
                // for slots 1 to 3 (0 to 2)
                if (biddingSquare.getIDByBidSlot(i, j) != -1) {
                    // if there is a valid player id in the square
                    Player player = gd.getPlayerByPlayerId(biddingSquare.getIDByBidSlot(i, j));
                    grant(player, i, j);
                }
            }
        }
    }

    /*
    this method is used to switch over BidTypes and call the specific
    granting method
     */
    private void grant(Player player, int bidType, int slot) {
        if (player == null) {
            // if player isn't in the game anymore, do not try to grant
            return;  // exists this method
        }

        switch (bidType) {
            case 0:
                // FOOD bids
                grantFood(player, slot);
                break;
            case 1:
                // NICENESS bids
                grantNiceness(player, slot);
                break;
            case 2:
                // TUNNEL BIDS
                grantTunnel(player, slot);
                break;
            default:
                // this case should not be entered, since we do not evaluate other BidTypes
                break;
        }
    }

    /*
    this method grants food bids
     */
    private void grantFood(Player player, int slot) {
        // TODO change
        switch (slot) {
            case 0:
                // first food slot
                if (player.changeGoldBy(-1)) {
                    // player can afford bid
                    player.changeFoodBy(2);
                    broadcastGoldChanged(-1, player.getPlayerID());
                    broadcastFoodChanged(2, player.getPlayerID());
                }
                break;
            case 1:
                // second food slot
                if (player.changeEvilnessBy(1)) {
                    // player can afford bid
                    player.changeFoodBy(3);
                    broadcastEvilnessChanged(1, player.getPlayerID());
                    broadcastFoodChanged(3, player.getPlayerID());
                }
                break;
            case 2:
                // third food slot
                if (player.changeEvilnessBy(2)) {
                    // player can afford bid
                    player.changeFoodBy(3);
                    player.changeGoldBy(1);
                    broadcastEvilnessChanged(2, player.getPlayerID());
                    broadcastFoodChanged(3, player.getPlayerID());
                    broadcastGoldChanged(1, player.getPlayerID());
                }
                break;
            default:
                // this case should never be entered
                break;
        }
    }

    /*
    this method grants niceness bids
     */
    private void grantNiceness(Player player, int slot) {
        switch (slot) {
            case 0:
                // first niceness slot
                player.changeEvilnessBy(-1);
                broadcastEvilnessChanged(-1, player.getPlayerID());
                break;
            case 1:
                // second niceness slot
                player.changeEvilnessBy(-2);
                broadcastEvilnessChanged(-2, player.getPlayerID());
                break;
            case 2:
                // third niceness slot
                if (player.changeGoldBy(-1)) {
                    // player can afford bid
                    player.changeEvilnessBy(-2);
                    broadcastGoldChanged(-1, player.getPlayerID());
                    broadcastEvilnessChanged(-2, player.getPlayerID());
                }
                break;
            default:
                // this case should never be entered
                break;
        }
    }

    /*
    this method grants tunnel bids
     */
    private void grantTunnel(Player player, int slot) {
        Dungeon dungeon = player.getDungeon();
        ServerConnection<Action> serverConn = gd.getServerConnection();
        int commId = player.getCommID();
        boolean maxImpUsage = false;

        int impsToMine = slot + 2;  // this follows from the bidding square specification

        // special check if enough imps for 3rd option
        if (impsToMine == 4 && dungeon.getRestingImps() < 5) {
            impsToMine--;
        }

        // reduce impsToMine until player has enough
        while (impsToMine > dungeon.getRestingImps()) {
            impsToMine--;
        }

        // if player has no imps at all, cancel granting
        if (impsToMine == 0) {
            return;
        }

        // if player can do the 3rd option, set flag so last tunnel digging will deduct two imps
        if (impsToMine == 4) {
            maxImpUsage = true;
        }

        // now ask for tunnel digging actions until all imps are sent out or player ends turn
        for (; impsToMine > 0; impsToMine--) {
            serverConn.sendDigTunnel(commId); // send DigTunnel once for every possible tile

            // if player activated room it might be the case that he hasn't enough imps anymore
            if (impsToMine > dungeon.getRestingImps()) {
                continue; // go to next iteration to reduce impsToMine
            }

            // this if statement prevents getting the next tunnel action when the
            // player hasn't enough imps anymore for the 4th mining imp
            // due to activating rooms or something else
            if (dungeon.getTunnelDiggingImps() == 3 && dungeon.getRestingImps() < 2) {
                break;
            }
            // add player's commId to a list of expected action-senders
            commIdsToDigTunnel.add(commId);

            serverConn.sendActNow(commId); // TODO check if sending once is sufficient
            while (commIdsToDigTunnel.size() > 0) {
                // loop until the player we evaluate has sent an action
                try {
                    Action action = serverConn.nextAction();
                    action.invoke(this);
                } catch (Exception e) {
                    // TODO implement behaviour???
                }
            }

            // if player has sent endTurn, do not ask for further tunnel actions
            if (gotEndTurn) {
                gotEndTurn = false;
                break;  // breaks loop of asking for inputs
            }

            // if we successfully handled the TunnelAction, change imp amount
            if (handledTunnelAction) {
                handledTunnelAction = false; // reset flag for next iteration

                if (maxImpUsage && impsToMine == 2) {
                    // this is the case of the last tile to mine on the 3rd bid slot
                    dungeon.sendImpsToDigTunnel(2); // deduct imps
                    broadcastImpsChanged(-2, player.getPlayerID()); // broadcast imp change
                    int xpos = lastDugTile.getxpos();
                    int ypos = lastDugTile.getypos();
                    broadcastTunnelDug(player.getPlayerID(), xpos, ypos); // broadcast tunnel dug
                    lastDugTile = null;
                    break;  // don't continue with impsToMine == 1
                } else {
                    // in this case it is either not the last tunnel tile or not the 3rd slot
                    dungeon.sendImpsToDigTunnel(1);
                    broadcastImpsChanged(-1, player.getPlayerID());
                    int xpos = lastDugTile.getxpos();
                    int ypos = lastDugTile.getypos();
                    broadcastTunnelDug(player.getPlayerID(), xpos, ypos);
                    lastDugTile = null;
                }
            } else {
                // if the received action was something else, repeat this for loop
                impsToMine++;  // this makes the loop to repeat with same index
            }
        }
    }

    /*
    this methods handles the receiving of a tunnel digging action
     */
    public void exec(DigTunnelAction dta) {
        ServerConnection<Action> serverConn = gd.getServerConnection();
        // get all information from Action object
        int commId = dta.getCommID();
        int[] coordsArr = dta.getCoords();
        Coordinate requestedPos = new Coordinate(coordsArr[0], coordsArr[1]);

        // get the player who requested to activate the room
        Player player = gd.getPlayerByCommID(commId);
        // if player doesn't exist, return
        if (player == null) {
            serverConn.sendActionFailed(commId, "you don't seem to be a registered player");
            return;
        }

        Dungeon playersDungeon = player.getDungeon();

        if (!commIdsToDigTunnel.contains(commId)) {
            // wrong player sent the event
            serverConn.sendActionFailed(commId, "it's not you're turn to dig tunnels");
        } else {
            // the right player has sent the event
            commIdsToDigTunnel.remove(commId); // remove this player from the list
            if (playersDungeon.dig(requestedPos.getxpos(), requestedPos.getypos())) {
                // in this case the tunnel was successfully dug
                handledTunnelAction = true; // set flag that tunnel was successfully dug
                lastDugTile = requestedPos; // tell eval what tile was dug
            } else {
                // in this case the tunnel couldn't be dug
                serverConn.sendActionFailed(commId, "cannot dig here");
            }
        }

    }

    /*
    this method handles room activations
     */
    public void exec(ActivateRoomAction ara) {
        int commId = ara.getCommID();
        int roomId = ara.getRoomID();
        ServerConnection<Action> serverConn = gd.getServerConnection();

        // get the player who requested to activate the room
        Player player = gd.getPlayerByCommID(commId);
        // if player doesn't exist, return
        if (player == null) {
            serverConn.sendActionFailed(commId, "you don't seem to be a registered player");
            return;
        }

        Dungeon playersDungeon = player.getDungeon();
        if(playersDungeon.activateRoom(roomId)) {
            Room activatedRoom = playersDungeon.getRoomById(roomId);
            broadcastImpsChanged(activatedRoom.getActivationCost(), player.getPlayerID());
            broadcastRoomActivated(player.getPlayerID(), roomId);
        } else {
            serverConn.sendActionFailed(commId, "couldn't activate room");
        }

    }

    public void exec(EndTurnAction eta) {
        ServerConnection<Action> serverConn = gd.getServerConnection();
        int commId = eta.getCommID();
        if (commIdsToDigTunnel.contains(commId)) {
            // in this case the player to dig tunnel ends his turn
            gotEndTurn = true;
            commIdsToDigTunnel.remove(commId);
        } else {
            serverConn.sendActionFailed(commId, "cannot end turn, it's not your turn");
        }
    }

    @Override
    public void exec(LeaveAction la) {
        int commId = la.getCommID();
        if(commIdsToDigTunnel.contains(commId)) {
            commIdsToDigTunnel.remove(commId);
        }
        gotEndTurn = true; // to prevent any further tunnel dig asking from this user
        super.exec(la);
    }

}
