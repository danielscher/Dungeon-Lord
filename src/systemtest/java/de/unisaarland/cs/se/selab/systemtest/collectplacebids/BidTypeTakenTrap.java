package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class BidTypeTakenTrap extends OurSystemTestFramework {
    public BidTypeTakenTrap() {
        super(BidTypeTakenTrap.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(BidTypeTakenTrap.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserter(1);

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
        bidsOfFirstSeasonFirstYear();
    }

    @Override
    protected void bidsOfFirstSeasonFirstYear() throws TimeoutException {
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

        this.sendPlaceBid(2, BidType.TRAP, 1);
        bidPlacedAsserter(BidType.TRAP, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.IMPS, 2);
        bidPlacedAsserter(BidType.IMPS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TRAP, 3);
        assertActionFailed(2);
        assertActNow(2);
    }

}
