package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.BidType;
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

        regPhaseAssertions(config);

        nextYearAsserter(1);

        nextRoundAsserter(1);

        // assert Adv. drawing

        adventurerAsserter(15);
        adventurerAsserter(18);
        adventurerAsserter(10);
        adventurerAsserter(23);


        // assert monster drawing

        monsterAsserter(13);
        monsterAsserter(19);
        monsterAsserter(12);

        // assert room drawing

        roomAsserter(1);
        roomAsserter(11);

        // assert bidding started

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);
        this.assertBiddingStarted(3);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);

        // place bids

        this.sendPlaceBid(0, BidType.FOOD, 1);
        this.sendPlaceBid(0, BidType.NICENESS, 2);
        this.sendPlaceBid(0, BidType.IMPS, 3);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        this.sendPlaceBid(1, BidType.NICENESS, 2);
        this.sendPlaceBid(1, BidType.IMPS, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        this.sendPlaceBid(2, BidType.NICENESS, 2);
        this.sendPlaceBid(2, BidType.IMPS, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        this.sendPlaceBid(3, BidType.NICENESS, 2);
        this.sendPlaceBid(3, BidType.IMPS, 3);

        // assert placing bids

        this.assertBidPlaced(0, BidType.FOOD, 0, 1);
        this.assertBidPlaced(1, BidType.FOOD, 0, 1);
        this.assertBidPlaced(2, BidType.FOOD, 0, 1);
        this.assertBidPlaced(3, BidType.FOOD, 0, 1);

        this.assertBidPlaced(0, BidType.FOOD, 1, 2);
        this.assertBidPlaced(1, BidType.FOOD, 1, 2);
        this.assertBidPlaced(2, BidType.FOOD, 1, 2);
        this.assertBidPlaced(3, BidType.FOOD, 1, 2);

        this.assertBidPlaced(0, BidType.FOOD, 2, 3);
        this.assertBidPlaced(1, BidType.FOOD, 2, 3);
        this.assertBidPlaced(2, BidType.FOOD, 2, 3);
        this.assertBidPlaced(3, BidType.FOOD, 2, 3);

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    private void regPhaseAssertions(final String config) throws TimeoutException {
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
    }

    private void nextYearAsserter(final int year) throws TimeoutException {
        this.assertNextYear(0, year);
        this.assertNextYear(1, year);
        this.assertNextYear(2, year);
        this.assertNextYear(3, year);
    }

    private void nextRoundAsserter(final int round) throws TimeoutException {
        this.assertNextRound(0, round);
        this.assertNextRound(1, round);
        this.assertNextRound(2, round);
        this.assertNextRound(3, round);
    }

    private void adventurerAsserter(final int advId) throws TimeoutException {
        this.assertAdventurerDrawn(0, advId);
        this.assertAdventurerDrawn(1, advId);
        this.assertAdventurerDrawn(2, advId);
        this.assertAdventurerDrawn(3, advId);
    }

    private void monsterAsserter(final int monsterId) throws TimeoutException {
        this.assertMonsterDrawn(0, monsterId);
        this.assertMonsterDrawn(1, monsterId);
        this.assertMonsterDrawn(2, monsterId);
        this.assertMonsterDrawn(3, monsterId);

    }

    private void roomAsserter(final int monsterId) throws TimeoutException {
        this.assertRoomDrawn(0, monsterId);
        this.assertRoomDrawn(1, monsterId);
        this.assertRoomDrawn(2, monsterId);
        this.assertRoomDrawn(3, monsterId);
    }

}
