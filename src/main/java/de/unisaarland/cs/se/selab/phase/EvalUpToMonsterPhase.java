package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.ArrayList;

public class EvalUpToMonsterPhase extends Phase {

    //Evaluation for the Gold, Imp, Trap and monster bids.

    public EvalUpToMonsterPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //retrieve Player ids of bid winners for corresponding bid type.
        ArrayList<Integer> goldWinners = collectBidWinners(BidType.GOLD);
        ArrayList<Integer> impWinners = collectBidWinners(BidType.IMPS);
        ArrayList<Integer> trapWinners = collectBidWinners(BidType.TRAP);
        ArrayList<Integer> monsterWinners = collectBidWinners(BidType.MONSTER);

        ServerConnection<Action> sc = gd.getServerConnection();

        return new EvalRoomPhase(super.gd);
    }

    private void eval() {
        //TODO
        //iterate over BiddingSquare
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
                grantTrap();
                break;

            case MONSTER:
                grantMonster();
                break;
            default:
                break;
        }
    }

    private void grantMonster() {
        //TODO: Implement this.
    }


    private void grantTrap() {
        //TODO: Implement this.
    }

    // will grant the bid reward corresponding to the slot of the GOLD option.
    // if player can't afford the slot reward will recurse to the lower bid slot.
    private boolean grantGold(Player player, int slot) {
        if (slot < 1 || slot > 3) {
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


    public void exec(HireMonsterAction hma) {
        //TODO
    }

    public void exec(ActivateRoomAction ara) {
        //TODO
    }

    public void exec(EndTurnAction eta) {
        //TODO
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
