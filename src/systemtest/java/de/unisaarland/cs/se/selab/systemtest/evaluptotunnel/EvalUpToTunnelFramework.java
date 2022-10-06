package de.unisaarland.cs.se.selab.systemtest.evaluptotunnel;


import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Register 2 Players and Leave
 */
public class EvalUpToTunnelFramework extends SystemTest {

    private final List<Integer> currSockets = new ArrayList<>();

    protected EvalUpToTunnelFramework(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    EvalUpToTunnelFramework() {
        super(EvalUpToTunnelFramework.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(EvalUpToTunnelFramework.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        // here i maintain a socketset to store the player who is playing
        currSockets.add(0);
        currSockets.add(1);
        currSockets.add(2);
        currSockets.add(3);
        return Set.of(0, 1, 2, 3);
    }

    /*
    helper method for asserting player
    NOTE: requires socketNum == playerId
     */

    protected void assertPlayerHelper(final int[] ids) throws TimeoutException {
        for (final Integer id : ids) {
            for (final Integer id2 : ids) {
                assertPlayer(id2, String.valueOf(id), id);
            }
        }
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserterHelper(1);

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

    protected void impsChangedAsserterHelper(final int amount, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertImpsChanged(currS, amount, playerId);

        }
    }

    protected void foodChangedAsserterHelper(final int amount, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertFoodChanged(currS, amount, playerId);
        }
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

        gameStartedAsserterHelper();

        this.assertPlayerHelper(new int[]{0, 1, 2, 3});
    }

    protected void gameStartedAsserterHelper() throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertGameStarted(currS);
        }
    }

    protected void actNowAsserterHelper() throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertActNow(currS);
        }
    }

    protected void biddingStartedAsserterHelper() throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertBiddingStarted(currS);
        }
    }

    protected void nextYearAsserterHelper(final int year)
            throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertNextYear(currS, year);
        }
    }

    protected void nextRoundAsserterHelper(final int round)
            throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertNextRound(currS, round);
        }
    }


    protected void adventurerAsserterHelper(final int advId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertAdventurerDrawn(currS, advId);
        }
    }


    protected void monsterAsserterHelper(final int monsterId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertMonsterDrawn(currS, monsterId);
        }
    }

    protected void roomAsserterHelper(final int roomId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertRoomDrawn(currS, roomId);
        }
    }

    protected void bidPlacedAsserterHelper(final BidType bid, final int playerId,
            final int slot) throws TimeoutException {
        for (final int currS : currSockets) {
            this.assertBidPlaced(currS, bid, playerId, slot);
        }
    }

    protected void goldChangedAsserterHelper(final int amount, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertGoldChanged(currS, amount, playerId);
        }
    }


    protected void evilnessChangedAsserterHelper(final int amount, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertEvilnessChanged(currS, amount, playerId);
        }
    }


    protected void bidRetrievedAsserterHelper(final BidType bidType, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertBidRetrieved(currS, bidType, playerId);
        }
    }

    protected void adventurerArrivedAsserterHelper(final int advId, final int playerId)
            throws TimeoutException {
        for (final int currS : currSockets) {
            assertAdventurerArrived(currS, advId, playerId);
        }
    }

    protected void adventurerDrawingFirstYearFirstSeason()
            throws TimeoutException {
        // assert Adv. drawing
        adventurerAsserterHelper(29);
        adventurerAsserterHelper(23);
        adventurerAsserterHelper(2);
        adventurerAsserterHelper(0);
    }

    protected void monsterDrawingFirstYearFirstSeason()
            throws TimeoutException {
        // assert monster drawing
        monsterAsserterHelper(23);
        monsterAsserterHelper(13);
        monsterAsserterHelper(9);
    }

    protected void roomDrawingFirstYearFirstSeason()
            throws TimeoutException {
        // assert room drawing
        roomAsserterHelper(5);
        roomAsserterHelper(4);
    }

    protected void assertEntityDrawingFirstYearFirstSeason()
            throws TimeoutException {
        adventurerDrawingFirstYearFirstSeason();
        monsterDrawingFirstYearFirstSeason();
        roomDrawingFirstYearFirstSeason();
    }


    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(1);

        assertEntityDrawingFirstYearFirstSeason();

        // assert bidding started

        this.biddingStartedAsserterHelper();

        // assert act now (for requesting bids)

        this.actNowAsserterHelper();

        // place bids
        bidsOfFirstSeasonFirstYear();

        // assert placing bids

        // first bid category (food)
        goldChangedAsserterHelper(-1, 0);
        foodChangedAsserterHelper(2, 0);
        evilnessChangedAsserterHelper(1, 1);
        foodChangedAsserterHelper(3, 1);
        evilnessChangedAsserterHelper(2, 2);
        foodChangedAsserterHelper(3, 2);
        goldChangedAsserterHelper(1, 2);

        // second bid category (niceness)
        evilnessChangedAsserterHelper(-1, 0);
        evilnessChangedAsserterHelper(-2, 1);
        goldChangedAsserterHelper(-1, 2);
        evilnessChangedAsserterHelper(-2, 2);

        // imp category
        foodChangedAsserterHelper(-1, 0);
        impsChangedAsserterHelper(1, 0);
        foodChangedAsserterHelper(-2, 1);
        impsChangedAsserterHelper(2, 1);
        foodChangedAsserterHelper(-1, 2);
        goldChangedAsserterHelper(-1, 2);
        impsChangedAsserterHelper(2, 2);

        //retrieve the slot 1
        bidRetrievedAsserterHelper(BidType.FOOD, 0);
        bidRetrievedAsserterHelper(BidType.FOOD, 1);
        bidRetrievedAsserterHelper(BidType.FOOD, 2);
        bidRetrievedAsserterHelper(BidType.FOOD, 3);

        // adventurer arrived (at dungeons)
        // TODO change this to respect order of evilness
        adventurerArrivedAsserterHelper(0, 0);
        adventurerArrivedAsserterHelper(2, 1);
        adventurerArrivedAsserterHelper(29, 2);
        adventurerArrivedAsserterHelper(23, 3);
    }

    protected void bidsOfFirstSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 0, 3);

        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 1, 3);

        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 2, 3);

        this.sendPlaceBid(3, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.IMPS, 3);
        bidPlacedAsserterHelper(BidType.IMPS, 3, 3);
    }
}
