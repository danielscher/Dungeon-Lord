package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 4 Players and all of them Leave
 */
public class EvalUpToTunnelAllPlayerLeft extends OurSystemTestFramework {

    EvalUpToTunnelAllPlayerLeft() {
        super(EvalUpToTunnelAllPlayerLeft.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelAllPlayerLeft.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
