package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class EvalUpToTunnelEvalFood extends OurSystemTestFramework {

    EvalUpToTunnelEvalFood() {
        super(EvalUpToTunnelEvalFood.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelEvalFood.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String cfg = createConfig();
        regPhaseAssertions(cfg);
        nextYearAsserter(1);
        simulateFirstBiddingSeason();

        // left after food bids' slots are evaluated
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void foodSlotsEvalAsserter() throws TimeoutException {
        // inform other players that player0 has paid 1 gold for 2 food
        this.assertGoldChanged(0, 1, 0);
        this.assertGoldChanged(1, 1, 0);
        this.assertGoldChanged(2, 1, 0);
        this.assertGoldChanged(3, 1, 0);
        this.assertFoodChanged(0, 2, 0);
        this.assertFoodChanged(1, 2, 0);
        this.assertFoodChanged(2, 2, 0);
        this.assertFoodChanged(3, 2, 0);
    }


}
