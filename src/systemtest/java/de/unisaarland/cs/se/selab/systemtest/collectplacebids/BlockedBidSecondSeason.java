package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class BlockedBidSecondSeason extends OurSystemTestFramework {
    public BlockedBidSecondSeason() {
        super(BlockedBidSecondSeason.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(BlockedBidSecondSeason.class, "configuration.json");
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
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 3);
        assertActionFailed(0);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.IMPS, 3);
        assertActionFailed(0);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 0,3);
    }
}
