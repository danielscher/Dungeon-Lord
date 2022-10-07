package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.evalroom.BiddingFoodTunnelRoom;
import java.util.Set;

public class ActivateRoomWhileChoosingMonsterSecondSeason extends BiddingFoodTunnelRoom {
    public ActivateRoomWhileChoosingMonsterSecondSeason() {
        super(ActivateRoomWhileChoosingMonsterSecondSeason.class, false);
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(0, 1, 2);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(ActivateRoomWhileChoosingMonsterSecondSeason.class,
                "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
    }

    protected void simulateSecondBiddingSeason() throws TimeoutException {
        // assert Adv. drawing

        adventurerAsserter(0);
        adventurerAsserter(18);
        adventurerAsserter(11);

        // assert monster drawing

        monsterAsserter(7);
        monsterAsserter(22);
        monsterAsserter(1);

        // assert room drawing

        roomAsserter(8);
        roomAsserter(15);

        // assert bidding started

        this.assertBiddingStarted(0);
        this.assertBiddingStarted(1);
        this.assertBiddingStarted(2);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);

        // place bids
        bidsOfSecondSeasonFirstYear();

        //Eval bids
        evalBidsSecondSeason();

        //BidRetrieved
        bidRetrievedAsserterHelper(BidType.TUNNEL, 0);
        bidRetrievedAsserterHelper(BidType.ROOM, 0);
        bidRetrievedAsserterHelper(BidType.GOLD, 0);

        bidRetrievedAsserterHelper(BidType.TUNNEL, 2);
        bidRetrievedAsserterHelper(BidType.ROOM, 2);
        bidRetrievedAsserterHelper(BidType.GOLD, 2);

        // imp return
        impsChangedAsserterHelper(2, 0);
        goldChangedAsserterHelper(2, 0);
        impsChangedAsserterHelper(3, 2);
        goldChangedAsserterHelper(3, 2);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserterHelper(0, 2);
        adventurerArrivedAsserterHelper(18, 0);
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(1, BidType.GOLD, 1);
        bidPlacedAsserter(BidType.GOLD, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.FOOD, 3);
        bidPlacedAsserter(BidType.FOOD, 1, 3);

        this.sendPlaceBid(2, BidType.GOLD, 1);
        bidPlacedAsserter(BidType.GOLD, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.FOOD, 3);
        bidPlacedAsserter(BidType.FOOD, 2, 3);

        this.sendPlaceBid(0, BidType.GOLD, 1);
        bidPlacedAsserter(BidType.GOLD, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlacedAsserter(BidType.MONSTER, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.FOOD, 3);
        bidPlacedAsserter(BidType.FOOD, 0, 3);
    }

    protected void evalBidsSecondSeason() throws TimeoutException {

        //player 1 has 6 food, 2 tile(1, 0) (2, 0), 1 gold, 3 imps
        //player 2 has 6 food 3 tile, 4 gold, 3 imps
        //player 0 has 5 food 2 tile, 1 gold, 3 imps
        //FOOD
        goldChangedAsserter(-1, 1);
        foodChangedAsserter(2, 1);

        evilnessChangedAsserter(1, 2);
        foodChangedAsserter(3, 2);

        evilnessChangedAsserter(2, 0);
        foodChangedAsserter(3, 0);
        goldChangedAsserter(1, 0);

        //GOLD
        impsChangedAsserter(-2, 1);
        impsChangedAsserter(-3, 2);
        impsChangedAsserter(-2, 0);

        //BidType Monster
        assertSelectMonster(1);
        assertActNow(1);
        sendLeave(1);
        currSockets.remove((Integer) 1);
        playerLeftAsserterHelper(1);

        assertSelectMonster(2);
        assertActNow(2);
        sendActivateRoom(2, 4);
        assertActionFailed(2);
        assertActNow(2);

        sendHireMonster(2, 7);
        monsterHiredAsserterHelper(7, 2);

        foodChangedAsserterHelper(-1, 0);
        assertSelectMonster(0);
        assertActNow(0);

        sendActivateRoom(0, 4);
        assertActionFailed(0);
        assertActNow(0);

        sendHireMonster(0, 1);
        evilnessChangedAsserterHelper(3, 0);
        monsterHiredAsserterHelper(1, 0);
    }
}
