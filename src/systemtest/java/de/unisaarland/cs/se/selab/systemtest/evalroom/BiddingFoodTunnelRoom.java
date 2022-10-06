package de.unisaarland.cs.se.selab.systemtest.evalroom;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.SystemTestTemplate;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class BiddingFoodTunnelRoom extends OurSystemTestFramework {

    public BiddingFoodTunnelRoom() {
        super(BiddingOnRoomBasic.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(SystemTestTemplate.class, "configuration.json");
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(0, 1, 2);
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        //simulateFirstBiddingSeason();

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
    }


    @Override
    protected void regPhaseAssertions(final String config) throws TimeoutException {
        this.sendRegister(0, "0");
        this.assertConfig(0, config);
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);

        this.sendStartGame(0);

        this.assertGameStarted(0);
        this.assertGameStarted(1);
        this.assertGameStarted(2);

        this.assertPlayerHelper(new int[]{0, 1, 2});
    }

    @Override
    protected void nextRoundAsserter(final int round) throws TimeoutException {
        this.assertNextRound(0, round);
        this.assertNextRound(1, round);
        this.assertNextRound(2, round);
    }

    @Override
    protected void goldChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertGoldChanged(0, amount, playerId);
        assertGoldChanged(1, amount, playerId);
        assertGoldChanged(2, amount, playerId);
    }

    @Override
    protected void bidPlacedAsserter(final BidType bid, final int playerId, final int slot)
            throws TimeoutException {
        this.assertBidPlaced(0, bid, playerId, slot);
        this.assertBidPlaced(1, bid, playerId, slot);
        this.assertBidPlaced(2, bid, playerId, slot);
    }

    @Override
    protected void foodChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertFoodChanged(0, amount, playerId);
        assertFoodChanged(1, amount, playerId);
        assertFoodChanged(2, amount, playerId);
    }

    @Override
    protected void evilnessChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertEvilnessChanged(0, amount, playerId);
        assertEvilnessChanged(1, amount, playerId);
        assertEvilnessChanged(2, amount, playerId);
    }

    @Override
    protected void adventurerArrivedAsserter(final int advId, final int playerId)
            throws TimeoutException {
        assertAdventurerArrived(0, advId, playerId);
        assertAdventurerArrived(1, advId, playerId);
        assertAdventurerArrived(2, advId, playerId);
    }

    @Override
    protected void monsterAsserter(final int monsterId) throws TimeoutException {
        this.assertMonsterDrawn(0, monsterId);
        this.assertMonsterDrawn(1, monsterId);
        this.assertMonsterDrawn(2, monsterId);
    }

    @Override
    protected void impsChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertImpsChanged(0, amount, playerId);
        assertImpsChanged(1, amount, playerId);
        assertImpsChanged(2, amount, playerId);
    }

    @Override
    protected void tunnelDugAsserter(final int playerId, final int x, final int y)
            throws TimeoutException {
        assertTunnelDug(0, playerId, x, y);
        assertTunnelDug(1, playerId, x, y);
        assertTunnelDug(2, playerId, x, y);

    }

    protected void roomBuildAsserter(final int playerId, final int roomId, final int x, final int y)
            throws TimeoutException {
        assertRoomBuilt(0, playerId, roomId, x, y);
        assertRoomBuilt(1, playerId, roomId, x, y);
        assertRoomBuilt(2, playerId, roomId, x, y);
    }

    @Override
    protected void adventurerAsserter(final int advId) throws TimeoutException {
        this.assertAdventurerDrawn(0, advId);
        this.assertAdventurerDrawn(1, advId);
        this.assertAdventurerDrawn(2, advId);
    }

    @Override
    protected void roomAsserter(final int roomId) throws TimeoutException {
        this.assertRoomDrawn(0, roomId);
        this.assertRoomDrawn(1, roomId);
        this.assertRoomDrawn(2, roomId);
    }

    @Override
    protected void nextYearAsserter(final int year) throws TimeoutException {
        this.assertNextYear(0, year);
        this.assertNextYear(1, year);
        this.assertNextYear(2, year);
    }

    @Override
    protected void bidRetrievedAsserter(final BidType bidType, final int playerId)
            throws TimeoutException {
        assertBidRetrieved(0, bidType, playerId);
        assertBidRetrieved(1, bidType, playerId);
        assertBidRetrieved(2, bidType, playerId);
    }

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserter(1);

        // assert Adv. drawing
        adventurerAsserter(29);
        adventurerAsserter(23);
        adventurerAsserter(2);

        // assert monster drawing

        monsterAsserter(23);
        monsterAsserter(13);
        monsterAsserter(9);

        // assert room drawing

        roomAsserter(5);
        roomAsserter(4);

        // assert bidding started
        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        // assert act now (for requesting bids)
        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);

        // place bids
        bidOnFoodTunnelROOM();

        // assert placing bids

        // first bid category (food)
        goldChangedAsserter(-1, 0);
        foodChangedAsserter(2, 0);
        evilnessChangedAsserter(1, 1);
        foodChangedAsserter(3, 1);
        evilnessChangedAsserter(2, 2);
        foodChangedAsserter(3, 2);
        goldChangedAsserter(1, 2);

        // second bid category (Tunnel)
        diggingTunnel();

        // third bid category (ROOMS)
        placingrooms();
        //retrive bids for slot 1
        bidRetrievedAsserter(BidType.FOOD, 0);
        bidRetrievedAsserter(BidType.FOOD, 1);
        bidRetrievedAsserter(BidType.FOOD, 2);

        //ims returns after digging
        impsChangedAsserter(2, 0);
        impsChangedAsserter(2, 1);
        impsChangedAsserter(3, 2);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(2, 0);
        adventurerArrivedAsserter(29, 1);
        adventurerArrivedAsserter(23, 2);
    }

    private void placingrooms() throws TimeoutException {
        //third bid category (ROOM) evaluating and placing the room
        //First player
        goldChangedAsserter(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        // since the player has a dug tile at (1,1) we can place a room INNER RING
        this.sendBuildRoom(0, 1, 1, 4);
        roomBuildAsserter(0, 4, 1, 1);

        //second player
        goldChangedAsserter(-1, 1);
        assertPlaceRoom(1);
        assertActNow(1);
        // since the player has only one tile at (0,0) but the room restriction is INNER RING
        // player cannot place the room
        this.sendBuildRoom(1, 0, 0, 4);
        assertActionFailed(1);
        this.sendEndTurn(1);

        assertPlaceRoom(2);
        assertActNow(2);
        //player chooses a room that is not available
        this.sendBuildRoom(2, 0, 0, 7);
        assertActionFailed(2);
        // send the next sendBuildRoom Action
        this.sendBuildRoom(2, 0, 0, 4);
        assertActionFailed(2);
        this.sendEndTurn(2);

    }

    private void diggingTunnel() throws TimeoutException {
        assertDigTunnel(0);
        assertActNow(0);
        //dig tunnel at coordinate(0,1)
        this.sendDigTunnel(0, 0, 1);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 0, 1);

        //next tunnel to dig (2 tunnels can be dug for the first slot)
        assertActNow(0);
        //dig tunnel at coordinate(1,1)
        this.sendDigTunnel(0, 1, 1);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 1, 1);

        //moving to the next bid player (3 tiles can be dug)
        assertDigTunnel(1);
        assertActNow(1);
        //dig tunnel at coordinate (1,0)
        this.sendDigTunnel(1, 1, 0);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 1, 0);

        assertActNow(1);
        this.sendDigTunnel(1, 2, 0);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 2, 0);

        assertActNow(1);
        this.sendEndTurn(1);

        //moving to the next bid player 4 imps and 1 supervisor but only 3 imps left

        assertDigTunnel(2);
        assertActNow(2);
        //dig tunnel at coordinate (1,0)
        this.sendDigTunnel(2, 1, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 1, 0);

        assertActNow(2);
        this.sendDigTunnel(2, 2, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 2, 0);

        assertActNow(2);
        this.sendDigTunnel(2, 3, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 3, 0);
    }

    private void bidOnFoodTunnelROOM() throws TimeoutException {

        // first player places all their bids.
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlacedAsserter(BidType.TUNNEL, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 0, 3);

        // second player places their bids.
        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        bidPlacedAsserter(BidType.TUNNEL, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 1, 3);

        //third player places their bids
        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TUNNEL, 2);
        bidPlacedAsserter(BidType.TUNNEL, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 2, 3);

    }

}


