package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.BidType;
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

public abstract class Phase {

    protected GameData gd;

    public Phase(final GameData gd) {
        this.gd = gd;
    }

    public abstract Phase run();

    /*
    sends action failed to commId
    to be used in the default implementation of most exec methods
     */
    public void sendError(final int commId) {
        gd.getServerConnection()
                .sendActionFailed(commId, "this action isn't valid within this phase");
    }

    public void exec(final RegAction x) {
        sendError(x.getCommID());
    }

    public void exec(final StartGameAction x) {
        sendError(x.getCommID());
    }

    public void exec(final LeaveAction la) {
        if (gd.checkIfRegistered(la.getCommID())) {
            gd.removePlayer(la.getCommID());
            broadcastLeft(gd.getPlayerIdByCommID(la.getCommID()));
        } else {
            gd.getServerConnection()
                    .sendActionFailed(la.getCommID(), "Player is not registered to leave");
        }

    }

    public void exec(final EndTurnAction x) {
        sendError(x.getCommID());
    }

    public void exec(final HireMonsterAction x) {
        sendError(x.getCommID());
    }

    public void exec(final PlaceBidAction x) {
        sendError(x.getCommID());
    }

    public void exec(final MonsterAction x) {
        sendError(x.getCommID());
    }

    public void exec(final MonsterTargetedAction x) {
        sendError(x.getCommID());
    }

    public void exec(final BattleGroundAction x) {
        sendError(x.getCommID());
    }

    public void exec(final DigTunnelAction x) {
        sendError(x.getCommID());
    }

    public void exec(final ActivateRoomAction x) {
        sendError(x.getCommID());
    }

    public void exec(final TrapAction x) {
        sendError(x.getCommID());
    }

    public void exec(final BuildRoomAction x) {
        sendError(x.getCommID());
    }

    public void exec(final Action x) {
        sendError(x.getCommID());
    }

    /*
    broadcasts "actNow" to every player
     */
    protected void broadcastActNow() {
        // for each playerID, invoke sendActNow
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted()
                .forEach(n -> gd.getServerConnection().sendActNow(gd.getCommIDByPlayerId(n)));
    }


