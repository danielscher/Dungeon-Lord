package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingThirdSeason;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class AllKindsofBidsFourthSeason extends FrameworkuptoBiddingThirdSeason {
    @Override
    public String createConfig() {
        return Utils.loadResource(AllKindsofBidsFourthSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();
        simulateThirdBiddingSeason();
        allKindsofBidsFourthSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void allKindsofBidsFourthSeason() throws TimeoutException {
        nextRoundAsserter(4);
        // assert monster drawing

        monsterAsserter(6);
        monsterAsserter(11);
        monsterAsserter(16);

        // assert room drawing

        roomAsserter(2);
        roomAsserter(9);

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
        bidsOfFourthSeasonFirstYear();

        //Eval bids
        evalBidsFourthSeason();

        //BidRetrieved
        bidRetrievedAsserter(BidType.MONSTER, 0);
        bidRetrievedAsserter(BidType.ROOM, 0);
        bidRetrievedAsserter(BidType.FOOD, 0);

        bidRetrievedAsserter(BidType.MONSTER, 1);
        bidRetrievedAsserter(BidType.ROOM, 1);
        bidRetrievedAsserter(BidType.GOLD, 1);

        bidRetrievedAsserter(BidType.MONSTER, 2);
        bidRetrievedAsserter(BidType.ROOM, 2);
        bidRetrievedAsserter(BidType.FOOD, 2);

        bidRetrievedAsserter(BidType.MONSTER, 3);
        bidRetrievedAsserter(BidType.ROOM, 3);
        bidRetrievedAsserter(BidType.TUNNEL, 3);
    }

    protected void bidsOfFourthSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(3, BidType.MONSTER, 1);
        assertActionFailed(3);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.IMPS, 2);
        bidPlacedAsserter(BidType.IMPS, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 3, 3);


        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.ROOM, 2);
        assertActionFailed(0);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TRAP, 2);
        bidPlacedAsserter(BidType.TRAP, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlacedAsserter(BidType.TUNNEL, 0, 3);


        this.sendPlaceBid(1, BidType.GOLD, 1);
        bidPlacedAsserter(BidType.GOLD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TUNNEL, 3);
        bidPlacedAsserter(BidType.TUNNEL, 1, 3);


        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TUNNEL, 3);
        bidPlacedAsserter(BidType.TUNNEL, 2, 3);
    }

    protected void evalBidsFourthSeason() throws TimeoutException {
        //FOOD
        goldChangedAsserter(-1, 0);
        foodChangedAsserter(2, 0);
        evilnessChangedAsserter(1, 2);
        foodChangedAsserter(3, 2);

        //NICENESS
        evilnessChangedAsserter(-1, 1);

        //TUNNEL
        //player 3 has 3 imps
        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 1, 1);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 1, 1);
        assertActNow(3);
        sendDigTunnel(3, 4, 0);
        impsChangedAsserter(-1, 3);
        tunnelDugAsserter(3, 4, 0);

        //player 0 has 4 imps
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 1, 0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 1, 0);
        assertActNow(0);
        sendDigTunnel(0, 2, 0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 2, 0);
        assertActNow(0);
        sendDigTunnel(0, 3, 0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 3, 0);

        //player 1 has 5 imps
        //1.tile
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 1, 0);
        //2.tile
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 1);
        //3.tile
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 2);
        //4.tile
        assertActNow(1);
        sendDigTunnel(1, 0, 3);
        impsChangedAsserter(-2, 1);
        tunnelDugAsserter(1, 0,  3);

        //GOLD
        //player 1 has no imps left
        //player 2 has 3 imps and 2 tiles
        impsChangedAsserter(-2, 2);
        //player 3 has 1 imps and 5 tiles
        impsChangedAsserter(-1, 3);


        //IMPS
        foodChangedAsserter(-1, 3);
        impsChangedAsserter(1, 3);

        //TRAP
        goldChangedAsserter(-1, 0);
        trapAcquiredAsserter(0, 26);
    }
}
