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

        combatPlayerZeroRoundFive();
        combatPlayerZeroRoundSix();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void combatPlayerZeroRoundFive() throws TimeoutException {
        nextRoundAsserter(5);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 0, 0);
        assertBattleGroundSet(0, 0, 0, 0);
        assertDefendYourself(0);
        sendEndTurn(0);
        assertActNow(0);
        assertAdventurerDamaged(0, 16, 2);
        assertAdventurerDamaged(0, 0, 2);
        assertAdventurerDamaged(0, 18, 2);
        assertTunnelConquered(0, 16, 0, 0);
    }

    protected void combatPlayerZeroRoundSix() throws TimeoutException {
        // so
        assertNextRound(0, 2);
        assertNextRound(0, 6);
        assertSetBattleGround(0);
        assertActNow(0);

        sendBattleGround(0, 0, 1);
        assertBattleGroundSet(0, 0, 0, 1);
        assertDefendYourself(0);
        sendEndTurn(0);
        assertActNow(0);
        // second round combat,
        assertAdventurerDamaged(0, 16, 1);
        assertAdventurerImprisoned(0, 16);
        assertAdventurerDamaged(0, 0, 1);
        assertAdventurerImprisoned(0, 0);
        assertAdventurerDamaged(0, 18, 2);
        assertAdventurerImprisoned(0, 18);

        assertActionFailed(0);
        assertAdventurerImprisoned(1, 0);
        assertAdventurerImprisoned(2, 0);
        assertAdventurerImprisoned(3, 0);

        assertAdventurerImprisoned(0, 16);
        assertAdventurerImprisoned(0, 18);


    }


}
