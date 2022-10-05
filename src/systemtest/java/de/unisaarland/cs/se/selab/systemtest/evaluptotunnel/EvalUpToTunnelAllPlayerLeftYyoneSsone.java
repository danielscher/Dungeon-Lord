package de.unisaarland.cs.se.selab.systemtest.evaluptotunnel;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 4 Players and all of them Leave
 */
public class EvalUpToTunnelAllPlayerLeftYyoneSsone extends OurSystemTestFramework {

    public EvalUpToTunnelAllPlayerLeftYyoneSsone() {
        super(EvalUpToTunnelAllPlayerLeftYyoneSsone.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelAllPlayerLeftYyoneSsone.class,
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
