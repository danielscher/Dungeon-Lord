package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class WholeGameNoEdgeCaseTest extends SystemTest {

    WholeGameNoEdgeCaseTest() {
        super(WholeGameNoEdgeCaseTest.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(WholeGameNoEdgeCaseTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    /*
    helper method for asserting player
    NOTE: requires socketNum == playerId
     */

    protected void assertPlayerHelper(final int[] ids) throws TimeoutException {
        for (final int id : ids) {
            for (final int id2 : ids) {
                assertPlayer(id2, String.valueOf(id), id);
            }
        }
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);
        this.sendRegister(3, "3");
        this.assertConfig(3, config);
        this.sendRegister(4, "4");
        this.assertConfig(4, config);

        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);
        this.assertGameStarted(4);

        this.assertPlayerHelper(new int[]{1, 2, 3, 4});

        this.assertNextYear(1, 1);
        this.assertNextYear(2, 1);
        this.assertNextYear(3, 1);
        this.assertNextYear(4, 1);

        this.assertNextRound(1, 1);
        this.assertNextRound(2, 1);
        this.assertNextRound(3, 1);
        this.assertNextRound(4, 1);

        // TODO try out what Adv ids it gives us and assert them
        // TODO continue writing this

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);
    }
}
