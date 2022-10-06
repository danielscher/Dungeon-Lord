package de.unisaarland.cs.se.selab.systemtest.evaltomonster;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class Send4ImpsToMineGold extends FrameworkuptoBiddingSecondSeason {

    @Override
    public String createConfig() {
        return Utils.loadResource(Send4ImpsToMineGold.class, "configuration_5Imps.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        //bid on Tunnel - Monster - Room player 3 gets nothing
        simulateFirstBiddingSeason();
        //bid on Food - Niceness - gold
        simulateSecondBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    //First Season

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        drawCardsAndPlaceBidsFirstSeason();

        evalBidsFirstSeason();

        retrieveBidsAndImps();

        adventurersArrive();
    }

    protected void drawCardsAndPlaceBidsFirstSeason() throws TimeoutException {
        nextRoundAsserter(1);

        // assert Adv. drawing

        assertEntityDrawingFirstYearFirstSeason();

        // assert bidding started

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);

        // place bids
        placeBidsTunnelMonsterRoom();

    }

    private void evaltunnelAssertion() throws TimeoutException {
        //BidType Tunnel
        //slot 1:
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 2);

        //slot 2:
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 1, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 1, 0);

        assertActNow(2);
        sendDigTunnel(2, 2, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 2, 0);

        assertActNow(2);
        sendDigTunnel(2, 3, 0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 3, 0);

        //slot 3:
        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 0, 1);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 0, 1);

        assertActNow(3);
        sendDigTunnel(3, 0, 2);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 0, 2);

        assertActNow(3);
        sendDigTunnel(3, 0, 3);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 0, 3);

        assertActNow(3);
        sendDigTunnel(3, 0, 4);
        impsChangedAsserter(-2, 3); // supervisor + digging
        tunnelDugAsserter(3, 0, 4);

    }

    protected void evalBidsFirstSeason() throws TimeoutException {

        //FOOD only player0
        goldChangedAsserter(-1, 0);
        foodChangedAsserter(2, 0);

        evaltunnelAssertion();

        //BidType Monster
        //slot 1:
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 9);
        evilnessChangedAsserter(3, 0);
        monsterHiredAsserter(9, 0);

        //slot 2:
        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 23);
        monsterHiredAsserter(23, 1);

        //slot 3:
        foodChangedAsserter(-1, 2);
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 13);
        foodChangedAsserter(-1, 2);
        evilnessChangedAsserter(1, 2);
        monsterHiredAsserter(13, 2);

        //BidType Room
        //slot 1:
        goldChangedAsserter(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendEndTurn(0);

        //slot 2:
        goldChangedAsserter(-1, 1);
        assertPlaceRoom(1);
        assertActNow(1);
        sendEndTurn(1);

        //slot 3:
        assertPlaceRoom(2);
        assertActNow(2);
        sendEndTurn(2);
        //sendBuildRoom(2, 0, 2, 5); //FIXME: this causes timeout.
        //roomBuiltAsserter(2, 5, 0, 2);
    }

    protected void placeBidsTunnelMonsterRoom() throws TimeoutException {

        //player 0
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 0, 3);

        //player 1
        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 1, 3);

        //player 2
        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 2, 3);

        //player 3
        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 3, 3);


    }

    private void retrieveBidsAndImps() throws TimeoutException {
        //BidRetrieved for slot 1 (each round)
        bidRetrievedAsserter(BidType.FOOD, 0);
        bidRetrievedAsserter(BidType.TUNNEL, 1);
        bidRetrievedAsserter(BidType.TUNNEL, 2);
        bidRetrievedAsserter(BidType.TUNNEL, 3);

        // imps return
        impsChangedAsserter(2, 1);

        impsChangedAsserter(3, 2);

        impsChangedAsserter(5, 3);

    }

    private void adventurersArrive() throws TimeoutException {
        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(0, 1);
        adventurerArrivedAsserter(2, 3);
        adventurerArrivedAsserter(29, 2);
        adventurerArrivedAsserter(23, 0);
    }

    //Second Season

    @Override
    protected void simulateSecondBiddingSeason() throws TimeoutException {
        drawCardsAndPlaceBidsSecondSeason();

        evalBidsSecondSeason();

        retrieveBidsAndImps2();

        adventurersArrive2();
    }

    private void drawCardsAndPlaceBidsSecondSeason() throws TimeoutException {
        nextRoundAsserter(2);

        // assert Adv. drawing

        adventurerAsserter(18);
        adventurerAsserter(11);
        adventurerAsserter(20);
        adventurerAsserter(6);

        // assert monster drawing

        monsterAsserter(7);
        monsterAsserter(22);
        monsterAsserter(1);

        // assert room drawing

        roomAsserter(8);
        roomAsserter(15);

        // assert bidding started

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);

        // place bids
        placeBidsFoodNiceGold();
    }

    private void placeBidsFoodNiceGold() throws TimeoutException {
        // first player places all their bids.
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 0, 2);
        assertActNow(0);

        sendPlaceBid(0, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 0, 3);

        // second player places their bids. (Starting player)
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);
        assertActNow(1);

        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 1, 3);

        //third player places their bids
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 2, 2);
        assertActNow(2);

        sendPlaceBid(2, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 2, 3);

        //fourth player places their bids
        sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 3, 1);
        assertActNow(3);

        sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 3, 2);
        assertActNow(3);

        sendPlaceBid(3, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 3, 3);
    }

    private void evalBidsSecondSeason() throws TimeoutException {

        // first bid category (food)
        //slot 1:
        goldChangedAsserter(-1, 1);
        foodChangedAsserter(2, 1);
        //slot 2:
        evilnessChangedAsserter(1, 2);
        foodChangedAsserter(3, 2);
        //slot 3:
        evilnessChangedAsserter(2, 3);
        foodChangedAsserter(3, 3);
        goldChangedAsserter(1, 3);

        // second bid category (niceness)
        //slot 1:
        evilnessChangedAsserter(-1, 1);
        //slot 2:
        evilnessChangedAsserter(-2, 2);
        //slot 3:
        goldChangedAsserter(-1, 3);
        evilnessChangedAsserter(-2, 3);

        // third bid category (GOLD) all players can use 1 imp as there is only 1 tile.
        // slot 1:
        impsChangedAsserter(-2, 1);

        // slot 2:
        impsChangedAsserter(-3, 2);

        // slot 3:
        impsChangedAsserter(-5, 3);
    }

    private void retrieveBidsAndImps2() throws TimeoutException {
        //retrieve bid first blocked then slot 1
        bidRetrievedAsserter(BidType.MONSTER, 0);
        bidRetrievedAsserter(BidType.ROOM, 0);
        bidRetrievedAsserter(BidType.FOOD, 0);

        bidRetrievedAsserter(BidType.MONSTER, 1);
        bidRetrievedAsserter(BidType.ROOM, 1);
        bidRetrievedAsserter(BidType.FOOD, 1);

        bidRetrievedAsserter(BidType.MONSTER, 2);
        bidRetrievedAsserter(BidType.ROOM, 2);
        bidRetrievedAsserter(BidType.FOOD, 2);

        bidRetrievedAsserter(BidType.MONSTER, 3);
        bidRetrievedAsserter(BidType.ROOM, 3);
        bidRetrievedAsserter(BidType.FOOD, 3);

        // imps return gold received.
        impsChangedAsserter(2, 1);
        goldChangedAsserter(2, 1);

        impsChangedAsserter(3, 2);
        goldChangedAsserter(3, 2);

        impsChangedAsserter(5, 3);
        goldChangedAsserter(4, 3);
    }

    private void adventurersArrive2() throws TimeoutException {
        adventurerArrivedAsserter(18, 1);
        adventurerArrivedAsserter(11, 2);
        adventurerArrivedAsserter(20, 3);
        adventurerArrivedAsserter(6, 0);
    }


}
