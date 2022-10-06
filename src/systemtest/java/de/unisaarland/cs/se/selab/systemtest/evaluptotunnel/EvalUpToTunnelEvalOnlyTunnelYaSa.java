package de.unisaarland.cs.se.selab.systemtest.evaluptotunnel;


import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class EvalUpToTunnelEvalOnlyTunnelYaSa extends OurSystemTestFramework {

    // this is to test in year 1 season 1 the third tunnel slot fail since no enough imps
    public EvalUpToTunnelEvalOnlyTunnelYaSa() {
        super(EvalUpToTunnelEvalOnlyTunnelYaSa.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelEvalOnlyTunnelYaSa.class, "configuration.json");
    }

    @Override
    protected Set<Integer> createSockets() {
        // here I maintain a socketset to store the player who is playing
        currSockets.add(0);
        currSockets.add(1);
        currSockets.add(2);
        currSockets.add(3);
        return Set.of(0, 1, 2, 3);
    }

    @Override
    public void run() throws TimeoutException {
        final String cfg = createConfig();
        regPhaseAssertions(cfg);
        nextYearAsserterHelper(1);
        simulateFirstBiddingSeason();
        // start evaluating food
        firstFoodSlotsEvalAsserter(0);
        secondFoodSlotsEvalAsserter(1);
        thirdFoodSlotsEvalAsserter(2);
        // start evaluating niceness
        firstNicenessSlotsEvalAsserter(0);
        secondNicenessSlotsEvalAsserter(1);
        thirdNicenessSlotsEvalAsserter(2);

        firstTunnelSlotsEvalAsserter(0);
        secondTunnelSlotsEvalAsserter(1);
        thirdTunnelSlotsEvalAsserter(2);

        // left after food bids' slots are evaluated
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    @Override
    protected void regPhaseAssertions(final String config) throws TimeoutException {
        this.sendRegister(0, "0");
        this.assertConfig(0, config);
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);
        this.sendRegister(3, "3");
        this.assertConfig(3, config);

        this.assertGameStarted(0);
        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);

        this.assertPlayerHelper(new int[]{0, 1, 2, 3});
    }

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(1);

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
        // assert placing bids
    }

    protected void firstFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player0 has paid 1 gold for 2 food
        this.goldChangedAsserterHelper(-1, playerId);
        this.foodChangedAsserterHelper(2, playerId);
    }

    protected void secondFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 has paid 1 niceness for 3 food
        this.evilnessChangedAsserterHelper(1, playerId);
        this.foodChangedAsserterHelper(3, playerId);
    }

    protected void thirdFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 has paid 2 niceness for 3 food,1 gold
        this.evilnessChangedAsserterHelper(2, playerId);
        this.foodChangedAsserterHelper(3, playerId);
        this.goldChangedAsserterHelper(1, playerId);
    }

    protected void firstNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player0 gets 1 niceness, (-1 evilness)
        this.evilnessChangedAsserterHelper(-1, playerId);
    }

    protected void secondNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 gets 2 niceness(-2 evilness)
        this.evilnessChangedAsserterHelper(-2, playerId);
    }

    protected void thirdNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player2 has paid 1 gold for 2 niceness
        this.goldChangedAsserterHelper(-1, playerId);
        this.evilnessChangedAsserterHelper(-2, playerId);
    }

    protected void firstTunnelSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player0 has paid 2 imps for 2 tunnel,need actNow
        this.assertDigTunnel(playerId);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 1, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 1, 0);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 2, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 2, 0);
    }

    protected void secondTunnelSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 has paid 3 imps for 3 tunnel
        this.assertDigTunnel(playerId);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 1, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 1, 0);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 2, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 2, 0);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 3, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 3, 0);
    }

    protected void thirdTunnelSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player2 cannot afford 5 imps to dig
        this.assertDigTunnel(playerId);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 1, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 1, 0);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 2, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 2, 0);

        this.assertActNow(playerId);
        this.sendDigTunnel(playerId, 3, 0);
        this.impsChangedAsserterHelper(-1, playerId);
        this.tunnelDugAsserter(playerId, 3, 0);
    }

    @Override
    protected void bidsOfFirstSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlacedAsserterHelper(BidType.TUNNEL, 0, 3);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.TUNNEL, 3);
        bidPlacedAsserterHelper(BidType.TUNNEL, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.TUNNEL, 3);
        bidPlacedAsserterHelper(BidType.TUNNEL, 2, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.TUNNEL, 3);
        bidPlacedAsserterHelper(BidType.TUNNEL, 3, 3);
    }


}
