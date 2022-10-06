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
        //player1Defend();
        //player2Defend();
        //player3Defend();

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

    protected void trapPlacedAsserter(final int trap, final int playerId)
            throws TimeoutException {
        assertTrapPlaced(0, playerId, trap);
        assertTrapPlaced(1, playerId, trap);
        assertTrapPlaced(2, playerId, trap);
        assertTrapPlaced(3, playerId, trap);
    }

    protected void adventurerDamagedAsserter(final int adventurer, final int amount)
            throws TimeoutException {
        assertAdventurerDamaged(0, adventurer, amount);
        assertAdventurerDamaged(1, adventurer, amount);
        assertAdventurerDamaged(2, adventurer, amount);
        assertAdventurerDamaged(3, adventurer, amount);
    }

    protected void imprisonAsserter(final int adventurer)
            throws TimeoutException {
        assertAdventurerImprisoned(0, adventurer);
        assertAdventurerImprisoned(1, adventurer);
        assertAdventurerImprisoned(2, adventurer);
        assertAdventurerImprisoned(3, adventurer);
    }

    protected void player0Defend() throws TimeoutException {
        //choose battleground
        this.assertSetBattleGround(0);
        this.assertActNow(0);
        this.sendBattleGround(0, 0, 0);
        battelGroundChoosedAsserter(0, 0, 0);

        //defend begin and set monster
        this.assertDefendYourself(0);
        this.assertActNow(0);
        this.sendMonster(0, 20);
        monsterPlacedAsserter(20, 0);
        // m20 is MUllI,damage=2
        this.assertActNow(0);
        this.sendEndTurn(0);

        //end turn 16, 0, 18
        //first adventurer
        adventurerDamagedAsserter(16, 2);
        adventurerDamagedAsserter(16, 2);
        imprisonAsserter(18);
        //second adventurer
        // 16 and 0 are warrior HP=3
        adventurerDamagedAsserter(0, 2);
        //third adventurer
        adventurerDamagedAsserter(18, 2);
    }

    protected void player1Defend() throws TimeoutException {
        //choose battleground
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 0, 0);
        battelGroundChoosedAsserter(0, 0, 1);

        //defend begin and set trap
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendTrap(1, 26);
        trapPlacedAsserter(26, 1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        //end turn


    }

    protected void player2Defend() throws TimeoutException {
        //choose battleground
        this.assertSetBattleGround(2);
        this.assertActNow(2);
        this.sendBattleGround(2, 0, 0);
        battelGroundChoosedAsserter(0, 0, 2);

        //defend begin and set trap and monster
        this.assertDefendYourself(2);
        this.assertActNow(2);
        this.sendTrap(2, 6);
        trapPlacedAsserter(6, 2);
        this.assertActNow(2);
        this.sendMonster(2, 14);
        monsterPlacedAsserter(14, 2);
        this.assertActNow(2);
        this.sendEndTurn(2);

    }

    protected void player3Defend() throws TimeoutException {
        //choose battleground
        this.assertSetBattleGround(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 0);
        battelGroundChoosedAsserter(0, 0, 3);

        //defend begin and set trap
        this.assertDefendYourself(3);
        this.assertActNow(3);
        this.sendTrap(3, 19);
        trapPlacedAsserter(19, 3);
        this.assertActNow(3);
        this.sendMonster(3, 3);
        monsterPlacedAsserter(3, 3);
        this.assertActNow(3);
        this.sendEndTurn(3);

    }
}
