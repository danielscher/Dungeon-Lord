package de.unisaarland.cs.se.selab.systemtest.evaluptotunnel;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class EvalUpToTunnelEvalNicenessYaSa extends OurSystemTestFramework {

    public EvalUpToTunnelEvalNicenessYaSa() {
        super(EvalUpToTunnelEvalNicenessYaSa.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelEvalNicenessYaSa.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String cfg = createConfig();
        regPhaseAssertions(cfg);
        nextYearAsserter(1);
        simulateFirstBiddingSeason();
        // start evaluating food
        firstFoodSlotsEvalAsserter(0);
        secondFoodSlotsEvalAsserter(1);
        thirdFoodSlotsEvalAsserter(2);
        // start evaluating niceness
        firstNicenessSlotsEvalAsserter(0);
        secondNicenessSlotsEvalAsserter(1);
        thirdNicenessSlotsEvalAsserter(2);

        // left after food bids' slots are evaluated
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

        this.biddingStartedAsserterAnyoneLeft();

        // assert act now (for requesting bids)

        this.actNowAsserterAnyoneLeft();

        // place bids
        bidsOfFirstSeasonFirstYear();
        // assert placing bids
    }

    protected void firstFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player0 has paid 1 gold for 2 food
        this.goldChangedAsserter(-1, playerId);
        this.foodChangedAsserter(2, playerId);
    }

    protected void secondFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 has paid 1 niceness for 3 food
        this.evilnessChangedAsserter(1, playerId);
        this.foodChangedAsserter(3, playerId);
    }

    protected void thirdFoodSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 has paid 2 niceness for 3 food,1 gold
        this.evilnessChangedAsserter(2, playerId);
        this.foodChangedAsserter(3, playerId);
        this.goldChangedAsserter(1, playerId);
    }

    protected void firstNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player0 gets 1 niceness, (-1 evilness)
        this.evilnessChangedAsserter(-1, playerId);
    }

    protected void secondNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player1 gets 2 niceness(-2 evilness)
        this.evilnessChangedAsserter(-2, playerId);
    }

    protected void thirdNicenessSlotsEvalAsserter(final int playerId) throws TimeoutException {
        // inform other players that player2 has paid 1 gold for 2 niceness
        this.goldChangedAsserter(-1, playerId);
        this.evilnessChangedAsserter(-2, playerId);

    }


}
