package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.HireMonsterAction;

public class EvalUpToMonsterPhase extends Phase {

    //Evaluation for the Gold, Imp, Trap and monster bids.

    public EvalUpToMonsterPhase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //retrieve Player ids of bid winners for corresponding bid type.
        int[] goldWinners = collectBidWinners(BidType.GOLD);
        int[] impWinners = collectBidWinners(BidType.IMPS);
        int[] trapWinners = collectBidWinners(BidType.TRAP);
        int[] monsterWinners = collectBidWinners(BidType.MONSTER);

        ServerConnection<Action> sc = gd.getServerConnection();

        return new EvalRoomPhase(super.gd);
    }

    private void eval() {
        //TODO
        //iterate over BiddingSquare
    }

    /*private void grant(Player player, BidType bidtype, int slot){
        Dungeon d = player.getDungeon();
        switch (bidtype) {
            case GOLD:
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                    break;
                }
            case IMPS: // IMP
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                    break;
                }
            case TRAP:
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                    break;
                }
            case MONSTER:
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                    break;
                }
        }
    }*/

    private void exec(HireMonsterAction hma) {
        //TODO
    }

    private void exec(ActivateRoomAction ara) {
        //TODO
    }

    private void exec(EndTurnAction eta) {
        //TODO
    }

    //returns list of player ids for a specific bid type.
    private int[] collectBidWinners(BidType bt) {
        BiddingSquare bs = gd.getBiddingSquare();
        int[] ids = new int[3];
        for (int i = 0; i < 3; i++) {
            ids[i] = bs.getIDByBidSlot(bt, i);
        }
        return ids; //collect the playerID
    }
}
