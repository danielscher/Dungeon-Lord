package de.unisaarland.cs.se.selab.systemtest.combatphasefirstyear;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.FrameworkuptoBiddingFourthSeason;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

public class CombatPhaseFirstRound extends FrameworkuptoBiddingFourthSeason {

    @Override
    public String createConfig() {
        return Utils.loadResource(CombatPhaseFirstRound.class, "configuration.json");
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

        simulaterfirstround();

        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

    protected void simulaterfirstround() throws TimeoutException {
        nextRoundAsserter(5);

        player0Defend();


    }

    protected void battelGroundChoosedAsserter(final int x, final int y, final int playerId)
            throws TimeoutException {
        assertBattleGroundSet(0, playerId, x, y);
        assertBattleGroundSet(1, playerId, x, y);
        assertBattleGroundSet(2, playerId, x, y);
        assertBattleGroundSet(3, playerId, x, y);
    }

    protected void monsterPlacedAsserter(final int monster, final int playerId)
            throws TimeoutException {
        assertMonsterPlaced(0, monster, playerId);
        assertMonsterPlaced(1, monster, playerId);
        assertMonsterPlaced(2, monster, playerId);
        assertMonsterPlaced(3, monster, playerId);
    }

    protected void adventurerDamagedAsserter(final int adventurer, final int amount)
            throws TimeoutException {
        assertAdventurerDamaged(0, adventurer, amount);
        assertAdventurerDamaged(1, adventurer, amount);
        assertAdventurerDamaged(2, adventurer, amount);
        assertAdventurerDamaged(3, adventurer, amount);
    }

    protected void player0Defend() throws TimeoutException {
        //choose battleground
        this.assertSetBattleGround(0);
        this.assertActNow(0);
        this.sendBattleGround(0, 0, 0);
        battelGroundChoosedAsserter(0, 0, 0);

        //defend begin
        this.assertDefendYourself(0);
        this.assertActNow(0);
        this.sendMonster(0, 20);
        monsterPlacedAsserter(20, 0);
        this.assertActNow(0);
        this.sendEndTurn(0);
        adventurerDamagedAsserter(16, 2);
        adventurerDamagedAsserter(0, 2);
        adventurerDamagedAsserter(18, 2);

    }



}
