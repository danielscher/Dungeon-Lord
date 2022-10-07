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

    @Override
    public void gotInvalidActionFrom(final int commID) {
        if (commID == currHandledCommId) {
            gd.getServerConnection().sendActNow(commID);
        }
    }

    private void eval() {
        final BiddingSquare bs = gd.getBiddingSquare();
        // iterates over slots
        for (int col = bs.typeToColumn(BidType.GOLD); col <= bs.typeToColumn(BidType.MONSTER);
                col++) {
            for (int row = 0; row < 3; row++) {
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
                handleHireMonsterAction = true;

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
                // check if player affords slot cost
                if (player.getFood() <= 0) {
                    break;
                }
                //event order food -> select monster
                player.changeFoodBy(-1); // pays for the slot 1 food.
                broadcastFoodChanged(-1, player.getPlayerID());
                gd.getServerConnection()
                        .sendSelectMonster(commId); // tells player to select monster.
                gd.getServerConnection().sendActNow(commId);
                handleHireMonsterAction = true;

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

        if (d.getNumGoldMineAbleTiles() >= numMiningImps) {
            if (d.sendImpsToMineGold(numMiningImps)) {
                if (numMiningImps == 4) { // broadcasts change of 5 imps for slot 3
                    broadcastImpsChanged(-numMiningImps - 1, player.getPlayerID());
                    return true;
                }
                broadcastImpsChanged(-numMiningImps, player.getPlayerID());
                return true;
            }
        }
        return grantGold(player, slot - 1); // recurse to next lower slot when can't afford.
    }

    private void grantImps(final Player player, final int slot) { // READ!: first slot := 1.
        final Dungeon d = player.getDungeon();
        // determines imps to be granted by slot.
        int numImps;

        if (slot == 1) {
            numImps = 1;
        } else {
            numImps = 2;
        }

        boolean canPaidGold;
        boolean canPaidFood;

        if (slot == 3) { // for last slot check if player can afford 1 food,gold.
            canPaidFood = (player.getFood() > 0);
            canPaidGold = (player.getGold() > 0);

            // if player paid in full grant imps otherwise grant nothing.
            if (canPaidGold && canPaidFood) {
                player.changeFoodBy(-1);
                broadcastFoodChanged(-1, player.getPlayerID());
                player.changeGoldBy(-1);
                broadcastGoldChanged(-1, player.getPlayerID());
                d.addImps(numImps);
                broadcastImpsChanged(numImps, player.getPlayerID());
            }

        } else { // slots 1,2 rewards
            if (player.getFood() >= numImps) {

                player.changeFoodBy(-numImps);
                broadcastFoodChanged(-numImps, player.getPlayerID());

                d.addImps(numImps);
                broadcastImpsChanged(numImps, player.getPlayerID());
            }
        }
    }

    @Override
    public void exec(final HireMonsterAction hma) {
        if (hma.getCommID() != currHandledCommId) { // only specific player can hire
            // if players isn't registered ignore.
            if (!gd.checkIfRegistered(hma.getCommID())) {
                return;
            }
            gd.getServerConnection()
                    .sendActionFailed(hma.getCommID(), "Illegal Action: not your turn.");
        }

        final Monster chosenMonster = gd.getAndRemoveMonster(hma.getMonster());
        if (chosenMonster == null) {
            gd.getServerConnection().sendActionFailed(currHandledCommId, "monster not"
                    + " available");
            gd.getServerConnection().sendActNow(currHandledCommId);
            return;
        }

        final Player player = gd.getPlayerByCommID(hma.getCommID());

        final int monsterHunger = chosenMonster.getHunger();
        final int monsterEvilness = chosenMonster.getEvilness();

        // checks if player can afford the monster food and evilness wise.
        if (player.getFood() >= monsterHunger
                && player.getEvilLevel() + monsterEvilness <= 15) {

            player.changeFoodBy(-monsterHunger);
            if (monsterHunger != 0) {
                broadcastFoodChanged(-monsterHunger, player.getPlayerID());
            }

            player.changeEvilnessBy(monsterEvilness);
            if (monsterEvilness != 0) {
                broadcastEvilnessChanged(monsterEvilness, player.getPlayerID());
            }

            // grand monster to player.
            player.getDungeon()
                    .addMonster(chosenMonster); // FIXME : possible null value exception.
            broadcastMonsterHired(chosenMonster.getMonsterID(), player.getPlayerID());
            handleHireMonsterAction = false; // finish handling action. terminates loop.
        } else {
            gd.getServerConnection().sendActionFailed(currHandledCommId, "cannot hire this"
                    + "monster because of evilness or food");
            gd.getServerConnection().sendActNow(currHandledCommId);
        }

    }

    @Override
    public void exec(final ActivateRoomAction ara) {
        final int commId = ara.getCommID();
        // if players isn't registered ignore.
        if (!gd.checkIfRegistered(commId)) {
            return;
        }

        // get the player who requested to activate the room
        final Player player = gd.getPlayerByCommID(commId);

        final int roomId = ara.getRoomID();
        final Dungeon playersDungeon = player.getDungeon();
        if (playersDungeon.activateRoom(roomId)) {
            final Room activatedRoom = playersDungeon.getRoomById(roomId);
            broadcastImpsChanged(-activatedRoom.getActivationCost(), player.getPlayerID());
            broadcastRoomActivated(player.getPlayerID(), roomId);
        } else {
            gd.getServerConnection().sendActionFailed(commId, "couldn't activate room");
        }

        if (currHandledCommId != commId) {
            // in this case we want another action of the player
            gd.getServerConnection().sendActNow(ara.getCommID());
        }
    }

    @Override
    public void exec(final EndTurnAction eta) {
        // if players isn't registered ignore.
        if (!gd.checkIfRegistered(eta.getCommID())) {
            return;
        }

        if (eta.getCommID() != currHandledCommId) {
            gd.getServerConnection()
                    .sendActionFailed(eta.getCommID(), "Illegal Action: not your turn.");
        }
        handleHireMonsterAction = false; // finish handling action. terminates loop.

    }

    @Override
    public void exec(final LeaveAction la) {
        // if players isn't registered ignore.
        if (!gd.checkIfRegistered(la.getCommID())) {
            return;
        }
        handleHireMonsterAction = false;
        super.exec(la);
    }

}
