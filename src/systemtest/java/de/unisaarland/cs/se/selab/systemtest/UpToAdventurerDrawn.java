package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class UpToAdventurerDrawn extends OurSystemTestFramework {

    UpToAdventurerDrawn() {
        super(UpToAdventurerDrawn.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(UpToAdventurerDrawn.class, "configuration.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        nextRoundAsserter(1);

        adventurerDrawingFirstYearFirstSeason();

        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
