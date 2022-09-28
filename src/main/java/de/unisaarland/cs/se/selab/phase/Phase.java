package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActivateRoomAction;
import de.unisaarland.cs.se.selab.game.action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.action.BuildRoomAction;
import de.unisaarland.cs.se.selab.game.action.DigTunnelAction;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.HireMonsterAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.action.MonsterAction;
import de.unisaarland.cs.se.selab.game.action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.action.PlaceBidAction;
import de.unisaarland.cs.se.selab.game.action.RegAction;
import de.unisaarland.cs.se.selab.game.action.StartGameAction;
import de.unisaarland.cs.se.selab.game.action.TrapAction;
import java.util.List;
import java.util.Set;

public abstract class Phase {

    protected GameData gd;

    public Phase(GameData gd) {
        this.gd = gd;
    }

    public Phase run() throws TimeoutException {
        return null;
    }

    public void exec(RegAction x) {

    }

    public void exec(StartGameAction x) {

    }

    public void exec(LeaveAction la) {
        if (gd.checkIfRegistered(la.getCommID())) {
            gd.removePlayer(la.getCommID());
            broadcastLeft(gd.getPlayerIdByCommID(la.getCommID()));
        } else {
            gd.getServerConnection()
                    .sendActionFailed(la.getCommID(), "Player is not registered to leave");
        }

    }

    public void exec(EndTurnAction x) {

    }

    public void exec(HireMonsterAction x) {

    }

    public void exec(PlaceBidAction x) {

    }

    public void exec(MonsterAction x) {

    }

    public void exec(MonsterTargetedAction x) {

    }

    public void exec(BattleGroundAction x) {

    }

    public void exec(DigTunnelAction x) {

    }

    public void exec(ActivateRoomAction x) {

    }

    public void exec(TrapAction x) {

    }

    public void exec(BuildRoomAction x) {

    }

    public void exec(Action x) {

    }

