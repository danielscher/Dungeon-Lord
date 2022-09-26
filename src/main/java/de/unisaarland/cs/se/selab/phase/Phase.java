package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.Action.*;
import java.util.Set;

public abstract class Phase {

    protected GameData gd;

    public Phase(GameData gd) {
        this.gd = gd;
    }

    public Phase run() throws TimeoutException {
        //return NULL;
        return null;
    }

    private void exec(RegAction x) {

    }

    private void exec(StartGameAction x) {

    }

    private void exec(LeaveAction x) {

    }

    private void exec(EndTurnAction x) {

    }

    private void exec(HireMonsterAction x) {

    }

    private void exec(PlaceBidAction x) {

    }

    private void exec(MonsterAction x) {

    }

    private void exec(MonsterTargetedAction x) {

    }

    private void exec(BattleGroundAction x) {

    }

    private void exec(DigTunnelAction x) {

    }

    private void exec(ActivateRoomAction x) {

    }

    private void exec(TrapAction x) {

    }

    private void exec(BuildRoomAction x) {

    }

    private void exec(Action x) {

    }

    /*
    broadcasts "actNow" to every player
     */
    protected void broadcastActNow() {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendActNow
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendActNow(commID);
        }
    }

    /*
    broadcasts "adventurerArrived" to every player
     */
    protected void broadcastAdventurerArrived(int adventurer, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerArrived
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerArrived(commID, adventurer, player);
        }
    }

    /*
    broadcasts "adventurerDamaged" to every player
     */
    protected void broadcastAdventurerDamaged(int adventurer, int amount, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerDamaged
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerDamaged(commID, adventurer, amount);
        }
    }

    /*
    broadcasts "adventurerDrawn" to every player
     */
    protected void broadcastAdventurerDrawn(int adventurer) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerDrawn
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerDrawn(commID, adventurer);
        }
    }

    /*
    broadcasts "adventurerFled" to every player
     */
    protected void broadcastAdventurerFled(int adventurer) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerFled
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerFled(commID, adventurer);
        }
    }

    /*
    broadcasts "adventurerHealed" to every player
     */
    protected void broadcastAdventurerHealed(int amount, int priest, int target) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerHealed
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerHealed(commID, amount, priest, target);
        }
    }

    /*
    broadcasts "adventurerImprisoned" to every player
     */
    protected void broadcastAdventurerImprisoned(int adventurer, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendAdventurerImprisoned
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendAdventurerImprisoned(commID, adventurer);
        }
    }

    /*
    broadcasts "BattleGroundSet" to every player
     */
    protected void broadcastBattleGroundSet(int player, int x, int y) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendBattleGroundSet
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendBattleGroundSet(commID, player, x, y);
        }
    }

    /*
    broadcasts "BiddingStarted" to every player
     */
    protected void broadcastBiddingStarted() {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendBiddingStarted
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendBiddingStarted(commID);
        }
    }

    /*
    broadcasts "EvilnessChanged" to every player
     */
    protected void broadcastEvilnessChanged(int amount, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendEvilnessChanged
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendEvilnessChanged(commID, amount, player);
        }
    }

    /*
    broadcasts "FoodChanged" to every player
     */
    protected void broadcastFoodChanged(int amount, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendFoodChanged
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendFoodChanged(commID, amount, player);
        }
    }

    /*
    broadcasts "GoldChanged" to every player
     */
    protected void broadcastGoldChanged(int amount, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendGoldChanged
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendGoldChanged(commID, amount, player);
        }
    }

    /*
    broadcasts "GameEnd" to every player
     */
    protected void broadcastGameEnd(int player, int points) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendGameEnd
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendGameEnd(commID, player, points);
        }
    }

    /*
    broadcasts "GameStarted" to every player
     */
    protected void broadcastGameStarted() {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendGameStarted
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendGameStarted(commID);
        }
    }

    /*
    broadcasts "ImpsChanged" to every player
     */
    protected void broadcastImpsChanged(int amount, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendImpsChanged
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendImpsChanged(commID, amount, player);
        }
    }

    /*
    broadcasts "Left" to every player
     */
    protected void broadcastLeft(int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendLeft
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendLeft(commID, player);
        }
    }

    /*
    broadcasts "MonsterDrawn" to every player
     */
    protected void broadcastMonsterDrawn(int monster) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendMonsterDrawn
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendMonsterDrawn(commID, monster);
        }
    }

    /*
    broadcasts "MonsterHired" to every player
     */
    protected void broadcastMonsterHired(int monster, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendMonsterHired
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendMonsterHired(commID, monster, player);
        }
    }

    /*
    broadcasts "MonsterPlaced" to every player
    */
    protected void broadcastMonsterPlaced(int monster, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendMonsterPlaced
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendMonsterPlaced(commID, monster, player);
        }
    }

    /*
    broadcasts "NextRound" to every player
     */
    protected void broadcastNextRound(int round) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendNextRound
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendNextRound(commID, round);
        }
    }

    /*
    broadcasts "NextYear" to every player
     */
    protected void broadcastNextYear(int year) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendNextYear
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendNextYear(commID, year);
        }
    }

    /*
    broadcasts "BidPlaced" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidPlaced(BidType bid, int player, int slot) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendBidPlaced
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendBidPlaced(commID, bid, player, slot);
        }
    }

    /*
    broadcasts "BidRetrieved" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidRetrieved(BidType bid, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendBidRetrieved
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendBidRetrieved(commID, bid, player);
        }
    }

    /*
    broadcasts "Player" to every player
    NOTE: needs to be called once for every registered playerID
     */
    protected void broadcastPlayer(String name, int player) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendPlayer
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendPlayer(commID, name, player);
        }
    }

    /*
    broadcasts "RegistrationAborted" to every player
     */
    protected void broadcastRegistrationAborted() {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendPlayer
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendRegistrationAborted(commID);
        }
    }

    /*
    broadcasts "RoomActivated" to every player
    */
    protected void broadcastRoomActivated(int player, int room) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendRoomActivated
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendRoomActivated(commID, player, room);
        }
    }

    /*
    broadcasts "RoomBuilt" to every player
    */
    protected void broadcastRoomBuilt(int player, int room, int x, int y) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendRoomBuilt
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendRoomBuilt(commID, player, room, x, y);
        }
    }

    /*
    broadcasts "RoomDrawn" to every player
    */
    protected void broadcastRoomDrawn(int room) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendRoomDrawn
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendRoomDrawn(commID, room);
        }
    }

    /*
    broadcasts "TrapAcquired" to every player
    */
    protected void broadcastTrapAcquired(int player, int trap) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendTrapAcquired
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendTrapAcquired(commID, player, trap);
        }
    }

    /*
    broadcasts "TrapPlaced" to every player
    */
    protected void broadcastTrapPlaced(int player, int trap) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendTrapPlaced
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendTrapPlaced(commID, player, trap);
        }
    }

    /*
    broadcasts "TunnelConquered" to every player
    */
    protected void broadcastTunnelConquered(int adventurer, int x, int y) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendTunnelConquered
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendTunnelConquered(commID, adventurer, x, y);
        }
    }

    /*
    broadcasts "TunnelTunnel" to every player
    */
    protected void broadcastTunnelDug(int player, int x, int y) {

        Set<Integer> commIDs = gd.getCommIDSet();
        // for each commID, invoke sendTunnelDug
        for (Integer commID : commIDs) {
            gd.getServerConnection().sendTunnelDug(commID, player, x, y);
        }
    }


}
