package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.evalroom.BiddingOnRoomBasic;

public class ActivateRoomWhileChoosingMonsterSecondSeason extends BiddingOnRoomBasic {
    public ActivateRoomWhileChoosingMonsterSecondSeason() {
        super(ActivateRoomWhileChoosingMonsterSecondSeason.class, false);
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
        nextRoundAsserterHelper(2);

        // assert Adv. drawing

        adventurerAsserterHelper(9);
        adventurerAsserterHelper(15);
        adventurerAsserterHelper(26);

        // assert monster drawing

        monsterAsserterHelper(14);
        monsterAsserterHelper(3);
        monsterAsserterHelper(20);

        // assert room drawing

        roomAsserterHelper(0);
        roomAsserterHelper(10);

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
        bidRetrievedAsserterHelper(BidType.NICENESS, 0);
        bidRetrievedAsserterHelper(BidType.ROOM, 0);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 0);

        bidRetrievedAsserterHelper(BidType.NICENESS, 2);
        bidRetrievedAsserterHelper(BidType.ROOM, 2);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 2);

        // imp return
        impsChangedAsserterHelper(3, 0);
        impsChangedAsserterHelper(3, 2);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserterHelper(16, 0);
        adventurerArrivedAsserterHelper(9, 2);
    }

    protected void bidsOfSecondSeasonFirstYear() throws TimeoutException {
        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.FOOD, 3);
        bidPlacedAsserterHelper(BidType.FOOD, 1, 3);

        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.FOOD, 3);
        bidPlacedAsserterHelper(BidType.FOOD, 2, 3);

        this.sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.FOOD, 3);
        bidPlacedAsserterHelper(BidType.FOOD, 0, 3);


    }

    protected void evalBidsSecondSeason() throws TimeoutException {

        //player 1 has 6 food, 2 gold, 3 imps
        //player 2 has 6 food 3 gold, 3 imps
        //player 0 has 5 food 1 gold, 3 imps
        //FOOD
        goldChangedAsserter(-1, 1);
        foodChangedAsserter(2, 1);
        evilnessChangedAsserter(1, 2);
        foodChangedAsserter(3, 2);
        evilnessChangedAsserter(2, 0);
        foodChangedAsserter(3, 0);
        goldChangedAsserter(1, 0);


        //BidType Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChangedAsserterHelper(-1, 1); //2 imps left
        tunnelDugAsserter(1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        impsChangedAsserterHelper(-1, 1); //1 imp left
        tunnelDugAsserter(1, 0, 2);

        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 1, 0);
        impsChangedAsserterHelper(-1, 2);
        tunnelDugAsserter(2, 1, 0);
        assertActNow(2);
        sendDigTunnel(2, 2, 0);
        impsChangedAsserterHelper(-1, 2);
        tunnelDugAsserter(2, 2, 0);
        assertActNow(2);
        sendDigTunnel(2, 3, 0);
        impsChangedAsserterHelper(-1, 2);
        tunnelDugAsserter(2, 3, 0);

        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 1);
        assertActNow(0);
        sendDigTunnel(0, 0, 2);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 2);
        assertActNow(0);
        sendDigTunnel(0, 0, 3);
        impsChangedAsserterHelper(-1, 0);
        tunnelDugAsserter(0, 0, 3); //0 imp left


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

        sendHireMonster(2, 14);
        foodChangedAsserterHelper(-1, 2);
        evilnessChangedAsserterHelper(1, 2);
        monsterHiredAsserterHelper(14, 2);


        foodChangedAsserterHelper(-1, 0);
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 20);
        foodChangedAsserterHelper(-3, 0);
        monsterHiredAsserterHelper(20, 0);
    }
}
