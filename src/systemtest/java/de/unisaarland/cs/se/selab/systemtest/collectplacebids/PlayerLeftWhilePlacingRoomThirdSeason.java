package de.unisaarland.cs.se.selab.systemtest.collectplacebids;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingThirdSeason;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class PlayerLeftWhilePlacingRoomThirdSeason extends FrameworkuptoBiddingSecondSeason {
    protected PlayerLeftWhilePlacingRoomThirdSeason(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
    }

    public PlayerLeftWhilePlacingRoomThirdSeason() {
        super(PlayerLeftWhilePlacingRoomThirdSeason.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(FrameworkuptoBiddingThirdSeason.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserterHelper(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();
        simulateThirdBiddingSeason();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void simulateThirdBiddingSeason() throws TimeoutException {
        nextRoundAsserterHelper(3);

        // assert Adv. drawing

        adventurerAsserterHelper(9);
        adventurerAsserterHelper(15);
        adventurerAsserterHelper(26);
        adventurerAsserterHelper(16);

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
        this.assertBiddingStarted(3);

        // assert act now (for requesting bids)

        this.assertActNow(0);
        this.assertActNow(1);
        this.assertActNow(2);
        this.assertActNow(3);

        // place bids
        bidsOfThirdSeasonFirstYear();

        //Eval bids
        evalBidsThirdSeason();

        //BidRetrieved
        bidRetrievedAsserterHelper(BidType.GOLD, 0);
        bidRetrievedAsserterHelper(BidType.TRAP, 0);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 0);

        bidRetrievedAsserterHelper(BidType.GOLD, 1);
        bidRetrievedAsserterHelper(BidType.TRAP, 1);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 1);

        bidRetrievedAsserterHelper(BidType.GOLD, 3);
        bidRetrievedAsserterHelper(BidType.TRAP, 3);
        bidRetrievedAsserterHelper(BidType.TUNNEL, 3);

        // imp return
        impsChangedAsserterHelper(3, 0);
        impsChangedAsserterHelper(3, 3);

        // adventurer arrived (at dungeons)
        adventurerArrivedAsserterHelper(16, 0);
        adventurerArrivedAsserterHelper(9, 1);
        adventurerArrivedAsserterHelper(26, 3);
    }

    protected void bidsOfThirdSeasonFirstYear() throws TimeoutException {

        this.sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 2, 1);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 2, 2);
        assertActNow(2);

        this.sendPlaceBid(2, BidType.ROOM, 3);
        bidPlacedAsserterHelper(BidType.ROOM, 2, 3);

        this.sendPlaceBid(3, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 3, 1);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 3, 2);
        assertActNow(3);

        this.sendPlaceBid(3, BidType.ROOM, 3);
        bidPlacedAsserterHelper(BidType.ROOM, 3, 3);

        this.sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 0, 2);
        assertActNow(0);

        this.sendPlaceBid(0, BidType.ROOM, 3);
        bidPlacedAsserterHelper(BidType.ROOM, 0, 3);

        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlacedAsserterHelper(BidType.TUNNEL, 1, 1);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.MONSTER, 2);
        bidPlacedAsserterHelper(BidType.MONSTER, 1, 2);
        assertActNow(1);

        this.sendPlaceBid(1, BidType.ROOM, 3);
        bidPlacedAsserterHelper(BidType.ROOM, 1, 3);
    }

    protected void evalBidsThirdSeason() throws TimeoutException {

        //BidType Tunnel
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        impsChangedAsserterHelper(-1, 2);
        tunnelDugAsserter(2, 0, 1);
        assertActNow(2);
        sendDigTunnel(2, 0, 2);
        impsChangedAsserterHelper(-1, 2);
        tunnelDugAsserter(2, 0, 2);

        assertDigTunnel(3);
        assertActNow(3);
        sendDigTunnel(3, 1, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 1, 0);
        assertActNow(3);
        sendDigTunnel(3, 2, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 2, 0);
        assertActNow(3);
        sendDigTunnel(3, 3, 0);
        impsChangedAsserterHelper(-1, 3);
        tunnelDugAsserter(3, 3, 0);

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
        tunnelDugAsserter(0, 0, 3);


        //BidType Monster
        assertSelectMonster(2);
        assertActNow(2);
        sendLeave(2);
        currSockets.remove((Integer) 2);
        playerLeftAsserterHelper(2);

        assertSelectMonster(3);
        assertActNow(3);
        sendHireMonster(3, 3);
        foodChangedAsserterHelper(-2, 3);
        evilnessChangedAsserterHelper(1, 3);
        monsterHiredAsserterHelper(3, 3);

        foodChangedAsserterHelper(-1, 0);
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 20);
        foodChangedAsserterHelper(-3, 0);
        monsterHiredAsserterHelper(20, 0);


        //BidType Room

        goldChangedAsserterHelper(-1, 3);
        assertPlaceRoom(3);
        assertActNow(3);
        sendEndTurn(3);

        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 3, 10);
        roomBuiltAsserter(0, 10, 0, 3);
    }
}
