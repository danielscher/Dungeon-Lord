package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class PlayerLeftWhileDiggingTunnelSecondSeason extends OurSystemTestFramework {
    public PlayerLeftWhileDiggingTunnelSecondSeason() {
        super(PlayerLeftWhileDiggingTunnelSecondSeason.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(PlayerLeftWhileDiggingTunnelSecondSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserterHelper(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }


    protected void simulateSecondBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(2);

        // assert Adv. drawing

        adventurerAsserterHelper(18);
        adventurerAsserterHelper(11);
        adventurerAsserterHelper(20);
        adventurerAsserterHelper(6);

        // assert monster drawing

        monsterAsserterHelper(7);
        monsterAsserterHelper(22);
        monsterAsserterHelper(1);

        // assert room drawing

        roomAsserterHelper(8);
        roomAsserterHelper(15);

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
        evalBidsSecondSeason();


        //BidRetrieved
        bidRetrievedAsserterHelper(BidType.NICENESS, 0);
        bidRetrievedAsserterHelper(BidType.IMPS, 0);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 0);

        bidRetrievedAsserterHelper(BidType.NICENESS, 3);
        bidRetrievedAsserterHelper(BidType.IMPS, 3);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 3);

        // imp return
        impsChangedAsserterHelper(4, 0);
        goldChangedAsserterHelper(1, 0);
        impsChangedAsserterHelper(3, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserterHelper(18, 0);
        adventurerArrivedAsserterHelper(11, 3);
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.GOLD, 2);
        bidPlacedAsserterHelper(BidType.GOLD, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TRAP, 3);
        bidPlacedAsserterHelper(BidType.TRAP, 1, 3);

        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.GOLD, 2);
        bidPlacedAsserterHelper(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendLeave(2);
        currSockets.remove((Integer) 2);
        playerLeftAsserterHelper(2);


        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.GOLD, 2);
        bidPlacedAsserterHelper(BidType.GOLD, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.TRAP, 3);
        bidPlacedAsserterHelper(BidType.TRAP, 3, 3);

        this.sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.GOLD, 2);
        bidPlacedAsserterHelper(BidType.GOLD, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TRAP, 3);
        bidPlacedAsserterHelper(BidType.TRAP, 0, 3);
    }

    protected void evalBidsSecondSeason() throws TimeoutException {
        //BidType Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChangedAsserterHelper(-1, 1);
        tunnelDugAsserter(1, 0, 1);

        assertActNow(1);
        sendLeave(1);
        currSockets.remove((Integer) 1);
        playerLeftAsserterHelper(1);


        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 1, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 1, 0);
        assertActNow(3);
        sendDigTunnel(3, 2, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 2, 0);
        assertActNow(3);
        sendDigTunnel(3, 3, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 3, 0);

        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 1);
        assertActNow(0);
        sendDigTunnel(0, 0, 2);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 2);
        assertActNow(0);
        sendDigTunnel(0, 0, 3);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 3);


        //second bid category (gold)
        //player 3 now has 3 tiles 0 imp
        //player 0 now has 3 tiles 1 imp
        impsChangedAsserterHelper(-1, 0);

        //trap category
        //player 0 now has 2 golds
        trapAcquiredAsserter(3, 26);
        goldChangedAsserterHelper(-2, 0);
        trapAcquiredAsserter(0, 6);
        trapAcquiredAsserter(0, 19);

    }
}
