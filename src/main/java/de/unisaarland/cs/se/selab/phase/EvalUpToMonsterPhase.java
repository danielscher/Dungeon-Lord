package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.Action.Action;
import de.unisaarland.cs.se.selab.game.Action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.Action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.Action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.comm.BidType;

public class EvalUpToMonsterPhase extends Phase{

    //Evaluation for the Gold, Imp, Trap and monster bids.

    public EvalUpToMonsterPhase(GameData gd) {
        super(gd);
    }

    public Phase run(){
        //retrieve Player ids of bid winners for corresponding bid type.
        int[] goldWinners = collectBidWinners(BidType.GOLD);
        int[] impWinners = collectBidWinners(BidType.IMPS);
        int[] trapWinners = collectBidWinners(BidType.TRAP);
        int[] monsterWinners = collectBidWinners(BidType.MONSTER);

        ServerConnection<Action> sc = gd.getServerConnection();


        return new EvalRoomPhase(super.gd);
    }

    private void eval(){
        //TODO
        //iterate over BiddingSquare
    }

    private void grant(Player player, int bidtype, int slot){
        Dungeon d = player.getDungeon();
        switch (bidtype) {
            case 3: // GOLD
                switch (slot){
                    case 0:
                        d.sendImpsToMineGold(2);
                    case 1:
                    case 2:
                }
            case 4: // IMP
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                }
            case 5: // TRAP
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                }
            case 6: // MONSTER
                switch (slot){
                    case 0:
                    case 1:
                    case 2:
                }
        }
    }

    public void exec(HireMonsterAction hma){
        //TODO
    }

    public void exec(ActivateRoomAction ara){
        //TODO
    }

    public void exec(EndTurnAction eta){
        //TODO
    }

    //returns list of player ids for a specific bid type.
    private int[] collectBidWinners(BidType bt){
        BiddingSquare bs = gd.getBiddingSquare();
        int [] ids = new int[3];
        for (int i = 0; i < 3; i++){
            ids[i] = bs.getIDByBidSlot(bt,i);
        }
        return ids; //collect the playerID
    }
}
