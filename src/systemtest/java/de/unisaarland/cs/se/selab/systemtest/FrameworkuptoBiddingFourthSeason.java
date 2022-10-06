package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class FrameworkuptoBiddingFourthSeason extends FrameworkuptoBiddingThirdSeason {


    @Override
    public String createConfig() {
        return Utils.loadResource(FrameworkuptoBiddingFourthSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();
        simulateThirdBiddingSeason();
        simulateFourthBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void simulateFourthBiddingSeason() throws TimeoutException {
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
        bidRetrievedAsserter(BidType.FOOD, 1);

        bidRetrievedAsserter(BidType.MONSTER, 2);
        bidRetrievedAsserter(BidType.ROOM, 2);
        bidRetrievedAsserter(BidType.FOOD, 2);

        bidRetrievedAsserter(BidType.MONSTER, 3);
        bidRetrievedAsserter(BidType.ROOM, 3);
        bidRetrievedAsserter(BidType.FOOD, 3);
    }

    protected void bidsOfFourthSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 3, 3);

        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 0, 3);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 2, 3);
    }

    protected void evalBidsFourthSeason() throws TimeoutException {
        goldChangedAsserter(-1, 3);
        foodChangedAsserter(2, 3);
        evilnessChangedAsserter(1, 0);
        foodChangedAsserter(3, 0);
        evilnessChangedAsserter(2, 1);
        foodChangedAsserter(3, 1);
        goldChangedAsserter(1, 1);

        // second bid category (niceness)
        evilnessChangedAsserter(-1, 3);
        evilnessChangedAsserter(-2, 0);
        goldChangedAsserter(-1, 1);
        evilnessChangedAsserter(-2, 1);

        // imp category
        foodChangedAsserter(-1, 3);
        impsChangedAsserter(1, 3);
        foodChangedAsserter(-2, 0);
        impsChangedAsserter(2, 0);
        foodChangedAsserter(-1, 1);
        goldChangedAsserter(-1, 1);
        impsChangedAsserter(2, 1);
    }
}
