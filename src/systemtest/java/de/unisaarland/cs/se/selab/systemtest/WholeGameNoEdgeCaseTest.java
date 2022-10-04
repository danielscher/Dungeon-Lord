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
        return Set.of(0, 1, 2, 3);
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
        this.sendRegister(0, "0");
        this.assertConfig(0, config);
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);
        this.sendRegister(3, "3");
        this.assertConfig(3, config);

        this.assertGameStarted(0);
        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);

        this.assertPlayerHelper(new int[]{0, 1, 2, 3});

        this.assertNextYear(0, 1);
        this.assertNextYear(1, 1);
        this.assertNextYear(2, 1);
        this.assertNextYear(3, 1);

        this.assertNextRound(0, 1);
        this.assertNextRound(1, 1);
        this.assertNextRound(2, 1);
        this.assertNextRound(3, 1);

        // TODO try out what Adv ids it gives us and assert them
        // TODO continue writing this

        // assert Adv. drawing

        this.assertAdventurerDrawn(0, 15);
        this.assertAdventurerDrawn(1, 15);
        this.assertAdventurerDrawn(2, 15);
        this.assertAdventurerDrawn(3, 15);

        this.assertAdventurerDrawn(0, 18);
        this.assertAdventurerDrawn(1, 18);
        this.assertAdventurerDrawn(2, 18);
        this.assertAdventurerDrawn(3, 18);

        this.assertAdventurerDrawn(0, 10);
        this.assertAdventurerDrawn(1, 10);
        this.assertAdventurerDrawn(2, 10);
        this.assertAdventurerDrawn(3, 10);

        this.assertAdventurerDrawn(0, 23);
        this.assertAdventurerDrawn(1, 23);
        this.assertAdventurerDrawn(2, 23);
        this.assertAdventurerDrawn(3, 23);

        // assert monster drawing

        this.assertMonsterDrawn(0, 13);
        this.assertMonsterDrawn(1, 13);
        this.assertMonsterDrawn(2, 13);
        this.assertMonsterDrawn(3, 13);

        this.assertMonsterDrawn(0, 19);
        this.assertMonsterDrawn(1, 19);
        this.assertMonsterDrawn(2, 19);
        this.assertMonsterDrawn(3, 19);

        this.assertMonsterDrawn(0, 12);
        this.assertMonsterDrawn(1, 12);
        this.assertMonsterDrawn(2, 12);
        this.assertMonsterDrawn(3, 12);

        this.assertMonsterDrawn(0, 1);
        this.assertMonsterDrawn(1, 1);
        this.assertMonsterDrawn(2, 1);
        this.assertMonsterDrawn(3, 1);



        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }
}
