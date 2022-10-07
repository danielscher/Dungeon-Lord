package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class BidonTunnelSecondSeason extends OurSystemTestFramework {
    protected BidonTunnelSecondSeason(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    public BidonTunnelSecondSeason() {
        super(BidonTunnelSecondSeason.class, false);
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

        evalBidsofSecondSeasonFirstYear();

        //BidRetrieved
        bidRetrievedAsserter(BidType.NICENESS, 0);
        bidRetrievedAsserter(BidType.IMPS, 0);
        bidRetrievedAsserter(BidType.TUNNEL, 0);

        bidRetrievedAsserter(BidType.NICENESS, 1);
        bidRetrievedAsserter(BidType.IMPS, 1);
        bidRetrievedAsserter(BidType.TUNNEL, 1);

        bidRetrievedAsserter(BidType.NICENESS, 2);
        bidRetrievedAsserter(BidType.IMPS, 2);
        bidRetrievedAsserter(BidType.TUNNEL, 2);

        bidRetrievedAsserter(BidType.NICENESS, 3);
        bidRetrievedAsserter(BidType.IMPS, 3);
        bidRetrievedAsserter(BidType.TUNNEL, 3);

        // imp return
        impsChangedAsserter(4, 1);
        goldChangedAsserter(2, 1);

        impsChangedAsserter(5, 2);
        goldChangedAsserter(2, 2);

        impsChangedAsserter(3, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(18, 0);
        adventurerArrivedAsserter(11, 1);
        adventurerArrivedAsserter(20, 2);
        adventurerArrivedAsserter(6, 3);
    }

    protected void evalBidsofSecondSeasonFirstYear() throws TimeoutException {
        // assert placing bids
        //player 1 has 4 food, 4 evil, 5imps
        //player 2 has 5 food, 5 evil, 5imps, 2 gold
        //player 0 has 4 food, 4 evil, 4imps, 2 gold

        // TUNNEL
        //player 1 got 2 tiles, 3imps left
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 2);

        //player 2 got 3 tiles, 1 imp left
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

        //player 3 got 3 tiles, 0 imp left
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

        //second bid category (gold)
        //player 1 1 imp left
        impsChangedAsserter(-2, 1);
        //player 2 0 imp left
        impsChangedAsserter(-2, 2);
        //player 3 has no imp

        //trap category
        goldChangedAsserter(-1, 1);
        trapAcquiredAsserter(1, 26);

        trapAcquiredAsserter(2, 6);

        goldChangedAsserter(-2, 3);
        trapAcquiredAsserter(3, 19);
        trapAcquiredAsserter(3, 5);
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 1, 3);

        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 2, 3);

        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 3, 3);

        this.sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.GOLD, 2);
        bidPlacedAsserter(BidType.GOLD, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TRAP, 3);
        bidPlacedAsserter(BidType.TRAP, 0, 3);
    }
}
