package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;

public class EvalUpToMonsterPhase extends Phase {

    // commId of the player being handled for which the HireMonster Action
    // is being currently handled.
    private int currHandledCommId = -1;

    // flag if hire monster action is currently being handled.
    private boolean handleHireMonsterAction = true;


    //Evaluation for the Gold, Imp, Trap and monster bids.
    public EvalUpToMonsterPhase(final GameData gd) {
        super(gd);
    }

    @Override
    public Phase run() {
        eval();
        return new EvalRoomPhase(super.gd);
    }

    private void eval() {
        final BiddingSquare bs = gd.getBiddingSquare();
        // iterates over slots
        for (int row = 0; row < 3; row++) {
            for (int col = bs.typeToColumn(BidType.GOLD); col <= bs.typeToColumn(BidType.MONSTER);
                    col++) {
                final Player player = gd.getPlayerByPlayerId(bs.getIDByBidSlot(row, col));
                if (player != null) {
                    grant(player, bs.columnToType(col), row + 1); // slot = row + 1.
                }

            }
        }
    }

    private void grant(final Player player, final BidType bidtype, final int slot) {

        switch (bidtype) {
            case GOLD:
                grantGold(player, slot);
                break;

            case IMPS:
                grantImps(player, slot);
                break;

            case TRAP:
                grantTrap(player, slot);
                break;

            case MONSTER:
                grantMonster(player, slot);
                break;
            default:
                break;
        }
    }

    private void grantMonster(final Player player, final int slot) {

        final int commId = player.getCommID();

        switch (slot) {
            case 1, 2: {
                gd.getServerConnection()
                        .sendSelectMonster(commId); // tells player to select monster.
                gd.getServerConnection().sendActNow(commId);

                // will ask for next action until receives End Turn or Hire Monster action.
                while (handleHireMonsterAction) {

                    currHandledCommId = commId;

                    try { // get action from player.
                        final Action action = gd.getServerConnection().nextAction();
                        action.invoke(this);

                    } catch (TimeoutException e) {
                        //TODO: add behaviour
                        kickPlayer(player.getPlayerID());

                    }
                }
                break;
            }
            case 3: {
                if (player.getFood() > 0) {
                    player.changeFoodBy(-1); // pays for the slot 1 food.
                    broadcastFoodChanged(-1, player.getPlayerID());

                    try { // get action from player.
                        final Action action = gd.getServerConnection().nextAction();
                        action.invoke(this);

                    } catch (TimeoutException e) {
                        kickPlayer(player.getPlayerID()); // kicks player (leave).
                    }
                }
                break;
            }
            default:
                break;
        }

    }


    private void grantTrap(final Player player, final int slot) {
        if (slot < 1 || slot > 3) { // ensures termination of recursion.
            return;
        }
        final Dungeon d = player.getDungeon();

        // bid rewards per case
        switch (slot) {
            case 1: { // pay 1 gold get 1 trap.
                gd.addDrawnTraps(slot); // adds traps to the currently available.
                if (player.getGold() > 0) {
                    player.changeGoldBy(-1);
                    broadcastGoldChanged(-1, player.getPlayerID());
                    final Trap trap = gd.getOneCurrAvailableTrap();
                    d.addTrap(trap); // grants player one Trap
                    broadcastTrapAcquired(player.getPlayerID(), trap.getTrapID());
                    break;
                }
                break;
            }
            case 2: { // get trap for free.
                gd.addDrawnTraps(slot); // adds traps to the currently available.
                final Trap trap = gd.getOneCurrAvailableTrap();
                d.addTrap(trap); // grants player one Trap
                broadcastTrapAcquired(player.getPlayerID(), trap.getTrapID());
                break;
            }
            case 3: { // pay 2 gold get 2 traps.
                if (player.getGold() > 1) {
                    gd.addDrawnTraps(slot); // adds traps to the currently available.
                    player.changeGoldBy(-2);
                    broadcastGoldChanged(-2, player.getPlayerID());
                    final Trap trap1 = gd.getOneCurrAvailableTrap();
                    final Trap trap2 = gd.getOneCurrAvailableTrap();
                    d.addTrap(trap1); // grants player one Trap.
                    d.addTrap(trap2); // grants second Trap.
                    broadcastTrapAcquired(player.getPlayerID(), trap1.getTrapID());
                    broadcastTrapAcquired(player.getPlayerID(), trap2.getTrapID());
                    break;
                }
                break;
            }
            default:
                break;
        }
    }

