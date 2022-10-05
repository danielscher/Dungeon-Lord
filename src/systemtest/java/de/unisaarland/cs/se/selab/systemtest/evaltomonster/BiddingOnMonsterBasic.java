package de.unisaarland.cs.se.selab.systemtest.evaltomonster;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.SystemTestTemplate;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class BiddingOnMonsterBasic extends OurSystemTestFramework {

    public BiddingOnMonsterBasic() {
        super(SystemTestTemplate.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(SystemTestTemplate.class, "configuration.json");
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(0, 1, 2);
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);


        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        //simulateFirstBiddingSeason();

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
    }


    @Override
    protected void regPhaseAssertions(final String config) throws TimeoutException {
        this.sendRegister(0, "0");
        this.assertConfig(0, config);
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);

        this.sendStartGame(0);

        this.assertGameStarted(0);
        this.assertGameStarted(1);
        this.assertGameStarted(2);

        this.assertPlayerHelper(new int[]{0, 1, 2});
    }

    @Override
    protected void nextRoundAsserter(final int round) throws TimeoutException {
        this.assertNextRound(0, round);
        this.assertNextRound(1, round);
        this.assertNextRound(2, round);
    }

    @Override
    protected void goldChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertGoldChanged(0, amount, playerId);
        assertGoldChanged(1, amount, playerId);
        assertGoldChanged(2, amount, playerId);
    }

    @Override
    protected void bidPlacedAsserter(final BidType bid, final int playerId, final int slot)
            throws TimeoutException {
        this.assertBidPlaced(0, bid, playerId, slot);
        this.assertBidPlaced(1, bid, playerId, slot);
        this.assertBidPlaced(2, bid, playerId, slot);
    }

    @Override
    protected void foodChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertFoodChanged(0, amount, playerId);
        assertFoodChanged(1, amount, playerId);
        assertFoodChanged(2, amount, playerId);
    }

    @Override
    protected void evilnessChangedAsserter(final int amount, final int playerId)
            throws TimeoutException {
        assertEvilnessChanged(0, amount, playerId);
        assertEvilnessChanged(1, amount, playerId);
        assertEvilnessChanged(2, amount, playerId);
    }

    @Override
    protected void adventurerArrivedAsserter(final int advId, final int playerId)
            throws TimeoutException {
        assertAdventurerArrived(0, advId, playerId);
        assertAdventurerArrived(1, advId, playerId);
        assertAdventurerArrived(2, advId, playerId);
    }

    @Override
    protected void monsterAsserter(final int monsterId) throws TimeoutException {
        this.assertMonsterDrawn(0, monsterId);
        this.assertMonsterDrawn(1, monsterId);
        this.assertMonsterDrawn(2, monsterId);
    }

    @Override
    protected void adventurerAsserter(final int advId) throws TimeoutException {
        this.assertAdventurerDrawn(0, advId);
        this.assertAdventurerDrawn(1, advId);
        this.assertAdventurerDrawn(2, advId);
    }

    @Override
    protected void roomAsserter(final int roomId) throws TimeoutException {
        this.assertRoomDrawn(0, roomId);
        this.assertRoomDrawn(1, roomId);
        this.assertRoomDrawn(2, roomId);
    }


    @Override
    protected void nextYearAsserter(final int year) throws TimeoutException {
        this.assertNextYear(0, year);
        this.assertNextYear(1, year);
        this.assertNextYear(2, year);
    }

    @Override
    protected void bidRetrievedAsserter(final BidType bidType, final int playerId)
            throws TimeoutException {
        assertBidRetrieved(0, bidType, playerId);
        assertBidRetrieved(1, bidType, playerId);
        assertBidRetrieved(2, bidType, playerId);
    }

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserter(1);

        // assert Adv. drawing
        adventurerAsserter(29);
        adventurerAsserter(23);
        adventurerAsserter(2);

        // assert monster drawing

        monsterAsserter(23);
        monsterAsserter(13);
        monsterAsserter(9);

        // assert room drawing

        roomAsserter(5);
        roomAsserter(4);

        // assert bidding started
        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        // assert act now (for requesting bids)
        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);

        // place bids
        bidOnFoodNiceMonster();

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

        // third bid category (Monster)
        // slot 1:
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 9); // evilness : 9 hunger : 0.
        evilnessChangedAsserter(3, 0);
        monsterHiredAsserter(9, 0);

        // slot 2:
        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 23); // evilness : 0 hunger : 0.
        monsterHiredAsserter(23, 1);

        // slot 3:
        foodChangedAsserter(-1, 2); //slot cost
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 13); // evilness : 1 hunger : 1.
        foodChangedAsserter(-1, 2);
        evilnessChangedAsserter(1, 2);
        monsterHiredAsserter(13, 2);

        //retrive bids for slot 1
        bidRetrievedAsserter(BidType.FOOD, 0);
        bidRetrievedAsserter(BidType.FOOD, 1);
        bidRetrievedAsserter(BidType.FOOD, 2);

        // adventurer arrived (at dungeons)
        // evilness : p0 > p2 > p1
        // adventurer difficulty : 23 > 29 > 2
        adventurerArrivedAsserter(2, 1);
        adventurerArrivedAsserter(29, 2);
        adventurerArrivedAsserter(23, 0);
    }


    private void bidOnFoodNiceMonster() throws TimeoutException {

        // first player places all their bids.
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 3);
        bidPlacedAsserter(BidType.MONSTER, 0, 3);

        // second player places their bids.
        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 3);
        bidPlacedAsserter(BidType.MONSTER, 1, 3);

        //third player places their bids
        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserter(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserter(BidType.NICENESS, 2, 2);
        assertActNow(2);

        // paying for slot and then feeding monster
        this.sendPlaceBid(2, BidType.MONSTER, 3);
        bidPlacedAsserter(BidType.MONSTER, 2, 3);


    }

    private void monsterHiredAsserter(final int monsterId, final int playerId)
            throws TimeoutException {
        assertMonsterHired(0, monsterId, playerId);
        assertMonsterHired(1, monsterId, playerId);
        assertMonsterHired(2, monsterId, playerId);
    }

}

