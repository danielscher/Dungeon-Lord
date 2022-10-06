package de.unisaarland.cs.se.selab.systemtest.edgyedgecase;


import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * this test digs tunnels and tries to place faulty bids
 */
public class TunnelDiggers extends OurSystemTestFramework {

    public TunnelDiggers() {
        super(TunnelDiggers.class, false);
    }

    @Override
    public String createConfig() {
        // use modded config
        return Utils.loadResource(TunnelDiggers.class, "five_imp_config.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        nextRoundAsserter(1);

        assertEntityDrawingFirstYearFirstSeason();

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);


        // bid placing
        // p3, gold, slot3
        sendPlaceBid(3, BidType.GOLD, 3);
        bidPlacedAsserter(BidType.GOLD, 3, 3);
        assertActNow(3);


        // p1, tunnel, slot1
        sendPlaceBid(1, BidType.TUNNEL,1);
        bidPlacedAsserter(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        // p0, food, s1
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        // p0, IMPS, s1 -> action failed because of slot
        sendPlaceBid(0, BidType.IMPS, 1);
        assertActionFailed(0);
        assertActNow(0);

        // p0, imps, s2
        sendPlaceBid(0, BidType.IMPS, 2);
        bidPlacedAsserter(BidType.IMPS, 0, 2);
        assertActNow(0);

        // p0, food, s3 --> action failed because of category
        sendPlaceBid(0, BidType.FOOD, 3);
        assertActionFailed(0);
        assertActNow(0);

        // p0, tunnel, s3
        sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlacedAsserter(BidType.TUNNEL, 0, 3);


        // p1, nice, slot2
        sendPlaceBid(1, BidType.NICENESS,2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);
        assertActNow(1);

        // p1, food, slot3
        sendPlaceBid(1, BidType.FOOD,3);
        bidPlacedAsserter(BidType.FOOD, 1, 3);

        // p2, tunnel, s1
        sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserter(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        // p2, nice, s2
        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 2, 2);
        assertActNow(2);

        // p2, food, s3
        sendPlaceBid(2, BidType.FOOD, 3);
        bidPlacedAsserter(BidType.FOOD, 2, 3);

        // p3, nice, slot1
        sendPlaceBid(3, BidType.NICENESS, 1);
        bidPlacedAsserter(BidType.NICENESS, 3, 1);
        assertActNow(3);

        // p3, food, slot2
        sendPlaceBid(3, BidType.FOOD, 2);
        bidPlacedAsserter(BidType.FOOD, 3, 2);



        // evaluation
        // FOOD
        goldChangedAsserter(-1, 0);
        foodChangedAsserter(2, 0);

        evilnessChangedAsserter(1, 3);
        foodChangedAsserter(3, 3);

        evilnessChangedAsserter(2, 1);
        foodChangedAsserter(3, 1);
        goldChangedAsserter(1,1);

        // NICENESS
        evilnessChangedAsserter(-1, 3);
        evilnessChangedAsserter(-2, 1);
        goldChangedAsserter(-1, 2);
        evilnessChangedAsserter(-2, 2);

        // TUNNEL
        // p1
        // p1 - 1st tile
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0,1);
        impsChangedAsserter(-1, 1);
        tunnelDugAsserter(1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0,1);
        assertActionFailed(1);

        // p1 - 2nd tile (end turn)
        assertActNow(1);
        sendEndTurn(1);

        // p1 - 3rd tile (not possible, isn't his turn)
        sendDigTunnel(1, 2, 1);
        assertActionFailed(1);
        sendDigTunnel(1, 1, 0);
        assertActionFailed(1); // TODO check that 2 (player to dig) doesn't receive another
        // act now



        // p2
        assertDigTunnel(2);
        // p2 - 1st tile
        assertActNow(2);
        sendDigTunnel(2, 0,1);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 0, 1);

        // p2 - 2nd tile
        assertActNow(2);
        sendDigTunnel(2, 1,0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 1, 0);

        // p2 - 3rd tile (invalid pos)
        assertActNow(2);
        sendDigTunnel(2, 1,1);
        assertActionFailed(2);

        // p2 - 3rd tile (valid pos)
        assertActNow(2);
        sendDigTunnel(2, 2,0);
        impsChangedAsserter(-1, 2);
        tunnelDugAsserter(2, 2, 0);

        // p0 - 1st tile
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 1,0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 1, 0);

        // p0 - 2nd tile
        assertActNow(0);
        sendDigTunnel(0, 2,0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 2, 0);

        // p0 - 3rd tile
        assertActNow(0);
        sendDigTunnel(0, 3,0);
        impsChangedAsserter(-1, 0);
        tunnelDugAsserter(0, 3, 0);


        // p0 - 4th tile
        assertActNow(0);
        sendDigTunnel(0, 4,0);
        impsChangedAsserter(-2, 0);
        tunnelDugAsserter(0, 4, 0);

        /*
        // GOLD
        impsChangedAsserter(-2, 3);

        // IMPS
        foodChangedAsserter(-1, 0);
        impsChangedAsserter(1, 0);
        */



        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
