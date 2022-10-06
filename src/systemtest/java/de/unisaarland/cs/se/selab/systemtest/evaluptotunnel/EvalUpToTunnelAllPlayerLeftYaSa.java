package de.unisaarland.cs.se.selab.systemtest.evaluptotunnel;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 4 Players and all of them Leave
 */
public class EvalUpToTunnelAllPlayerLeftYaSa extends OurSystemTestFramework {

    public EvalUpToTunnelAllPlayerLeftYaSa() {
        super(EvalUpToTunnelAllPlayerLeftYaSa.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelAllPlayerLeftYaSa.class,
                "configuration.json");
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
