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
        player0Defend();
        player1Defend();
        player2Defend();
        player3Defend();

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

    protected void conqueredAsserter(final int adventurer, final int x, final int y)
            throws TimeoutException {
        assertTunnelConquered(0, adventurer, x, y);
        assertTunnelConquered(1, adventurer, x, y);
        assertTunnelConquered(2, adventurer, x, y);
        assertTunnelConquered(3, adventurer, x, y);
    }

    protected void healedAdventurerAsserter(final int amount, final int priest, final int target)
            throws TimeoutException {
        assertAdventurerHealed(0, amount, priest, target);
        assertAdventurerHealed(1, amount, priest, target);
        assertAdventurerHealed(2, amount, priest, target);
        assertAdventurerHealed(3, amount, priest, target);
    }

    protected void fledAdventurerAsserter(final int adventuruer)
            throws TimeoutException {
        assertAdventurerFled(0, adventuruer);
        assertAdventurerFled(1, adventuruer);
        assertAdventurerFled(2, adventuruer);
        assertAdventurerFled(3, adventuruer);

    }

    protected void player0Defend() throws TimeoutException {
        //choose battleground
        nextRoundAsserter(1);
        this.assertSetBattleGround(0);
        this.assertActNow(0);
        this.sendBattleGround(0, 0, 0);
        battelGroundChoosedAsserter(0, 0, 0);

        //defend begin and set monster
        this.assertDefendYourself(0);
        this.assertActNow(0);
        //Monster MULTI, damage=2
        this.sendMonster(0, 20);
        monsterPlacedAsserter(20, 0);
        // m20 is MUllI,damage=2
        this.assertActNow(0);
        this.sendEndTurn(0);

        //end turn 16, 0, 18
        //first adventurer, Hp=3, charge=true
        //second adventurer, Hp=3, charge=true
        //third adventurer, Hp=4, thief
        adventurerDamagedAsserter(16, 2); //monster
        adventurerDamagedAsserter(0, 2);  //monster
        adventurerDamagedAsserter(18, 2);  //monster
        adventurerDamagedAsserter(16, 1);  //fatig
        imprisonAsserter(16);
        adventurerDamagedAsserter(0, 1);   //fatig
        imprisonAsserter(0);
        adventurerDamagedAsserter(18, 2);  //fatig
        imprisonAsserter(18);
        //already end combat phase for player0
    }

    protected void player1Defend() throws TimeoutException {
        //choose battleground
        nextRoundAsserter(1);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 0, 0);
        battelGroundChoosedAsserter(0, 0, 1);

        //defend begin and set trap
        this.assertDefendYourself(1);
        this.assertActNow(1);
        //Trap BASIC damage=3
        this.sendTrap(1, 26);
        trapPlacedAsserter(26, 1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        //end turn 2 11 9
        //first adventurer HP=4, defuse value=1
        adventurerDamagedAsserter(2, 2);  //trap
        adventurerDamagedAsserter(2, 2);  //fatig
        imprisonAsserter(2);
        //second adventurer HP=4, heal value=2
        adventurerDamagedAsserter(11, 2);  //faitg
        //third adventurer  HP=3, heal value=1
        adventurerDamagedAsserter(9, 2);   //fatig
        //conquer
        conqueredAsserter(11, 0, 0);
        evilnessChangedAsserter(-1, 1);
        //fled
        fledAdventurerAsserter(2);
        evilnessChangedAsserter(-1, 1);
        //heal
        healedAdventurerAsserter(2, 11, 11);
        healedAdventurerAsserter(1, 9, 9);
        nextRoundAsserter(2);

        //next round (second round) (no unconquered tile left)
        nextRoundAsserter(2);
        evilnessChangedAsserter(-1, 1);
        nextRoundAsserter(3);

        //next round (third round)
        nextRoundAsserter(3);
        evilnessChangedAsserter(-1, 1);
        nextRoundAsserter(4);

        //next round (fourth round)
        nextRoundAsserter(4);
        evilnessChangedAsserter(-1, 1);
    }

    protected void player2Defend() throws TimeoutException {
        //choose battleground
        nextRoundAsserter(1);
        this.assertSetBattleGround(2);
        this.assertActNow(2);
        this.sendBattleGround(2, 0, 0);
        battelGroundChoosedAsserter(0, 0, 2);
        //defend begin and set trap and monster
        this.assertDefendYourself(2);
        this.assertActNow(2);
        //trap MULTI damage=1
        this.sendTrap(2, 6);
        trapPlacedAsserter(6, 2);
        this.assertActNow(2);
        //monster BASIC damage=3
        this.sendMonster(2, 14);
        monsterPlacedAsserter(14, 2);
        this.assertActNow(2);
        this.sendEndTurn(2);
        //end turn 20 29 26
        //first adventurer, charge=true, defuse value=2, HP=5
        adventurerDamagedAsserter(20, 3); //monster
        adventurerDamagedAsserter(20, 2); //fatig
        imprisonAsserter(20);
        //second adventurer, heal value=2, defuse value=1, HP=5
        adventurerDamagedAsserter(29, 2); //fatig
        //third adventurer, defuse value=1, HP=4
        adventurerDamagedAsserter(26, 2); //fatig
        //conquer
        conqueredAsserter(29, 0, 0);
        evilnessChangedAsserter(-1, 2);
        //heal
        healedAdventurerAsserter(2, 29, 29);
        nextRoundAsserter(2);

        //next round (second round)
        nextRoundAsserter(2);
        this.assertSetBattleGround(2);
        this.assertActNow(2);
        this.sendBattleGround(2, 0, 1);
        battelGroundChoosedAsserter(0, 1, 2);
        //defend begin and set trap and monster
        this.assertDefendYourself(2);
        this.assertActNow(2);
        this.sendEndTurn(2);
        //end turn 29 26
        adventurerDamagedAsserter(29, 2); //fatig
        adventurerDamagedAsserter(26, 2); //fatig
        imprisonAsserter(26);
        //conquer
        conqueredAsserter(29, 0, 1);
        evilnessChangedAsserter(-1, 2);
        //heal
        healedAdventurerAsserter(2, 29, 29);
        nextRoundAsserter(3);

        //next round (third round)
        nextRoundAsserter(3);
        this.assertSetBattleGround(2);
        this.assertActNow(2);
        this.sendBattleGround(2, 0, 2);
        battelGroundChoosedAsserter(0, 2, 2);
        //defend begin and set trap and monster
        this.assertDefendYourself(2);
        this.assertActNow(2);
        this.sendEndTurn(2);
        //end turn 29
        adventurerDamagedAsserter(29, 2); //fatig
        //conquer
        conqueredAsserter(29, 0, 2);
        evilnessChangedAsserter(-1, 2);
        //fled
        fledAdventurerAsserter(20);
        evilnessChangedAsserter(-1, 2);
        //heal
        healedAdventurerAsserter(2, 29, 29);
        nextRoundAsserter(4);

        //next round (fourth round)
        nextRoundAsserter(4);
        fledAdventurerAsserter(26);
        evilnessChangedAsserter(-1, 2); //because of fled
        evilnessChangedAsserter(-1, 2); //because of all tiles conquered
    }

    protected void player3Defend() throws TimeoutException {
        //choose battleground
        nextRoundAsserter(1);
        this.assertSetBattleGround(3);
        this.assertActNow(3);
        this.sendBattleGround(3, 0, 0);
        battelGroundChoosedAsserter(0, 0, 3);
        //defend begin and set trap
        this.assertDefendYourself(3);
        this.assertActNow(3);
        //trap MULTI damage=1
        this.sendTrap(3, 19);
        trapPlacedAsserter(19, 3);
        this.assertActNow(3);
        //monster BASIC damage=1
        this.sendMonster(3, 3);
        monsterPlacedAsserter(3, 3);
        this.assertActNow(3);
        this.sendEndTurn(3);
        //end turn 6 23 15
        //first adventurer HP=6, heal val=2, defuse val=2
        //second adventurer HP=6, heal val=3
        //third adventurer HP=6, heal val=3
        adventurerDamagedAsserter(15, 1); //trapdamage
        adventurerDamagedAsserter(6, 1); //monsterdamage
        adventurerDamagedAsserter(6, 2); //fatigdamage
        adventurerDamagedAsserter(23, 2); //fatigdamage
        adventurerDamagedAsserter(15, 2); //fatigdamage
        //conquer
        conqueredAsserter(6, 0, 0);
        evilnessChangedAsserter(-1, 3);
        //heal
        healedAdventurerAsserter(2, 6, 6);
        healedAdventurerAsserter(1, 23, 6);
        healedAdventurerAsserter(2, 23, 23);
        healedAdventurerAsserter(3, 15, 15);



    }
}
