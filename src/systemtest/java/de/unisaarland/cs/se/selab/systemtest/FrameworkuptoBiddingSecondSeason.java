package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class FrameworkuptoBiddingSecondSeason extends OurSystemTestFramework {

    public FrameworkuptoBiddingSecondSeason() {
        super(FrameworkuptoBiddingSecondSeason.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(FrameworkuptoBiddingSecondSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void simulateSecondBiddingSeason() throws TimeoutException {
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
        bidsOfSecondSeasonFirstYear();

        // assert placing bids

        // first bid category (food)
        goldChangedAsserter(-1, 1);
        foodChangedAsserter(2, 1);
        evilnessChangedAsserter(1, 2);
        foodChangedAsserter(3, 2);
        evilnessChangedAsserter(2, 3);
        foodChangedAsserter(3, 3);
        goldChangedAsserter(1, 3);

        //second bid category (gold)
        //only 1 because every player has so far only one tile
        impsChangedAsserter(-1, 1);
        impsChangedAsserter(-1, 2);
        impsChangedAsserter(-1, 3);

        //trap category
        goldChangedAsserter(-1, 1);
        trapAcquiredAsserter(1, 26);

        trapAcquiredAsserter(2, 6);

        goldChangedAsserter(-2, 3);
        trapAcquiredAsserter(3, 19);
        trapAcquiredAsserter(3, 5);

        //BidRetrieved
        bidRetrievedAsserter(BidType.NICENESS, 0);
        bidRetrievedAsserter(BidType.IMPS, 0);
        bidRetrievedAsserter(BidType.FOOD, 0);

        bidRetrievedAsserter(BidType.NICENESS, 1);
        bidRetrievedAsserter(BidType.IMPS, 1);
        bidRetrievedAsserter(BidType.FOOD, 1);

        bidRetrievedAsserter(BidType.NICENESS, 2);
        bidRetrievedAsserter(BidType.IMPS, 2);
        bidRetrievedAsserter(BidType.FOOD, 2);

        bidRetrievedAsserter(BidType.NICENESS, 3);
        bidRetrievedAsserter(BidType.IMPS, 3);
        bidRetrievedAsserter(BidType.FOOD, 3);

        // imp return
        impsChangedAsserter(1, 1);
        goldChangedAsserter(1, 1);

        impsChangedAsserter(1, 2);
        goldChangedAsserter(1, 2);

        impsChangedAsserter(1, 3);
        goldChangedAsserter(1, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(18, 0);
        adventurerArrivedAsserter(11, 1);
        adventurerArrivedAsserter(20, 2);
        adventurerArrivedAsserter(6, 3);
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 2, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 3, 3);

        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 0, 3);
    }
}
