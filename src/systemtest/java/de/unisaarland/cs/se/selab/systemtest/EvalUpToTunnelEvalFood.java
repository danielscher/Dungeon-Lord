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


}
