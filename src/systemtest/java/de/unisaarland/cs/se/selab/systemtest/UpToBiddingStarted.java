package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class UpToBiddingStarted extends OurSystemTestFramework {

    UpToBiddingStarted() {
        super(UpToBiddingStarted.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(UpToBiddingStarted.class, "configuration.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        nextRoundAsserter(1);

        // assert Adv. drawing

        adventurerAsserter(5);
        adventurerAsserter(21);
        adventurerAsserter(24);
        adventurerAsserter(14);

        // assert monster drawing

        monsterAsserter(8);
        monsterAsserter(4);
        monsterAsserter(2);

        // assert room drawing

        roomAsserter(1);
        roomAsserter(11);

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);


        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
