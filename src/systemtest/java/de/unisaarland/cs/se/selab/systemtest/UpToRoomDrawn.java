package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class UpToRoomDrawn extends OurSystemTestFramework {

    UpToRoomDrawn() {
        super(UpToRoomDrawn.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(UpToRoomDrawn.class, "configuration.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        nextRoundAsserter(1);

        // assert Adv. drawing

        adventurerDrawingFirstYearFirstSeason();

        monsterDrawingFirstYearFirstSeason();

        roomDrawingFirstYearFirstSeason();



        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
