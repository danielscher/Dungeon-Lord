package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class PlayerLeftWhilePlacingBidFirstSeason extends OurSystemTestFramework {
    public PlayerLeftWhilePlacingBidFirstSeason() {
        super(PlayerLeftWhilePlacingBidFirstSeason.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(PlayerLeftWhilePlacingBidFirstSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserterHelper(1);

        simulateFirstBiddingSeason();

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }


    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(1);

        assertEntityDrawingFirstYearFirstSeason();

        // assert bidding started

        this.biddingStartedAsserterHelper();

        // assert act now (for requesting bids)

        this.actNowAsserterHelper();

        // place bids
        bidsOfFirstSeasonFirstYear();

        // assert placing bids

        // first bid category (food)
        goldChangedAsserterHelper(-1, 1);
        foodChangedAsserterHelper(2, 1);
        evilnessChangedAsserterHelper(1, 2);
        foodChangedAsserterHelper(3, 2);
        evilnessChangedAsserterHelper(2, 3);
        foodChangedAsserterHelper(3, 3);
        goldChangedAsserterHelper(1, 3);

        // second bid category (niceness)
        evilnessChangedAsserterHelper(-1, 1);
        evilnessChangedAsserterHelper(-2, 2);
        goldChangedAsserterHelper(-1, 3);
        evilnessChangedAsserterHelper(-2, 3);

        // imp category
        foodChangedAsserterHelper(-1, 1);
        impsChangedAsserterHelper(1, 1);
        foodChangedAsserterHelper(-2, 2);
        impsChangedAsserterHelper(2, 2);
        foodChangedAsserterHelper(-1, 3);
        goldChangedAsserterHelper(-1, 3);
        impsChangedAsserterHelper(2, 3);

        //retrieve the slot 1
        bidRetrievedAsserterHelper(BidType.FOOD, 1);
        bidRetrievedAsserterHelper(BidType.FOOD, 2);
        bidRetrievedAsserterHelper(BidType.FOOD, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserterHelper(0, 1);
        adventurerArrivedAsserterHelper(2, 2);
        adventurerArrivedAsserterHelper(29, 3);
    }

    @Override
    protected void bidsOfFirstSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 0, 2);
        assertActNow(0);

        sendLeave(0);
        currSockets.remove((Integer) 0);
        playerLeftAsserterHelper(0);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 2, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 3, 3);
    }
}

