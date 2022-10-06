package de.unisaarland.cs.se.selab.systemtest.evaltomonster;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.SystemTestTemplate;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class PlaceBidOnMonsterAndLeave extends OurSystemTestFramework {

    public PlaceBidOnMonsterAndLeave() {
        super(PlaceBidOnMonsterAndLeave.class, false);
        currSockets.add(0);
        currSockets.add(1);
        currSockets.add(2);
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

    private void bidOnFoodNiceMonster() throws TimeoutException {

        // first player places all their bids.
        this.sendPlaceBid(0, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 3);
        bidPlacedAsserterHelper(BidType.MONSTER, 0, 3);

        // second player places their bids.
        this.sendPlaceBid(1, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 3);
        bidPlacedAsserterHelper(BidType.MONSTER, 1, 3);

        //third player places their bids
        this.sendPlaceBid(2, BidType.FOOD, 1);
        bidPlacedAsserterHelper(BidType.FOOD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlacedAsserterHelper(BidType.NICENESS, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 3);
        bidPlacedAsserterHelper(BidType.MONSTER, 2, 3);

    }

    @Override
    protected void simulateFirstBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(1);

        // assert Adv. drawing
        adventurerAsserterHelper(29);
        adventurerAsserterHelper(23);
        adventurerAsserterHelper(2);

        // assert monster drawing

        monsterAsserterHelper(23);
        monsterAsserterHelper(13);
        monsterAsserterHelper(9);

        // assert room drawing

        roomAsserterHelper(5);
        roomAsserterHelper(4);

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

        // third bid category (Monster)
        // slot 1:
        assertSelectMonster(0);
        assertActNow(0);
        sendLeave(0);
        currSockets.remove(0);
        playerLeftAsserterHelper(0);

        // slot 2:
        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 23); // evilness : 0 hunger : 0.
        monsterHiredAsserterHelper(23, 1);

        // slot 3:
        foodChangedAsserterHelper(-1, 2);
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 13); // evilness : 1 hunger : 1.
        foodChangedAsserterHelper(-1, 2);
        evilnessChangedAsserterHelper(1, 2);
        monsterHiredAsserterHelper(13, 2);

        //retrieve bids for slot 1

        bidRetrievedAsserterHelper(BidType.FOOD, 1);
        bidRetrievedAsserterHelper(BidType.FOOD, 2);

        // adventurer arrived (at dungeons)
        // evilness : p0 > p2 > p1
        // adventurer difficulty : 23 > 29 > 2
        adventurerArrivedAsserterHelper(2, 1);
        adventurerArrivedAsserterHelper(29, 2);

    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserterHelper(1);

        simulateFirstBiddingSeason();
        //simulateFirstBiddingSeason();

        // can ignore
        //this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
    }
}
