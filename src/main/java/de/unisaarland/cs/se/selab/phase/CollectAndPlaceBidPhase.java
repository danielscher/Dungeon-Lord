package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.Action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.Action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;

public class CollectAndPlaceBidPhase extends Phase{

    private int[] allPlayerCommID;
    private Player[] allPlayers;
    private BiddingSquare bs;

    public CollectAndPlaceBidPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {

        for (Player p : allPlayers) {
            PlaceBidAction pba = (PlaceBidAction) gd.getServerConnection().nextAction();
            exec(pba);
        }

        if (!checkIfAllBidsChosen()) {
            throw new IllegalStateException("All bids have to be chosen");
        } else {
            blockBids();                                                                            //block 2nd and 3rd bids, release old blocked bids

            for (int i = 0; i < 3; i++) {                                                           //the sequence of inserting bid on bidding square: go through players
                for(Player p : allPlayers) {                                                        // to get their first bids, insert, then 2nd bids of players, insert
                    boolean inserted = bs.insert(p.getBid(i), p.getPlayerID());
                    if (!inserted) {
                        throw new IllegalStateException("Slot occupied");
                    } else {
                        gd.getServerConnection().sendBidPlaced(p.getCommID(), p.getBid(i), p.getPlayerID(), i);
                    }
                }
            }
        }
        return new EvalUpToTunnelPhase(gd);
    }

    private void eval(){
        //TODO
        //iterates over Bidding Square
    }

    private void grant(Player p,int r, int c){
        //TODO
        //grants a single bid
    }

    private void exec(PlaceBidAction pba){
        boolean bidAdded = false;
        Player currPlayer = gd.getPlayerByCommID(pba.getCommID());
        bidAdded = currPlayer.addBid(pba.getBid(), pba.getSlot());
        if (!bidAdded){
            gd.getServerConnection().sendActionFailed(currPlayer.getCommID(), "can't choose this bid");
        }
    }

    private void exec(ActivateRoomAction ara){
        //TODO
    }

    private boolean checkIfAllBidsChosen(){
        //to_do
        return false;
    }

    private void blockBids(){
        //to_to
    }
}
