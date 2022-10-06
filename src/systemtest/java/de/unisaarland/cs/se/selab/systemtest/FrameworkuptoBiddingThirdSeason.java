package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class FrameworkuptoBiddingThirdSeason extends FrameworkuptoBiddingSecondSeason {


    @Override
    public String createConfig() {
        return Utils.loadResource(FrameworkuptoBiddingThirdSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();
        simulateThirdBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void simulateThirdBiddingSeason() throws TimeoutException {
        nextRoundAsserter(3);

        // assert Adv. drawing

        adventurerAsserter(9);
        adventurerAsserter(15);
        adventurerAsserter(26);
        adventurerAsserter(16);

        // assert monster drawing

        monsterAsserter(14);
        monsterAsserter(3);
        monsterAsserter(20);

        // assert room drawing

        roomAsserter(0);
        roomAsserter(10);

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
        bidsOfThirdSeasonFirstYear();

        //Eval bids
        evalBidsThirdSeason();

        //BidRetrieved
        bidRetrievedAsserter(BidType.GOLD, 0);
        bidRetrievedAsserter(BidType.TRAP, 0);
        bidRetrievedAsserter(BidType.TUNNEL, 0);

        bidRetrievedAsserter(BidType.GOLD, 1);
        bidRetrievedAsserter(BidType.TRAP, 1);
        bidRetrievedAsserter(BidType.TUNNEL, 1);

        bidRetrievedAsserter(BidType.GOLD, 2);
        bidRetrievedAsserter(BidType.TRAP, 2);
        bidRetrievedAsserter(BidType.TUNNEL, 2);

        bidRetrievedAsserter(BidType.GOLD, 3);
        bidRetrievedAsserter(BidType.TRAP, 3);
        bidRetrievedAsserter(BidType.TUNNEL, 3);

        // imp return
        impsChangedAsserter(3, 0);
        impsChangedAsserter(2, 2);
        impsChangedAsserter(3, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(16, 0);
        adventurerArrivedAsserter(9, 1);
        adventurerArrivedAsserter(26, 2);
        adventurerArrivedAsserter(15, 3);
    }

    protected void bidsOfThirdSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 2, 3);

        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 3, 3);

        this.sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 0, 3);

        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.ROOM, 3);
        bidPlacedAsserter(BidType.ROOM, 1, 3);
    }

    protected void evalBidsThirdSeason() throws TimeoutException {

        //BidType Tunnel
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 0, 1);
        assertActNow(2);
        sendDigTunnel(2, 0, 2);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 0, 2);

        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 1, 0);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 1, 0);
        assertActNow(3);
        sendDigTunnel(3, 2, 0);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 2, 0);
        assertActNow(3);
        sendDigTunnel(3, 3, 0);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 3, 0);

        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 0, 1);
        assertActNow(0);
        sendDigTunnel(0, 0, 2);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 0, 2);
        assertActNow(0);
        sendDigTunnel(0, 0, 3);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 0, 3);


        //BidType Monster
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 14);
        foodChangedAsserter(-1, 2);
        evilnessChangedAsserter(1, 2);
        monsterHiredAsserter(14, 2);

        assertSelectMonster(3);
        assertActNow(3);
        sendHireMonster(3, 3);
        foodChangedAsserter(-2, 3);
        evilnessChangedAsserter(1, 3);
        monsterHiredAsserter(3, 3);

        foodChangedAsserter(-1, 0);
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 20);
        foodChangedAsserter(-3, 0);
        monsterHiredAsserter(20, 0);


        //BidType Room
        goldChangedAsserter(-1, 2);
        assertPlaceRoom(2);
        assertActNow(2);
        sendBuildRoom(2, 0, 2, 0);
        roomBuiltAsserter(2, 0, 0, 2);

        goldChangedAsserter(-1, 3);
        assertPlaceRoom(3);
        assertActNow(3);
        sendEndTurn(3);

        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 3, 10);
        roomBuiltAsserter(0, 10, 0, 3);
    }
}
