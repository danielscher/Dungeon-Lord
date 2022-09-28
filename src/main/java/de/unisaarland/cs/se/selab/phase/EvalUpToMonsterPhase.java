package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.ArrayList;

public class EvalUpToMonsterPhase extends Phase {

    private int currHandledHireMonsterCommId = -1; // commId of the player being handled.
    private boolean handleHireMonsterAction = true; // flag if hire monster action is being handled.
    //Evaluation for the Gold, Imp, Trap and monster bids.

    public EvalUpToMonsterPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        eval();
        return new EvalRoomPhase(super.gd);
    }

    private void eval() {
        BiddingSquare bs = gd.getBiddingSquare();
        // iterates over slots
        for (int row = 0; row < 3; row++) {
            for (int col = bs.typeToColumn(BidType.GOLD); col <= bs.typeToColumn(BidType.MONSTER);
                    col++) {
                Player player = gd.getPlayerByPlayerId(bs.getIDByBidSlot(row, col));
                grant(player, bs.columnToType(col), row + 1); // slot = row + 1.
            }
        }
    }

    private void grant(Player player, BidType bidtype, int slot) {

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

    private void grantMonster(Player player, int slot) {
        ServerConnection<Action> sc = gd.getServerConnection();

        int commId = player.getCommID();

        switch (slot) {
            case 1, 2: {
                sc.sendSelectMonster(commId); // tells player to select monster.
                sc.sendActNow(commId);

                // will ask for next action until receives End Turn or Hire Monster action.
                while (handleHireMonsterAction) {

                    currHandledHireMonsterCommId = commId;

                    try { // get action from player.
                        Action action = sc.nextAction();
                        action.invoke(this);

                    } catch (Exception e) {
                        //TODO: add behaviour
                    }
                }
                break;
            }
            case 3: {
                if (player.getFood() > 0) {
                    player.changeFoodBy(-1); // pays for the slot 1 food.
                    broadcastFoodChanged(-1, player.getPlayerID());

                    try { // get action from player.
                        Action action = sc.nextAction();
                        action.invoke(this);

                    } catch (Exception e) {
                        //TODO: add behaviour
                    }
                }
                break;
            }
            default:
                break;
        }
    }


    private void grantTrap(Player player, int slot) {
        if (slot < 1 || slot > 3) { // ensures termination of recursion.
            return;
        }
        Dungeon d = player.getDungeon();

        // bid rewards per case
        switch (slot) {
            case 1: { // pay 1 gold get 1 trap.
                gd.addDrawnTraps(slot); // adds traps to the currently available.
                if (player.getGold() > 0) {
                    player.changeGoldBy(-1);
                    broadcastGoldChanged(-1, player.getPlayerID());
                    Trap trap = gd.getOneCurrAvailableTrap();
                    d.addTrap(trap); // grants player one Trap
                    broadcastTrapAcquired(player.getPlayerID(), trap.getTrapID());
                    break;
                }
                break;
            }
            case 2: { // get trap for free.
                gd.addDrawnTraps(slot); // adds traps to the currently available.
                Trap trap = gd.getOneCurrAvailableTrap();
                d.addTrap(trap); // grants player one Trap
                broadcastTrapAcquired(player.getPlayerID(), trap.getTrapID());
                break;
            }
            case 3: { // pay 2 gold get 2 traps.
                if (player.getGold() > 1) {
                    gd.addDrawnTraps(slot); // adds traps to the currently available.
                    player.changeGoldBy(-2);
                    broadcastGoldChanged(-2, player.getPlayerID());
                    Trap trap1 = gd.getOneCurrAvailableTrap();
                    Trap trap2 = gd.getOneCurrAvailableTrap();
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
    private boolean grantGold(Player player, int slot) {
        if (slot < 0 || slot > 3) { // slot 0 is allowed for sending out 1 imp to mine gold.
            return false;
        } // ensures termination of recursion.
        Dungeon d = player.getDungeon();
        int numMiningImps = slot + 1; // #imps to be sent mining

        // checks how many tiles will be worked.
        int tilesNeeded = slot < 3 ? numMiningImps : numMiningImps - 1; // for supervisor -1 tile.

        //checks if player can afford i.e. enough tiles/imps
        if (d.getNumGoldMineAbleTiles() == tilesNeeded) {
            if (d.sendImpsToMineGold(numMiningImps)) {
                broadcastImpsChanged(numMiningImps, player.getPlayerID());
                return true;
            }
        }
        return grantGold(player, slot - 1); // recurse to next lower slot when can't afford.
    }

    private boolean grantImps(Player player, int slot) { // READ!: first slot := 1.
        if (slot < 1 || slot > 3) {
            return false;
        } // ensures termination of recursion.
        Dungeon d = player.getDungeon();
        // determines imps to be granted by slot.
        int numImps = slot == 3 ? slot - 1 : slot;

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
    public void exec(HireMonsterAction hma) {
        ServerConnection<Action> sc = gd.getServerConnection();
        Player player = gd.getPlayerByCommID(hma.getCommID());
        if (hma.getCommID() != currHandledHireMonsterCommId) { // only specific player can hire
            sc.sendActionFailed(hma.getCommID(), "Illegal Action: not your turn.");
        }
        Monster chosenMonster = gd.getCurrAvailableMonster(hma.getMonster());
        int monsterHunger = chosenMonster.getHunger();
        int monsterEvilness = chosenMonster.getEvilness();

        // checks if player can afford the monster food and evilness wise.
        if (player.getFood() >= monsterHunger
                && player.getEvilLevel() + monsterEvilness <= 15) {

            player.changeFoodBy(-monsterHunger);
            broadcastFoodChanged(-monsterHunger, player.getPlayerID());

            player.changeEvilnessBy(-monsterEvilness);
            broadcastEvilnessChanged(-monsterEvilness, player.getPlayerID());

            // grand monster to player.
            player.getDungeon().addMonster(chosenMonster);// FIXME : possible null value exception.
            broadcastMonsterHired(chosenMonster.getMonsterID(), player.getPlayerID());
            handleHireMonsterAction = false; // finish handling action. terminates loop.
        }
    }

    @Override
    public void exec(ActivateRoomAction ara) {
        //TODO
    }

    @Override
    public void exec(EndTurnAction eta) {

        ServerConnection<Action> sc = gd.getServerConnection();
        int commId = eta.getCommID();
        if (eta.getCommID() != currHandledHireMonsterCommId) {
            sc.sendActionFailed(eta.getCommID(), "Illegal Action: not your turn.");
        }
        handleHireMonsterAction = false; // finish handling action. terminates loop.
    }


    //returns list of player ids for a specific bid type.
    private ArrayList<Integer> collectBidWinners(BidType bt) {
        BiddingSquare bs = gd.getBiddingSquare();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            ids.add(bs.getIDByBidSlot(bt, i));
        }
        return ids; //collect the playerID
    }
}
