package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class OurSystemTestFramework extends SystemTest {

    OurSystemTestFramework(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    OurSystemTestFramework() {
        super(OurSystemTestFramework.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(OurSystemTestFramework.class, "configuration.json");
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

        simulateFirstBiddingSeason();

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void impsChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertImpsChanged(0, amount, playerId);
        assertImpsChanged(1, amount, playerId);
        assertImpsChanged(2, amount, playerId);
        assertImpsChanged(3, amount, playerId);
    }

    protected void foodChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertFoodChanged(0, amount, playerId);
        assertFoodChanged(1, amount, playerId);
        assertFoodChanged(2, amount, playerId);
        assertFoodChanged(3, amount, playerId);

    }

    protected void regPhaseAssertions(final String config) throws TimeoutException {
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

    protected void nextYearAsserter(final int year) throws TimeoutException {
        this.assertNextYear(0, year);
        this.assertNextYear(1, year);
        this.assertNextYear(2, year);
        this.assertNextYear(3, year);
    }

    protected void nextRoundAsserter(final int round) throws TimeoutException {
        this.assertNextRound(0, round);
        this.assertNextRound(1, round);
        this.assertNextRound(2, round);
        this.assertNextRound(3, round);
    }

    protected void adventurerAsserter(final int advId) throws TimeoutException {
        this.assertAdventurerDrawn(0, advId);
        this.assertAdventurerDrawn(1, advId);
        this.assertAdventurerDrawn(2, advId);
        this.assertAdventurerDrawn(3, advId);
    }

    protected void monsterAsserter(final int monsterId) throws TimeoutException {
        this.assertMonsterDrawn(0, monsterId);
        this.assertMonsterDrawn(1, monsterId);
        this.assertMonsterDrawn(2, monsterId);
        this.assertMonsterDrawn(3, monsterId);

    }

    protected void roomAsserter(final int roomId) throws TimeoutException {
        this.assertRoomDrawn(0, roomId);
        this.assertRoomDrawn(1, roomId);
        this.assertRoomDrawn(2, roomId);
        this.assertRoomDrawn(3, roomId);
    }

    protected void bidPlacedAsserter(final BidType bid, final int playerId, final int slot)
            throws TimeoutException {
        this.assertBidPlaced(0, bid, playerId, slot);
        this.assertBidPlaced(1, bid, playerId, slot);
        this.assertBidPlaced(2, bid, playerId, slot);
        this.assertBidPlaced(3, bid, playerId, slot);
    }

    protected void goldChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertGoldChanged(0, amount, playerId);
        assertGoldChanged(1, amount, playerId);
        assertGoldChanged(2, amount, playerId);
        assertGoldChanged(3, amount, playerId);
    }

    protected void evilnessChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertEvilnessChanged(0, amount, playerId);
        assertEvilnessChanged(1, amount, playerId);
        assertEvilnessChanged(2, amount, playerId);
        assertEvilnessChanged(3, amount, playerId);
    }

    protected void adventurerArrivedAsserter(final int advId, final int playerId)
            throws TimeoutException {
        assertAdventurerArrived(0, advId, playerId);
        assertAdventurerArrived(1, advId, playerId);
        assertAdventurerArrived(2, advId, playerId);
        assertAdventurerArrived(3, advId, playerId);
    }

    protected void simulateFirstBiddingSeason() throws TimeoutException {
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
        bidsOfFirstSeasonFirstYear();

        // assert placing bids

        // first bid category (food)
        goldChangedAsserter(-1, 0);
        foodChangedAsserter(2, 0);
        evilnessChangedAsserter(1, 1);
        foodChangedAsserter(3, 1);
        evilnessChangedAsserter(2, 2);
        foodChangedAsserter(3, 2);
        goldChangedAsserter(1, 2);

        // second bid category (niceness)
        evilnessChangedAsserter(-1, 0);
        evilnessChangedAsserter(-2, 1);
        goldChangedAsserter(-1, 2);
        evilnessChangedAsserter(-2, 2);

        // imp category
        foodChangedAsserter(-1, 0);
        impsChangedAsserter(1, 0);
        foodChangedAsserter(-2, 1);
        impsChangedAsserter(2, 1);
        foodChangedAsserter(-1, 2);
        goldChangedAsserter(-1, 2);
        impsChangedAsserter(2, 2);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserter(24, 0);
        adventurerArrivedAsserter(5, 1);
        adventurerArrivedAsserter(21, 2);
        adventurerArrivedAsserter(14, 3);
    }

    protected void bidsOfFirstSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 0, 2);

        this.sendPlaceBid(0, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 0, 3);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);

        this.sendPlaceBid(1, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 2, 2);

        this.sendPlaceBid(2, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 2, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 3, 1);

        this.sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 3, 2);

        this.sendPlaceBid(3, BidType.IMPS, 3);
        bidPlacedAsserter(BidType.IMPS, 3, 3);
    }
}
