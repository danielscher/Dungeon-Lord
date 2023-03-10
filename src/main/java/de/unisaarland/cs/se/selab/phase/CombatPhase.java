package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.TimeStamp;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.LeaveAction;
import de.unisaarland.cs.se.selab.game.action.MonsterAction;
import de.unisaarland.cs.se.selab.game.action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.action.TrapAction;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombatPhase extends Phase {

    private final Player currPlayingPlayer;
    private final TimeStamp timeStamp = gd.getTime();
    //placed monsters and the target position
    private final Map<Monster, Integer> placedMonsters = new HashMap<>();
    private final Dungeon dungeon;
    //selected Trap
    private Trap placedTrap;
    private boolean endTurn;
    private boolean playerLeft;


    public CombatPhase(final GameData gd, final Player player) {
        super(gd);
        this.currPlayingPlayer = player;
        this.dungeon = currPlayingPlayer.getDungeon();

    }


    @Override
    public Phase run() {
        final int currPlayerId = currPlayingPlayer.getPlayerID();
        if (gd.getPlayerByPlayerId(currPlayerId) == null) {
            // in this case we still have a reference to the player object, but he isn't
            // registered anymore --> transition to next phase
            return goToNextPhase();
        }

        final Coordinate battleground = dungeon.getCurrBattleGround();

        if (battleground == null && dungeon.getNumUnconqueredTiles() == 0) {
            letOneAdvFlee();
            if (currPlayingPlayer.changeEvilnessBy(-1)) {
                broadcastEvilnessChanged(-1, currPlayerId);
            }
            return goToNextPhase();
        }

        //send defend yourself
        gd.getServerConnection().sendDefendYourself(currPlayingPlayer.getCommID());
        //coordinate of the current battleground

        while (!endTurn) {
            try {
                gd.getServerConnection().sendActNow(currPlayingPlayer.getCommID());
                gd.getServerConnection().nextAction().invoke(this);
            } catch (TimeoutException e) {
                kickPlayer(currPlayingPlayer.getPlayerID());
                // TODO add logic to skip to next phase (next players combat or bidding or endgame)
                return goToNextPhase();
            }
            if (gd.getPlayerByPlayerId(currPlayerId) == null) {
                // in this case we still have a reference to the player object, but he isn't
                // registered anymore --> transition to next phase
                return goToNextPhase();
            }
        }
        //if player left skip thr damages and healing

        // calculate damage.
        trapDamage();
        monsterDamage();
        fatigueDamage();

        // if at least one adventurer is  alive conquer tile
        // and let adventurer escape if now unconquered tiles left.
        conquerTile();

        //healing
        healAdventurers();

        // checks what should be the next phase.
        return goToNextPhase();
    }

    @Override
    public void gotInvalidActionFrom(final int commID) {
        if (commID == currPlayingPlayer.getCommID()) {
            gd.getServerConnection().sendActNow(commID);
        }
    }

    @Override
    public void exec(final LeaveAction la) {
        if (la.getCommID() == currPlayingPlayer.getCommID()) {
            endTurn = true;
        }
        playerLeft = true;
        super.exec(la);
    }


    @Override
    public void exec(final TrapAction ta) {
        // check if action is of the expected commID.
        if (ta.getCommID() != currPlayingPlayer.getCommID()) {

            gd.getServerConnection().sendActionFailed(ta.getCommID(),
                    "CommId of the current player didn't match");
            return;
        }

        // check whether trap is already placed
        if (placedTrap != null) {
            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "Trap has been already placed");
            return;
        }

        // check if the trap exists in the Dungeon.
        if (dungeon.getTrapByID(ta.getTrapID()) == null) {
            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "No available trap for the requested id");
            return;
        }

        // check if the trap is available this year.
        if (!dungeon.getTrapByID(ta.getTrapID()).isAvailableThisYear()) {

            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "Trap is not available this year");
            return;
        }

        // placing a trap in a room always costs one coin of gold (cost = 1 gold.)
        if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {

            // check if player can afford placing a trap in a room.
            if (!currPlayingPlayer.changeGoldBy(-1)) {
                gd.getServerConnection().sendActionFailed(ta.getCommID(),
                        "player cannot afford one gold to place trap in the room");
                return;
            }

            // broadcasts gold change.
            placedTrap = dungeon.getTrapByID(ta.getTrapID());
            broadcastGoldChanged(-1, currPlayingPlayer.getCommID());

            // placing a trap on a regular tile.
        } else {
            placedTrap = dungeon.getTrapByID(ta.getTrapID());
        }

        // broadcast that a trap has been placed
        // and sets the trap to be unavailable for the next year.
        broadcastTrapPlaced(currPlayingPlayer.getPlayerID(),
                ta.getTrapID());
        dungeon.getTrapByID(ta.getTrapID()).setUnavailable();
    }


    @Override
    public void exec(final MonsterAction ma) {

        // check if the monster action is valid
        if (!canPlaceMonster(ma)) {
            return;
        }

        final int monsterId = ma.getMonster();
        final Monster selectedMonster = dungeon.getMonsterByID(monsterId);

        // check if monster isn't targeted
        if (selectedMonster.getAttack() == Attack.TARGETED) {
            // in this case the monster is targeted but no target was specified
            // therefor send ActionFailed and return
            gd.getServerConnection().sendActionFailed(ma.getCommID(),
                    "the selected is TARGETED, please specify a target");
            return;
        }

        placedMonsters.put(selectedMonster, -1); // add monster to target map (with target -1)
        broadcastMonsterPlaced(monsterId, currPlayingPlayer.getPlayerID()); // broadcast Event
        selectedMonster.setUnavailable(); // make this monster unavailable until next year
    }

    @Override
    public void exec(final MonsterTargetedAction mta) {

        // check if the monster action is valid
        if (!canPlaceMonster(mta)) {
            return;
        }

        final int monsterId = mta.getMonster();
        final Monster selectedMonster = dungeon.getMonsterByID(monsterId);

        // check if monster is targeted
        if (selectedMonster.getAttack() != Attack.TARGETED) {
            // in this case the monster is not targeted but a target was specified
            // therefor send ActionFailed and return
            gd.getServerConnection().sendActionFailed(mta.getCommID(),
                    "cannot target an not-targeted monster");
            return;
        }

        final int target = mta.getPosition();

        // check if target is valid
        if (target < 1 || target > 3) {
            // in this case the targeted position is invalid, therefor send ActionFailed
            gd.getServerConnection().sendActionFailed(mta.getCommID(),
                    "selected position must be between 1 and 3");
            return;
        }

        placedMonsters.put(selectedMonster, target); // add monster to target map
        broadcastMonsterPlaced(monsterId, currPlayingPlayer.getPlayerID()); // broadcast Event
        selectedMonster.setUnavailable(); // make this monster unavailable until next year
    }

    @Override
    public void exec(final EndTurnAction eta) {
        if (currPlayingPlayer.getCommID() != eta.getCommID()) {
            gd.getServerConnection().sendActionFailed(eta.getCommID(),
                    "CommID of the player didn't match");
        } else {
            endTurn = true;
        }

    }

    /**
     * this method checks if a monster action is valid right now and the monster can be placed,
     * NOTE: doesn't check attack type!
     */
    private boolean canPlaceMonster(final MonsterAction ma) {
        // check if the Action came from the right player
        if (ma.getCommID() != currPlayingPlayer.getCommID()) {
            // in this case the received Action Object wasn't sent from the right player
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(), "it's not your turn to place monsters");
            return false;
        }

        final int monsterId = ma.getMonster();

        // check if player owns monster
        if (dungeon.getMonsterByID(monsterId) == null) {
            // in this case the player doesn't own the monster
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(), "you don't seem to own this monster");
            return false;
        }

        final Monster selectedMonster = dungeon.getMonsterByID(monsterId);

        // check if monster is available in this year
        if (!selectedMonster.availableThisYear()) {
            // in this case the monster is not available
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(), "monster not available in this year");
            return false;
        }

        // calculate the amount of monsters that can be placed on this tile
        int placeableMonsters = 1;
        final Coordinate battleGround = dungeon.getCurrBattleGround();
        if (dungeon.hasTileRoom(battleGround)) {
            // in this case the selected battleground has a room -> increment placeable Monsters
            placeableMonsters++;
        }

        if (placedMonsters.size() >= placeableMonsters) {
            // in this case the player doesn't own the monster
            gd.getServerConnection().sendActionFailed(ma.getCommID(),
                    "you already placed the max. amount (" + placeableMonsters
                            + ") of monsters");
            return false;
        }

        // if none of the above checks failed, the player can place the monster
        return true;
    }

    /**
     * this method checks if a monster action is valid right now and the monster can be placed, uses
     * other overloaded method of not-targeted monster
     */
    private boolean canPlaceMonster(final MonsterTargetedAction mta) {
        final MonsterAction ma = new MonsterAction(mta.getCommID(), mta.getMonster());
        return canPlaceMonster(ma);
    }

    private void healAdventurers() {
        //returns if the player left or no adventurers in the queue
        if (dungeon.getNumAdventurersInQueue() == 0 || playerLeft) {
            return;
        }
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            if (dungeon.getAdventurer(i).getHealValue() > 0) {
                //adventurer i will heal others
                heal(i);
            }
        }
    }

    private void heal(final int i) {
        int reshealval = dungeon.getAdventurer(i).getHealValue();
        for (int j = 0; j < dungeon.getNumAdventurersInQueue(); j++) {
            if (dungeon.getAdventurer(j).getHealthPoints()
                    == dungeon.getAdventurer(j).getMaxHealthPoints()) {
                continue;
            }
            final int oldhp = dungeon.getAdventurer(j).getHealthPoints();
            reshealval = dungeon.getAdventurer(j).healBy(reshealval);
            broadcastAdventurerHealed(dungeon.getAdventurer(j).getHealthPoints() - oldhp,
                    dungeon.getAdventurer(i).getAdventurerID(),
                    dungeon.getAdventurer(j).getAdventurerID());
            if (reshealval == 0) {
                break;
            }
        }
    }

    private void fatigueDamage() {
        //returns if the player left
        if (playerLeft) {
            return;
        }
        final List<Adventurer> advList = new ArrayList<>(dungeon.getAdventurerQueue());
        for (final Adventurer currAdv : advList) {
            damageAdv(currAdv, 2);
        }
    }

    private void calcMonsterTargetedDamage(final Monster monster) {
        final Adventurer adTargeted = dungeon.getAdventurer(placedMonsters
                .get(monster));
        if (adTargeted != null) {
            damageAdv(adTargeted, monster.getDamage());
        }

    }

    private void calcMonsterBasicDamage(final Monster monster) {
        final Adventurer advToDamage = dungeon.getAdventurer(0);
        if (advToDamage != null) {
            damageAdv(advToDamage, monster.getDamage());
        }
    }

    private void calcMonsterMultiDamage(final Monster monster) {
        final int monsterDamage = monster.getDamage();
        final List<Adventurer> adventurerList = new ArrayList<>(dungeon.getAdventurerQueue());
        for (final Adventurer adventurer : adventurerList) {
            damageAdv(adventurer, monsterDamage);
        }
    }

    private void monsterDamage() {
        //returns if the player left
        if (playerLeft) {
            return;
        }
        if (!placedMonsters.isEmpty()) {
            for (final Monster monster : placedMonsters.keySet()) {
                switch (monster.getAttack()) {
                    case TARGETED -> calcMonsterTargetedDamage(monster);
                    case BASIC -> calcMonsterBasicDamage(monster);
                    case MULTI -> calcMonsterMultiDamage(monster);
                    default -> {
                    }
                }

            }

        }
    }

    private void calcTrapTargetedDamage(final int totalDefuseVal) {
        final int damageToApply = placedTrap.getDamage() - totalDefuseVal;
        final Adventurer adventurerToDamage = dungeon.getAdventurer(placedTrap.getTarget());

        if (adventurerToDamage != null) {
            damageAdv(adventurerToDamage, damageToApply);
        }
    }

    private void calcTrapBasicDamage(final int totalDefuseVal) {
        final int damageToApply = placedTrap.getDamage() - totalDefuseVal;
        final Adventurer adventurerToDamage = dungeon.getAdventurer(0);

        if (adventurerToDamage != null) {
            damageAdv(adventurerToDamage, damageToApply);
        }
    }

    private void calcTrapMultiDamage(final int totalDefuseVal) {
        int defuseBudget = totalDefuseVal;
        final int trapDamage = placedTrap.getDamage();
        final List<Adventurer> adventurerList = new ArrayList<>(dungeon.getAdventurerQueue());
        for (final Adventurer adventurer : adventurerList) {
            int appliedDmg = trapDamage;
            if (defuseBudget > 0) {
                // in this case we have a defuse-value-budget
                if (defuseBudget >= appliedDmg) {
                    // here the damage can be reduced to 0
                    defuseBudget -= appliedDmg;
                    appliedDmg = 0;
                } else {
                    // here the defuse-value only partially defuses the trap
                    appliedDmg -= defuseBudget;
                    defuseBudget = 0;
                }
            }
            damageAdv(adventurer, appliedDmg);
        }
    }

    private void trapDamage() {
        //returns if the player left
        if (playerLeft) {
            return;
        }
        int totalDefuseVal = 0;
        // get all defuse values from adventurers.
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            totalDefuseVal += dungeon.getAdventurer(i).getDefuseValue();
        }
        if (placedTrap != null) {
            if (totalDefuseVal < placedTrap.getDamage()) {
                switch (placedTrap.getAttack()) {
                    case TARGETED -> calcTrapTargetedDamage(totalDefuseVal);
                    case BASIC -> calcTrapBasicDamage(totalDefuseVal);
                    case MULTI -> calcTrapMultiDamage(totalDefuseVal);
                    default -> {
                    }
                }
            }

        }
    }

    /**
     * checks what should be the next phase returns ChooseBattleGroundPhase for same player if
     * season < 8 returns ChooseBattleGroundPhase for next player if season = 8 returns
     * CollectAndPlaceBidPhase when all player finished combat phase.
     */
    private Phase goToNextPhase() {
        if (gd.getAllPlayerID().isEmpty()) {
            return null;
        }

        if (timeStamp.getSeason() < 8 && dungeon.getNumAdventurersInQueue() > 0 && !playerLeft) {
            timeStamp.nextSeason();
            return new ChooseBattleGroundPhase(gd, currPlayingPlayer); // same player next round.
        } else {
            //if we have players left to combat
            if (gd.getNextCombatPlayer(currPlayingPlayer.getPlayerID()) >= 0) {
                timeStamp.timeTravel();
                return new ChooseBattleGroundPhase(gd, gd.getPlayerByPlayerId(
                        gd.getNextCombatPlayer(currPlayingPlayer.getPlayerID())));
                // passing to next year or ending the game
            } else {
                if (timeStamp.getYear() == gd.getMaxYears()) {
                    return new GameEndPhase(gd);
                } else {
                    timeStamp.nextyear();
                    return new CollectAndPlaceBidPhase(gd);

                }
            }
        }
    }

    /**
     * this method damages an adventurer with the given amount, might imprison him
     *
     * @param adventurer the adventurer to damage
     * @param amount     the amount to damage the adventurer with
     * @return the amount of damage left (-1 in case of 0) to adapt to old convention
     */
    private int damageAdv(final Adventurer adventurer, final int amount) {
        final int healthBeforeDamage = adventurer.getHealthPoints();
        final int advId = adventurer.getAdventurerID();
        final int leftoverDamage = adventurer.damagehealthby(amount);

        if (leftoverDamage != -1) {
            // in this case the adventurer died/will be imprisoned
            broadcastAdventurerDamaged(adventurer.getAdventurerID(), healthBeforeDamage);
            dungeon.imprison(advId);
            broadcastAdventurerImprisoned(advId);
        } else {
            // in this case he survived the attack
            broadcastAdventurerDamaged(advId, amount);
        }

        return leftoverDamage;
    }

    /**
     * checks if the battleground to be conquered and adventurer flees
     */
    private void conquerTile() {
        if (playerLeft) {
            return;
        }
        // if at least 1 adventurer is alive conquer.
        if (dungeon.getAdventurer(0) != null) {
            final Coordinate coordinate = dungeon.getCurrBattleGround();
            dungeon.setTileConquered(coordinate);
            broadcastTunnelConquered(dungeon.getAdventurer(0).getAdventurerID(),
                    coordinate.getxpos(), coordinate.getypos());

            if (currPlayingPlayer.changeEvilnessBy(-1)) {
                broadcastEvilnessChanged(-1, currPlayingPlayer.getPlayerID());
            }

            // check if adventurer can escape.
            if (dungeon.getNumUnconqueredTiles() == 0) {
                letOneAdvFlee();
            }
        }
    }

    private void letOneAdvFlee() {
        if (dungeon.getNumImprisonedAdventurers() != 0) {

            // free an imprisoned adventurer.
            broadcastAdventurerFled(dungeon.fleeadventureinQueue().getAdventurerID());
            if (currPlayingPlayer.changeEvilnessBy(-1)) {
                broadcastEvilnessChanged(-1, currPlayingPlayer.getPlayerID());
            }
        }
    }

}
