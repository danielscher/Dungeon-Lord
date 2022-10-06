package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class CombatPhaseFirstRound extends FrameworkuptoBiddingFourthSeason {

    @Override
    public String createConfig() {
        return Utils.loadResource(CombatPhaseFirstRound.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);
    }
}
