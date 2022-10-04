package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
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

    private final List<Integer> commIdsToDigTunnel = new ArrayList<>();
    private boolean gotEndTurn;
    private boolean handledTunnelAction;
    private Coordinate lastDugTile;

    public EvalUpToTunnelPhase(final GameData gd) {
        super(gd);
    }

    @Override
    public Phase run() {
        eval();

        if (gd.getAllPlayerID().isEmpty()) {
            return null;
        }
        return new EvalUpToMonsterPhase(gd);
    }

    /*
    iterates over bidding square and grants the bids
     */
    private void eval() {
        final BiddingSquare biddingSquare = gd.getBiddingSquare();
        for (int i = 0; i < 3; i++) {
            // for BidTypes from FOOD to TUNNEL
            for (int j = 0; j < 3; j++) {
                // for slots 1 to 3 (0 to 2)
                if (biddingSquare.getIDByBidSlot(i, j) != -1) {
                    // if there is a valid player id in the square
                    final Player player = gd.getPlayerByPlayerId(
                            biddingSquare.getIDByBidSlot(i, j));
                    grant(player, i, j);
                }
            }
        }
    }

    /*
    this method is used to switch over BidTypes and call the specific
    granting method
     */
    private void grant(final Player player, final int bidType, final int slot) {
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
    private void grantFood(final Player player, final int slot) {
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
    private void grantNiceness(final Player player, final int slot) {
        // TODO change events because maybe the full amount wasnt granted
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
    private void grantTunnel(final Player player, final int slot) {
        final Dungeon dungeon = player.getDungeon();
        final int commId = player.getCommID();
        boolean maxImpUsage = false;

        int impsToMine = calcMaxImpUsage(dungeon.getNumImps(), slot);

        // if player can do the 3rd option, set flag so last tunnel digging will deduct two imps
        if (impsToMine == 4) {
            maxImpUsage = true;
        }

        // now ask for tunnel digging actions until all imps are sent out or player ends turn
        while (impsToMine > 0) {
            impsToMine = getInputs(impsToMine, commId, player, dungeon, maxImpUsage);
        }
    }

    /*
    helper method for grantTunnel to calculate the maximum imp usage the player
    is capable of
     */
    private int calcMaxImpUsage(final int availImps, final int slot) {
        int res = slot + 2; // this follows from the bidding square specification

        // special check if enough imps for 3rd option
        if (res == 4 && availImps < 5) {
            res--;
        }

        // reduce impsToMine until player has enough
        while (res > availImps) {
            res--;
        }
        return res;
    }

    /*
    helper method which handles the getting and further processing of user inputs
    return -1 means "break" loop
    return == impsToMine means "continue"
     */
    private int getInputs(final int impsToMine, final int commId, final Player player,
            final Dungeon dungeon, final boolean maxImpUsage) {
        gd.getServerConnection()
                .sendDigTunnel(commId); // send DigTunnel once for every possible tile
        // if player activated room it might be the case that he hasn't enough imps anymore
        if (impsToMine > dungeon.getRestingImps()) {
            return impsToMine; // go to next iteration to reduce impsToMine
        }

        // this if statement prevents getting the next tunnel action when the
        // player hasn't enough imps anymore for the 4th mining imp
        // due to activating rooms or something else
        if (dungeon.getTunnelDiggingImps() == 3 && dungeon.getRestingImps() < 2) {
            return -1;
        }

        // get a new action from this player
        requestActionFrom(commId);

        // if player has sent endTurn, do not ask for further tunnel actions
        if (gotEndTurn) {
            gotEndTurn = false;
            return -1;
        }

        // if we successfully handled the TunnelAction, change imp amount
        if (handledTunnelAction) {
            handledTunnelAction = false; // reset flag for next iteration

            if (maxImpUsage && impsToMine == 2) {
                // this is the case of the last tile to mine on the 3rd bid slot
                dungeon.sendImpsToDigTunnel(2); // deduct imps
                broadcastImpsChanged(-2, player.getPlayerID()); // broadcast imp change
                final int xpos = lastDugTile.getxpos();
                final int ypos = lastDugTile.getypos();
                broadcastTunnelDug(player.getPlayerID(), xpos, ypos); // broadcast tunnel dug
                lastDugTile = null;
                return -1;  // don't continue with impsToMine == 1
            } else {
                // in this case it is either not the last tunnel tile or not the 3rd slot
                dungeon.sendImpsToDigTunnel(1);
                broadcastImpsChanged(-1, player.getPlayerID());
                final int xpos = lastDugTile.getxpos();
                final int ypos = lastDugTile.getypos();
                broadcastTunnelDug(player.getPlayerID(), xpos, ypos);
                lastDugTile = null;
            }
        } else {
            // if the received action was something else, repeat this for loop
            return impsToMine;  // this makes the loop to repeat with same index
        }
        return impsToMine - 1;
    }

    /*
    helper method to ask for an Action object from a player of a given commID
     */
    private void requestActionFrom(final int commId) {
        // add player's commId to a list of expected action-senders
        commIdsToDigTunnel.add(commId);

        gd.getServerConnection().sendActNow(commId); // TODO check if sending once is sufficient

        while (!commIdsToDigTunnel.isEmpty()) {
            // loop until the player we evaluate has sent an action
            try {
                final Action action = gd.getServerConnection().nextAction();
                action.invoke(this);
            } catch (TimeoutException e) {
                // TODO implement behaviour???
                kickPlayer(gd.getPlayerIdByCommID(commIdsToDigTunnel.get(0)));
                commIdsToDigTunnel.clear();
            }
        }
    }

    /*
    this method handles the receiving of a tunnel digging action
     */
    @Override
    public void exec(final DigTunnelAction dta) {
        // get all information from Action object
        final int commId = dta.getCommID();

        // get the player who requested to activate the room
        final Player player = gd.getPlayerByCommID(commId);
        // if player doesn't exist, return
        if (player == null) {
            gd.getServerConnection()
                    .sendActionFailed(commId, "you don't seem to be a registered player");
            return;
        }

        final Dungeon playersDungeon = player.getDungeon();

        if (!commIdsToDigTunnel.contains(commId)) {
            // wrong player sent the event
            gd.getServerConnection().sendActionFailed(commId, "it's not your turn to dig tunnels");
        } else {
            final int[] coordsArr = dta.getCoords();
            final Coordinate requestedPos = new Coordinate(coordsArr[0], coordsArr[1]);
            // the right player has sent the event
            commIdsToDigTunnel.remove(commId); // remove this player from the list
            if (playersDungeon.dig(requestedPos.getxpos(), requestedPos.getypos())) {
                // in this case the tunnel was successfully dug
                handledTunnelAction = true; // set flag that tunnel was successfully dug
                lastDugTile = requestedPos; // tell eval what tile was dug
            } else {
                // in this case the tunnel couldn't be dug
                gd.getServerConnection().sendActionFailed(commId, "cannot dig here");
            }
        }

    }

    /*
    this method handles room activations
     */
    @Override
    public void exec(final ActivateRoomAction ara) {
        final int commId = ara.getCommID();

        // get the player who requested to activate the room
        final Player player = gd.getPlayerByCommID(commId);
        // if player doesn't exist, return
        if (player == null) {
            gd.getServerConnection()
                    .sendActionFailed(commId, "you don't seem to be a registered player");
            return;
        }

        final Dungeon playersDungeon = player.getDungeon();
        final int roomId = ara.getRoomID();
        if (playersDungeon.activateRoom(roomId)) {
            final Room activatedRoom = playersDungeon.getRoomById(roomId);
            broadcastImpsChanged(activatedRoom.getActivationCost(), player.getPlayerID());
            broadcastRoomActivated(player.getPlayerID(), roomId);
        } else {
            gd.getServerConnection().sendActionFailed(commId, "couldn't activate room");
        }

    }

    @Override
    public void exec(final EndTurnAction eta) {
        final int commId = eta.getCommID();
        if (commIdsToDigTunnel.contains(commId)) {
            // in this case the player to dig tunnel ends his turn
            gotEndTurn = true;
            commIdsToDigTunnel.remove(commId);
        } else {
            gd.getServerConnection()
                    .sendActionFailed(commId, "cannot end turn, it's not your turn");
        }
    }

    @Override
    public void exec(final LeaveAction la) {
        final int commId = la.getCommID();
        if (commIdsToDigTunnel.contains(commId)) {
            commIdsToDigTunnel.remove((Object) commId); // casting to Object needed for overloading
        }
        gotEndTurn = true; // to prevent any further tunnel dig asking from this user
        super.exec(la);
    }

}
