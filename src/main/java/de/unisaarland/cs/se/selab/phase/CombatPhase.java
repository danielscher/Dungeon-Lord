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
import java.util.HashMap;
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
            // in this case we still have a reference to the player object, but he isnt
            // registered anymore --> transition to next phase
            return goToNextPhase();
        }

        //send defend yourself
        gd.getServerConnection().sendDefendYourself(currPlayingPlayer.getCommID());
        //coordinate of the current battleground
        final Coordinate battleground = dungeon.getCurrBattleGround();

        while (!endTurn) {
            try {
                gd.getServerConnection().sendActNow(currPlayingPlayer.getCommID());
                gd.getServerConnection().nextAction().invoke(this);
            } catch (TimeoutException e) {
                kickPlayer(currPlayingPlayer.getPlayerID());
                // TODO add logic to skip to next phase (next players combat or bidding or endgame)
                return null;
            }
            // check if the battleground is full
            if (dungeon.hasTileRoom(battleground) && (placedTrap != null
                    && placedMonsters.size() > 1)) {
                break;
            }
            if (!dungeon.hasTileRoom(battleground) && (placedTrap != null
                    && placedMonsters.size() == 1)) {
                break;
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
                int res = dungeon.getAdventurer(i).getHealValue();
                for (int j = 0; j < dungeon.getNumAdventurersInQueue(); j++) {
                    final int currentRes = res;
                    res = dungeon.getAdventurer(j).healBy(res);
                    if (res > 0) {
                        broadcastAdventurerHealed(currentRes - res,
                                dungeon.getAdventurer(i).getAdventurerID(),
                                dungeon.getAdventurer(j).getAdventurerID());
                    } else {
                        broadcastAdventurerHealed(currentRes,
                                dungeon.getAdventurer(i).getAdventurerID(),
                                dungeon.getAdventurer(j).getAdventurerID());
                        break;
                    }

                }

            }
        }
    }

    private void fatigueDamage() {
        //returns if the player left
        if (playerLeft) {
            return;
        }
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            if (dungeon.getAdventurer(i).damagehealthby(2) >= 0) {
                dungeon.imprison(dungeon.getAdventurer(i).getAdventurerID());
                broadcastAdventurerImprisoned(dungeon.getAdventurer(i).getAdventurerID());

            } else {
                broadcastAdventurerDamaged(dungeon.getAdventurer(i).getAdventurerID(), 2);

            }
        }
    }

    private void calcMonsterTargetedDamage(final Monster monster) {
        final Adventurer adTargeted = dungeon.getAdventurer(placedMonsters
                .get(monster));
        if (adTargeted != null) {
            if (adTargeted.damagehealthby(monster.getDamage()) >= 0) {
                dungeon.imprison(adTargeted.getAdventurerID());
                broadcastAdventurerImprisoned(adTargeted.getAdventurerID());

            } else {
                broadcastAdventurerDamaged(adTargeted.getAdventurerID(),
                        monster.getDamage());
            }
        }

    }

    private void calcMonsterBasicDamage(final Monster monster) {
        if (dungeon.getAdventurer(0) != null) {
            if (dungeon.getAdventurer(0).damagehealthby(monster.getDamage()) >= 0) {
                dungeon.imprison(dungeon.getAdventurer(0).getAdventurerID());
                broadcastAdventurerImprisoned(
                        dungeon.getAdventurer(0).getAdventurerID());
            } else {
                broadcastAdventurerDamaged(
                        dungeon.getAdventurer(0).getAdventurerID(),
                        monster.getDamage());
            }

        }
    }

    private void calcMonsterMultiDamage(final Monster monster) {
        int res = monster.getDamage();

        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            final int currentReduction = res;
            res = dungeon.getAdventurerById(i).damagehealthby(res);
            if (res >= 0) {
                dungeon.imprison(dungeon.getAdventurerById(i).getAdventurerID());
                broadcastAdventurerImprisoned(
                        dungeon.getAdventurer(i).getAdventurerID());

            } else {
                broadcastAdventurerDamaged(
                        dungeon.getAdventurer(i).getAdventurerID(),
                        currentReduction);
                break;
            }
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
        if (dungeon.getAdventurer(placedTrap.getTarget()) != null) {
            if (dungeon.getAdventurer(placedTrap.getTarget())
                    .damagehealthby(placedTrap.getDamage() - totalDefuseVal) >= 0) {
                dungeon.imprison(dungeon.getAdventurer(placedTrap.getTarget())
                        .getAdventurerID());
                broadcastAdventurerImprisoned(
                        dungeon.getAdventurer(placedTrap.getTarget())
                                .getAdventurerID());
            } else {
                broadcastAdventurerDamaged(
                        dungeon.getAdventurer(placedTrap.getTarget())
                                .getAdventurerID(),
                        placedTrap.getDamage() - totalDefuseVal);
            }
        }
    }

    private void calcTrapBasicDamage(final int totalDefuseVal) {
        if (dungeon.getAdventurer(0)
                .damagehealthby(placedTrap.getDamage() - totalDefuseVal) >= 0) {
            dungeon.imprison(dungeon.getAdventurer(0).getAdventurerID());
            broadcastAdventurerImprisoned(
                    dungeon.getAdventurer(0).getAdventurerID());
        } else {
            broadcastAdventurerDamaged(dungeon.getAdventurer(0).getAdventurerID(),
                    placedTrap.getDamage() - totalDefuseVal);
        }
    }

    private void calcTrapMultiDamage(final int totalDefuseVal) {
        int res = placedTrap.getDamage() - totalDefuseVal;
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            final int currentReduction = res;
            res = dungeon.getAdventurerById(i).damagehealthby(res);
            if (res >= 0) {
                dungeon.imprison(dungeon.getAdventurerById(i).getAdventurerID());
                broadcastAdventurerImprisoned(
                        dungeon.getAdventurer(i).getAdventurerID());
            } else {
                broadcastAdventurerDamaged(
                        dungeon.getAdventurer(i).getAdventurerID(),
                        currentReduction);
                break;
            }
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

        if (timeStamp.getSeason() < 8 && dungeon.getNumAdventurersInQueue() > 0 && !playerLeft) {
            timeStamp.nextSeason();
            broadcastNextRound(timeStamp.getSeason() - 4);
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

            currPlayingPlayer.changeEvilnessBy(-1);

            // check if adventurer can escape.
            if (dungeon.getNumUnconqueredTiles() == 0) {
                if (dungeon.getNumImprisonedAdventurers() != 0) {

                    // free an imprisoned adventurer.
                    broadcastAdventurerFled(dungeon.fleeadventureinQueue().getAdventurerID());
                    currPlayingPlayer.changeEvilnessBy(-1);
                }

            }
        }
    }

}