    /*
    broadcasts "actNow" to every player
     */
    protected void broadcastActNow() {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendActNow
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n -> sc.sendActNow(gd.getCommIDByPlayerId(n)));
    }

    /*
    broadcasts "adventurerArrived" to every player
     */
    protected void broadcastAdventurerArrived(int adventurer, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerArrived
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerArrived(gd.getCommIDByPlayerId(n), adventurer, player));
    }

    /*
    broadcasts "adventurerDamaged" to every player
     */
    protected void broadcastAdventurerDamaged(int adventurer, int amount, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerDamaged
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerDamaged(gd.getCommIDByPlayerId(n), adventurer, amount));
    }

    /*
    broadcasts "adventurerDrawn" to every player
     */
    protected void broadcastAdventurerDrawn(int adventurer) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerDrawn
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerDrawn(gd.getCommIDByPlayerId(n), adventurer));
    }

    /*
    broadcasts "adventurerFled" to every player
     */
    protected void broadcastAdventurerFled(int adventurer) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerFled
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerFled(gd.getCommIDByPlayerId(n), adventurer));
    }

    /*
    broadcasts "adventurerHealed" to every player
     */
    protected void broadcastAdventurerHealed(int amount, int priest, int target) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerHealed
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerHealed(gd.getCommIDByPlayerId(n), amount, priest, target));
    }

    /*
    broadcasts "adventurerImprisoned" to every player
     */
    protected void broadcastAdventurerImprisoned(int adventurer, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendAdventurerImprisoned
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendAdventurerImprisoned(gd.getCommIDByPlayerId(n), adventurer));
    }

    /*
    broadcasts "BattleGroundSet" to every player
     */
    protected void broadcastBattleGroundSet(int player, int x, int y) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendBattleGroundSet
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendBattleGroundSet(gd.getCommIDByPlayerId(n), player, x, y));
    }

    /*
    broadcasts "BiddingStarted" to every player
     */
    protected void broadcastBiddingStarted() {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendBiddingStarted
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendBiddingStarted(gd.getCommIDByPlayerId(n)));
    }

    /*
    broadcasts "EvilnessChanged" to every player
     */
    protected void broadcastEvilnessChanged(int amount, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendEvilnessChanged
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendEvilnessChanged(gd.getCommIDByPlayerId(n), amount, player));
    }

    /*
    broadcasts "FoodChanged" to every player
     */
    protected void broadcastFoodChanged(int amount, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendFoodChanged
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendFoodChanged(gd.getCommIDByPlayerId(n), amount, player));
    }

    /*
    broadcasts "GoldChanged" to every player
     */
    protected void broadcastGoldChanged(int amount, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendGoldChanged
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendGoldChanged(gd.getCommIDByPlayerId(n), amount, player));
    }

    /*
    broadcasts "GameEnd" to every player
     */
    protected void broadcastGameEnd(int player, int points) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendGameEnd
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendGameEnd(gd.getCommIDByPlayerId(n), player, points));
    }

    /*
    broadcasts "GameStarted" to every player
     */
    protected void broadcastGameStarted() {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendGameStarted
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendGameStarted(gd.getCommIDByPlayerId(n)));
    }

    /*
    broadcasts "ImpsChanged" to every player
     */
    protected void broadcastImpsChanged(int amount, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendImpsChanged
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendImpsChanged(gd.getCommIDByPlayerId(n), amount, player));
    }

    /*
    broadcasts "Left" to every player
     */
    protected void broadcastLeft(int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendLeft
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendLeft(gd.getCommIDByPlayerId(n), player));
    }

    /*
    broadcasts "MonsterDrawn" to every player
     */
    protected void broadcastMonsterDrawn(int monster) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendMonsterDrawn
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendMonsterDrawn(gd.getCommIDByPlayerId(n), monster));
    }

    /*
    broadcasts "MonsterHired" to every player
     */
    protected void broadcastMonsterHired(int monster, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendMonsterHired
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendMonsterHired(gd.getCommIDByPlayerId(n), monster, player));
    }

    /*
    broadcasts "MonsterPlaced" to every player
    */
    protected void broadcastMonsterPlaced(int monster, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendMonsterPlaced
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendMonsterPlaced(gd.getCommIDByPlayerId(n), monster, player));
    }

    /*
    broadcasts "NextRound" to every player
     */
    protected void broadcastNextRound(int round) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendNextRound
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendNextRound(gd.getCommIDByPlayerId(n), round));
    }

    /*
    broadcasts "NextYear" to every player
     */
    protected void broadcastNextYear(int year) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendNextYear
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendNextYear(gd.getCommIDByPlayerId(n), year));
    }

    /*
    broadcasts "BidPlaced" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidPlaced(BidType bid, int player, int slot) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendBidPlaced
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendBidPlaced(gd.getCommIDByPlayerId(n), bid, player, slot));
    }

    /*
    broadcasts "BidRetrieved" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidRetrieved(BidType bid, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendBidRetrieved
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendBidRetrieved(gd.getCommIDByPlayerId(n), bid, player));
    }

    /*
    broadcasts "Player" to every player
    NOTE: needs to be called once for every registered playerID
     */
    protected void broadcastPlayer(String name, int player) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendPlayer
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendPlayer(gd.getCommIDByPlayerId(n), name, player));
    }

    /*
    broadcasts "RegistrationAborted" to every player
     */
    protected void broadcastRegistrationAborted() {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendPlayer
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendRegistrationAborted(gd.getCommIDByPlayerId(n)));
    }

    /*
    broadcasts "RoomActivated" to every player
    */
    protected void broadcastRoomActivated(int player, int room) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendRoomActivated
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendRoomActivated(gd.getCommIDByPlayerId(n), player, room));
    }

    /*
    broadcasts "RoomBuilt" to every player
    */
    protected void broadcastRoomBuilt(int player, int room, int x, int y) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendRoomBuilt
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendRoomBuilt(gd.getCommIDByPlayerId(n), player, room, x, y));
    }

    /*
    broadcasts "RoomDrawn" to every player
    */
    protected void broadcastRoomDrawn(int room) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendRoomDrawn
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendMonsterDrawn(gd.getCommIDByPlayerId(n), room));
    }

    /*
    broadcasts "TrapAcquired" to every player
    */
    protected void broadcastTrapAcquired(int player, int trap) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendTrapAcquired
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendTrapAcquired(gd.getCommIDByPlayerId(n), player, trap));
    }

    /*
    broadcasts "TrapPlaced" to every player
    */
    protected void broadcastTrapPlaced(int player, int trap) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendTrapPlaced
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendTrapPlaced(gd.getCommIDByPlayerId(n), player, trap));
    }

    /*
    broadcasts "TunnelConquered" to every player
    */
    protected void broadcastTunnelConquered(int adventurer, int x, int y) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendTunnelConquered
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendTunnelConquered(gd.getCommIDByPlayerId(n), adventurer, x, y));
    }

    /*
    broadcasts "TunnelTunnel" to every player
    */
    protected void broadcastTunnelDug(int player, int x, int y) {
        Set<Integer> commIDs = gd.getCommIDSet();
        // for each playerID, invoke sendTunnelDug
        ServerConnection<Action> sc = gd.getServerConnection();
        List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                sc.sendTunnelDug(gd.getCommIDByPlayerId(n), player, x, y));
    }


}