    // will grant the bid reward corresponding to the slot of the GOLD option.
    // if player can't afford the slot reward will recurse to the lower bid slot.
    private boolean grantGold(final Player player, final int slot) {
        if (slot < 0 || slot > 3) { // slot 0 is allowed for sending out 1 imp to mine gold.
            return false;
        } // ensures termination of recursion.
        final Dungeon d = player.getDungeon();
        final int numMiningImps = slot + 1; // #imps to be sent mining

        // checks how many tiles will be worked.
        final int tilesNeeded =
                slot < 3 ? numMiningImps : numMiningImps - 1; // for supervisor -1 tile.

        //checks if player can afford i.e. enough tiles/imps
        if (d.getNumGoldMineAbleTiles() == tilesNeeded) {
            if (d.sendImpsToMineGold(numMiningImps)) {
                broadcastImpsChanged(numMiningImps, player.getPlayerID());
                return true;
            }
        }
        return grantGold(player, slot - 1); // recurse to next lower slot when can't afford.
    }

    private boolean grantImps(final Player player, final int slot) { // READ!: first slot := 1.
        if (slot < 1 || slot > 3) {
            return false;
        } // ensures termination of recursion.
        final Dungeon d = player.getDungeon();
        // determines imps to be granted by slot.
        final int numImps = slot == 3 ? slot - 1 : slot;

        if (slot == 3) { // for last slot check if player can afford 1 food,gold.
            if ((player.getFood() > 0) && (player.getGold() > 0)) {
                //change food/gold and broadcast changes
                player.changeFoodBy(-1);
                broadcastFoodChanged(-1, player.getPlayerID());

                player.changeGoldBy(-1);
                broadcastGoldChanged(-1, player.getPlayerID());

                d.addImps(numImps);
                broadcastImpsChanged(numImps, player.getPlayerID());
                return true;
            } else {
                return false;
            }
        } else { // slots 1,2 rewards
            if (player.getFood() >= numImps) {

                player.changeFoodBy(-numImps);
                broadcastFoodChanged(-numImps, player.getPlayerID());

                d.addImps(numImps);
                broadcastImpsChanged(numImps, player.getPlayerID());
                return true;
            }
        }
        return grantImps(player, slot - 1); // recurse to next lower slot when can't afford.
    }

    @Override
    public void exec(final HireMonsterAction hma) {
        final Player player = gd.getPlayerByCommID(hma.getCommID());
        if (hma.getCommID() != currHandledCommId) { // only specific player can hire
            gd.getServerConnection()
                    .sendActionFailed(hma.getCommID(), "Illegal Action: not your turn.");
        }
        final Monster chosenMonster = gd.getAndRemoveMonster(hma.getMonster());
        final int monsterHunger = chosenMonster.getHunger();
        final int monsterEvilness = chosenMonster.getEvilness();

        // checks if player can afford the monster food and evilness wise.
        if (player.getFood() >= monsterHunger
                && player.getEvilLevel() + monsterEvilness <= 15) {

            player.changeFoodBy(-monsterHunger);
            broadcastFoodChanged(-monsterHunger, player.getPlayerID());

            player.changeEvilnessBy(-monsterEvilness);
            broadcastEvilnessChanged(-monsterEvilness, player.getPlayerID());

            // grand monster to player.
            player.getDungeon()
                    .addMonster(chosenMonster); // FIXME : possible null value exception.
            broadcastMonsterHired(chosenMonster.getMonsterID(), player.getPlayerID());
            handleHireMonsterAction = false; // finish handling action. terminates loop.
        }

    }

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

        final int roomId = ara.getRoomID();
        final Dungeon playersDungeon = player.getDungeon();
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

        if (eta.getCommID() != currHandledCommId) {
            gd.getServerConnection()
                    .sendActionFailed(eta.getCommID(), "Illegal Action: not your turn.");
        }
        handleHireMonsterAction = false; // finish handling action. terminates loop.

    }

    @Override
    public void exec(final LeaveAction la) {
        handleHireMonsterAction = false;
        super.exec(la);
    }

}