    /*
    broadcasts "adventurerArrived" to every player
     */
    protected void broadcastAdventurerArrived(final int adventurer, final int player) {
        // for each playerID, invoke sendAdventurerArrived
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendAdventurerArrived(gd.getCommIDByPlayerId(n), adventurer, player));
    }

    /*
    broadcasts "adventurerDamaged" to every player
     */
    protected void broadcastAdventurerDamaged(final int adventurer, final int amount) {
        // for each playerID, invoke sendAdventurerDamaged
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendAdventurerDamaged(gd.getCommIDByPlayerId(n), adventurer, amount));


    }

    /*
    broadcasts "adventurerDrawn" to every player
     */
    protected void broadcastAdventurerDrawn(final int adventurer) {
        // for each playerID, invoke sendAdventurerDrawn
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendAdventurerDrawn(gd.getCommIDByPlayerId(n), adventurer));

    }

    /*
    broadcasts "adventurerFled" to every player
     */
    protected void broadcastAdventurerFled(final int adventurer) {
        // for each playerID, invoke sendAdventurerFled
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted()
                .forEach(n -> gd.getServerConnection()
                        .sendAdventurerFled(gd.getCommIDByPlayerId(n), adventurer));
    }


    /*
    broadcasts "adventurerHealed" to every player
     */
    protected void broadcastAdventurerHealed(final int amount, final int priest, final int target) {
        // for each playerID, invoke sendAdventurerHealed
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendAdventurerHealed(gd.getCommIDByPlayerId(n), amount, priest, target));

    }

    /*
    broadcasts "adventurerImprisoned" to every player
     */
    protected void broadcastAdventurerImprisoned(final int adventurer) {
        // for each playerID, invoke sendAdventurerImprisoned
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendAdventurerImprisoned(gd.getCommIDByPlayerId(n), adventurer));

    }

    /*
    broadcasts "BattleGroundSet" to every player
     */
    protected void broadcastBattleGroundSet(final int player, final int x, final int y) {
        // for each playerID, invoke sendBattleGroundSet
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendBattleGroundSet(gd.getCommIDByPlayerId(n), player, x, y));

    }

    /*
    broadcasts "BiddingStarted" to every player
     */
    protected void broadcastBiddingStarted() {
        // for each playerID, invoke sendBiddingStarted
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendBiddingStarted(gd.getCommIDByPlayerId(n)));

    }

    /*
    broadcasts "EvilnessChanged" to every player
     */
    protected void broadcastEvilnessChanged(final int amount, final int player) {
        // for each playerID, invoke sendEvilnessChanged
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendEvilnessChanged(gd.getCommIDByPlayerId(n), amount, player));

    }

    /*
    broadcasts "FoodChanged" to every player
     */
    protected void broadcastFoodChanged(final int amount, final int player) {
        // for each playerID, invoke sendFoodChanged
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendFoodChanged(gd.getCommIDByPlayerId(n), amount, player));

    }

    /*
    broadcasts "GoldChanged" to every player
     */
    protected void broadcastGoldChanged(final int amount, final int player) {
        // for each playerID, invoke sendGoldChanged
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendGoldChanged(gd.getCommIDByPlayerId(n), amount, player));

    }

    /*
    broadcasts "GameEnd" to every player
     */
    protected void broadcastGameEnd(final int player, final int points) {
        // for each playerID, invoke sendGameEnd
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendGameEnd(gd.getCommIDByPlayerId(n), player, points));

    }

    /*
    broadcasts "GameStarted" to every player
     */
    protected void broadcastGameStarted() {
        // for each playerID, invoke sendGameStarted
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendGameStarted(gd.getCommIDByPlayerId(n)));

    }

    /*
    broadcasts "ImpsChanged" to every player
     */
    protected void broadcastImpsChanged(final int amount, final int player) {
        // for each playerID, invoke sendImpsChanged
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendImpsChanged(gd.getCommIDByPlayerId(n), amount, player));

    }

    /*
    broadcasts "Left" to every player
     */
    protected void broadcastLeft(final int player) {
        // for each playerID, invoke sendLeft
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendLeft(gd.getCommIDByPlayerId(n), player));

    }

    /*
    broadcasts "MonsterDrawn" to every player
     */
    protected void broadcastMonsterDrawn(final int monster) {
        // for each playerID, invoke sendMonsterDrawn
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendMonsterDrawn(gd.getCommIDByPlayerId(n), monster));

    }

    /*
    broadcasts "MonsterHired" to every player
     */
    protected void broadcastMonsterHired(final int monster, final int player) {
        // for each playerID, invoke sendMonsterHired
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendMonsterHired(gd.getCommIDByPlayerId(n), monster, player));

    }

    /*
    broadcasts "MonsterPlaced" to every player
    */
    protected void broadcastMonsterPlaced(final int monster, final int player) {
        // for each playerID, invoke sendMonsterPlaced
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendMonsterPlaced(gd.getCommIDByPlayerId(n), monster, player));

    }

    /*
    broadcasts "NextRound" to every player
     */
    protected void broadcastNextRound(final int round) {
        // for each playerID, invoke sendNextRound
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendNextRound(gd.getCommIDByPlayerId(n), round));

    }

    /*
    broadcasts "NextYear" to every player
     */
    protected void broadcastNextYear(final int year) {
        // for each playerID, invoke sendNextYear
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendNextYear(gd.getCommIDByPlayerId(n), year));

    }

    /*
    broadcasts "BidPlaced" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidPlaced(final BidType bid, final int player, final int slot) {
        // for each playerID, invoke sendBidPlaced
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendBidPlaced(gd.getCommIDByPlayerId(n), bid, player, slot));

    }

    /*
    broadcasts "BidRetrieved" to every player
    NOTE: this BidType is the provided one, not our BidType!!!
     */
    protected void broadcastBidRetrieved(final BidType bid, final int player) {
        // for each playerID, invoke sendBidRetrieved
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendBidRetrieved(gd.getCommIDByPlayerId(n), bid, player));

    }

    /*
    broadcasts "Player" to every player
    NOTE: needs to be called once for every registered playerID
     */
    protected void broadcastPlayer(final String name, final int player) {
        // for each playerID, invoke sendPlayer
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendPlayer(gd.getCommIDByPlayerId(n), name, player));

    }

    /*
    broadcasts "RegistrationAborted" to every player
     */
    protected void broadcastRegistrationAborted() {
        // for each playerID, invoke sendPlayer
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendRegistrationAborted(gd.getCommIDByPlayerId(n)));

    }

    /*
    broadcasts "RoomActivated" to every player
    */
    protected void broadcastRoomActivated(final int player, final int room) {
        // for each playerID, invoke sendRoomActivated
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendRoomActivated(gd.getCommIDByPlayerId(n), player, room));

    }

    /*
    broadcasts "RoomBuilt" to every player
    */
    protected void broadcastRoomBuilt(final int player, final int room, final int x, final int y) {
        // for each playerID, invoke sendRoomBuilt
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendRoomBuilt(gd.getCommIDByPlayerId(n), player, room, x, y));

    }

    /*
    broadcasts "RoomDrawn" to every player
    */
    protected void broadcastRoomDrawn(final int room) {
        // for each playerID, invoke sendRoomDrawn
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendMonsterDrawn(gd.getCommIDByPlayerId(n), room));

    }

    /*
    broadcasts "TrapAcquired" to every player
    */
    protected void broadcastTrapAcquired(final int player, final int trap) {
        // for each playerID, invoke sendTrapAcquired
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendTrapAcquired(gd.getCommIDByPlayerId(n), player, trap));

    }

    /*
    broadcasts "TrapPlaced" to every player
    */
    protected void broadcastTrapPlaced(final int player, final int trap) {
        // for each playerID, invoke sendTrapPlaced
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendTrapPlaced(gd.getCommIDByPlayerId(n), player, trap));

    }

    /*
    broadcasts "TunnelConquered" to every player
    */
    protected void broadcastTunnelConquered(final int adventurer, final int x, final int y) {
        // for each playerID, invoke sendTunnelConquered
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection()
                        .sendTunnelConquered(gd.getCommIDByPlayerId(n), adventurer, x, y));

    }

    /*
    broadcasts "TunnelTunnel" to every player
    */
    protected void broadcastTunnelDug(final int player, final int x, final int y) {
        // for each playerID, invoke sendTunnelDug
        final List<Integer> playerIDs = gd.getAllPlayerID();
        playerIDs.stream().sorted().forEach(n ->
                gd.getServerConnection().sendTunnelDug(gd.getCommIDByPlayerId(n), player, x, y));

    }

    protected void kickPlayer(final int playerId) {
        final LeaveAction la = new LeaveAction(gd.getCommIDByPlayerId(playerId));
        exec(la);
    }

}
