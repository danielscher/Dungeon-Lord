package de.unisaarland.cs.se.selab.systemtest.cambatphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingFourthSeason;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;


public class CombatAllPlayerEndTurn extends FrameworkuptoBiddingFourthSeason {

    @Override
    public String createConfig() {
        return Utils.loadResource(CombatAllPlayerEndTurn.class, "configuration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        regPhaseAssertions(config);

        nextYearAsserter(1);

        simulateFirstBiddingSeason();
        simulateSecondBiddingSeason();
        simulateThirdBiddingSeason();
        simulateFourthBiddingSeason();
        // start combat phase from player0 to player3

        //player0 after 2 years of combat  imprisoned 3 adv successful
        combatPlayerZeroRoundFive(0);
        combatPlayerZeroRoundSix(0);

        //player1 after 2 years of combat  imprisoned 3 adv successful??
        //combatPlayerOneRoundFive(1);
        //combatPlayerOneRoundSix(1);

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void combatPlayerZeroRoundFive(final int playerId) throws TimeoutException {
        nextRoundAsserter(1);
        assertSetBattleGround(playerId);
        assertActNow(playerId);
        sendBattleGround(playerId, 0, 0);
        assertBattleGroundSet(playerId, playerId, 0, 0);
        assertDefendYourself(playerId);
        sendEndTurn(playerId);
        assertActNow(playerId);
        assertAdventurerDamaged(playerId, 16, 2);
        assertAdventurerDamaged(playerId, 0, 2);
        assertAdventurerDamaged(playerId, 18, 2);
        assertTunnelConquered(playerId, 16, 0, 0);
        assertEvilnessChanged(playerId, -1, 0);
    }

    protected void combatPlayerZeroRoundSix(final int playerId) throws TimeoutException {
        // so the weird thing happened,
        assertNextRound(playerId, 2);
        assertNextRound(playerId, 2);
        assertSetBattleGround(playerId);
        assertActNow(playerId);

        sendBattleGround(playerId, 0, 1);
        assertBattleGroundSet(playerId, playerId, 0, 1);
        assertDefendYourself(playerId);
        sendEndTurn(playerId);
        assertActNow(playerId);
        // second round combat,
        assertAdventurerDamaged(playerId, 16, 1);
        assertAdventurerImprisoned(playerId, 16);
        assertAdventurerDamaged(playerId, 0, 1);
        assertAdventurerImprisoned(playerId, 0);
        assertAdventurerDamaged(playerId, 18, 2);
        assertAdventurerImprisoned(playerId, 18);
        // try player0 still
        //assertNextRound(playerId, 1);
        // assertActionFailed(0);
        // assertBattleGroundSet(playerId, playerId, 0, 0);
        assertNextRound(0, 1);
    }

    protected void combatPlayerOneRoundFive(final int playerId) throws TimeoutException {
        // the problem appears here
        // nextRoundAsserter(1);
        // assertSetBattleGround(playerId);
        // assertActNow(playerId);
        sendBattleGround(playerId, 0, 0);
        assertBattleGroundSet(playerId, playerId, 0, 0);
        assertDefendYourself(playerId);
        sendEndTurn(playerId);
        assertActNow(playerId);
        assertAdventurerDamaged(playerId, 16, 2);
        assertAdventurerDamaged(playerId, 0, 2);
        assertAdventurerDamaged(playerId, 18, 2);
        assertTunnelConquered(playerId, 16, 0, 0);
        assertEvilnessChanged(playerId, -1, 0);
    }

    protected void combatPlayerOneRoundSix(final int playerId) throws TimeoutException {
        // so the weird thing happened,
        assertNextRound(playerId, 2);
        assertNextRound(playerId, 6);
        assertSetBattleGround(playerId);
        assertActNow(playerId);

        sendBattleGround(playerId, 0, 1);
        assertBattleGroundSet(playerId, playerId, 0, 1);
        assertDefendYourself(playerId);
        sendEndTurn(playerId);
        assertActNow(playerId);
        // second round combat,
        assertAdventurerDamaged(playerId, 16, 1);
        assertAdventurerImprisoned(playerId, 16);
        assertAdventurerDamaged(playerId, 0, 1);
        assertAdventurerImprisoned(playerId, 0);
        assertAdventurerDamaged(playerId, 18, 2);
        assertAdventurerImprisoned(playerId, 18);
    }




}
