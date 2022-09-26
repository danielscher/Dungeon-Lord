package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.Action.*;
import de.unisaarland.cs.se.selab.game.BiddingSquare;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;

public class CollectAndPlaceBidPhase extends Phase{

    private ServerConnection sc;
    private int[] allPlayerCommID;
    private Player[] allPlayers;
    private BiddingSquare bs;

    public CollectAndPlaceBidPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        for(Player p: allPlayers){
            //TODO: PlaceBidAction pba = (PlaceBidAction) sc.nextAction(); exec(pba);
        }

        if (!checkIfAllBidsChosen()) {
            throw new IllegalStateException("All bids have to be chosen");
        } else {
            blockBids();

            //for(Player p: allPlayers){
                //TODO: the sequence of inserting bid on bidding square: go through players, to get their first bids, insert, then 2nd bids of players, insert?
            //BidType bid = p.getBid()
            //bs.insert()
                    //TODO: what if insert return false?
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
