package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.TimeStamp;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
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

    //selected Trap
    private Trap placedTrap;
    //placed monsters and the target position
    private final Map<Monster, Integer> placedMonsters = new HashMap<>();
    private final Dungeon dungeon;

    private boolean endTurn = false;


    public CombatPhase(final GameData gd, final Player player) {
        super(gd);
        this.currPlayingPlayer = player;
        this.dungeon = currPlayingPlayer.getDungeon();

    }


    @Override
    public Phase run() throws TimeoutException {
        //send defend yourself
        try (ServerConnection<Action> serverConn = gd.getServerConnection()) {
            serverConn.sendDefendYourself(currPlayingPlayer.getCommID());
        }
        //coordinate of the current battleground
        final Coordinate battleground = dungeon.getCurrBattleGround();

        while (!endTurn) {
            try (ServerConnection<Action> sc = gd.getServerConnection()) {
                sc.sendActNow(currPlayingPlayer.getCommID());
                sc.nextAction().invoke(this);
            }
            // check if the battleground is full
            if (dungeon.hasTileRoom(battleground)) {
                if (placedTrap != null && placedMonsters.size() > 1) {
                    break;
                }
            } else {
                if (placedTrap != null && placedMonsters.size() == 1) {
                    break;
                }
            }
        }

        // calculate damage.
        trapDamage();
        monsterDamage();
        fatigueDamage();

        // if at least one adventurer is  alive conquer tile
        // and let adventurer escape if now unconquered tiles left.
        conquerTile();

        //healing
        healAdventurers(); // TODO: 01.10.22 if all adventurers have been defeated during combat

        // checks what should be the next phase.
        return goToNextPhase();
    }


    @Override
    public void exec(final TrapAction ta) {
        // check if action is of the expected commID.
        if (ta.getCommID() != currPlayingPlayer.getCommID()) {
            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "CommId of the current player didn't match");
        }

        // check whether trap is already placed
        if (placedTrap != null) {
            try (ServerConnection<Action> serverConn = gd.getServerConnection()) {
                serverConn.sendActionFailed(ta.getCommID(), "Trap has been already placed");
            }
        }

        // check if the trap exists in the Dungeon.
        if (dungeon.getTrapByID(ta.getTrapID()) == null) {
            gd.getServerConnection().sendActionFailed(ta.getCommID(),
                    "No available trap for the requested id");
        }

        // check if the trap is available this year.
        if (!dungeon.getTrapByID(ta.getTrapID()).isAvailableThisYear()) {
            gd.getServerConnection().sendActionFailed(ta.getCommID(),
                    "Trap is not available this year");
        }

        // placing a trap in a room always costs one coin of gold (cost = 1 gold.)
        if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {

            // check if player can afford placing a trap in a room.
            if (!currPlayingPlayer.changeGoldBy(-1)) {
                gd.getServerConnection().sendActionFailed(ta.getCommID(),
                        "player cannot afford one gold to place trap in the room");
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

        if (ma.getCommID() == currPlayingPlayer.getCommID()) {
            // check if 2 monsters are already placed
            if (placedMonsters.size() >= 2) {
                try (ServerConnection<Action> serverConn = gd.getServerConnection()) {
                    serverConn.sendActionFailed(ma.getCommID(),
                            "Two Monsters have been already placed");
                }
                //if one monster is already placed
            } else if (placedMonsters.size() == 1) {
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    if (dungeon.getMonsterByID(ma.getMonster()).availableThisYear()) {
                        if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                            placedMonsters.put(dungeon.getMonsterByID(ma.getMonster()), -1);
                            broadcastMonsterPlaced(ma.getMonster(),
                                    currPlayingPlayer.getPlayerID());
                            dungeon.getMonsterByID(ma.getMonster()).setUnavailable();
                        } else {
                            gd.getServerConnection().sendActionFailed(ma.getCommID(),
                                    "One Monster is already placed in the Tile");

                        }
                    } else {
                        gd.getServerConnection().sendActionFailed(ma.getCommID(),
                                "Monster is not available this year");
                    }
                } else {
                    gd.getServerConnection().sendActionFailed(ma.getCommID(),
                            "No available monster for the requested id");

                }

            } else {
                //the tile and the room can have at least one monster
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    if (dungeon.getMonsterByID(ma.getMonster()).availableThisYear()) {
                        placedMonsters.put(dungeon.getMonsterByID(ma.getMonster()), -1);
                        broadcastMonsterPlaced(ma.getMonster(), currPlayingPlayer.getPlayerID());
                        dungeon.getMonsterByID(ma.getMonster()).setUnavailable();
                    } else {
                        gd.getServerConnection().sendActionFailed(ma.getCommID(),
                                "Monster is not available this year");
                    }
                } else {
                    gd.getServerConnection().sendActionFailed(ma.getCommID(),
                            "No available monster for the requested id");
                }
            }

        } else {
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(), "CommID of the current player did not match");
        }
    }

    @Override
    public void exec(final MonsterTargetedAction mta) {
        if (mta.getCommID() == currPlayingPlayer.getCommID()) {
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(mta.getCommID(), "Two Monsters have been already placed");

            } else if (placedMonsters.size() == 1) {
                if (dungeon.getMonsterByID(mta.getMonster()) != null) {
                    if (dungeon.getMonsterByID(mta.getMonster()).availableThisYear()) {
                        //check the tile has room to place two monsters
                        if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                            //check the target monster has the TARGETED strategy
                            if (dungeon.getMonsterByID(mta.getMonster()).getAttack()
                                    == Attack.TARGETED) {
                                placedMonsters.put(dungeon.getMonsterByID(mta.getMonster()),
                                        mta.getPosition());
                                broadcastMonsterPlaced(mta.getMonster(),
                                        currPlayingPlayer.getPlayerID());
                                dungeon.getMonsterByID(mta.getMonster()).setUnavailable();
                            } else {
                                try (ServerConnection<Action> serverConn =
                                        gd.getServerConnection()) {
                                    serverConn.sendActionFailed(mta.getCommID(),
                                            "The Monster Attack strategy is not Targeted");
                                }
                            }
                        } else {
                            try (ServerConnection<Action> serverConn =
                                    gd.getServerConnection()) {
                                serverConn.sendActionFailed(mta.getCommID(),
                                        "One Monster is already placed in the Tile");
                            }

                        }
                    } else {
                        try (ServerConnection<Action> serverConn =
                                gd.getServerConnection()) {
                            serverConn.sendActionFailed(mta.getCommID(),
                                    "Monster is not available this year");
                        }

                    }
                } else {
                    try (ServerConnection<Action> serverConn =
                            gd.getServerConnection()) {
                        serverConn.sendActionFailed(mta.getCommID(),
                                "No available monster for the requested id");
                    }
                }

            } else {
                if (dungeon.getMonsterByID(mta.getMonster()) != null) {
                    if (dungeon.getMonsterByID(mta.getMonster()).availableThisYear()) {
                        if (dungeon.getMonsterByID(mta.getMonster()).getAttack()
                                == Attack.TARGETED) {
                            placedMonsters.put(dungeon.getMonsterByID(mta.getMonster()),
                                    mta.getMonster());
                            broadcastMonsterPlaced(mta.getMonster(),
                                    currPlayingPlayer.getPlayerID());
                            dungeon.getMonsterByID(mta.getMonster()).setUnavailable();
                        }
                    } else {
                        try (ServerConnection<Action> serverConn =
                                gd.getServerConnection()) {
                            serverConn.sendActionFailed(mta.getCommID(),
                                    "Monster is not available this year");
                        }
                    }

                } else {
                    try (ServerConnection<Action> serverConn =
                            gd.getServerConnection()) {
                        serverConn.sendActionFailed(mta.getCommID(),
                                "No available monster for the requested id");
                    }
                }
            }

        } else {
            try (ServerConnection<Action> serverConn =
                    gd.getServerConnection()) {
                serverConn.sendActionFailed(mta.getCommID(),
                        "CommID of the current player did not match");
            }
        }
    }

    @Override
    public void exec(final EndTurnAction eta) {
        if (currPlayingPlayer.getCommID() != eta.getCommID()) {
            try (ServerConnection<Action> serverConn =
                    gd.getServerConnection()) {
                serverConn.sendActionFailed(eta.getCommID(),
                        "CommID of the player didn't match");
            }
        } else {
            endTurn = true;
        }

    }

    private void healAdventurers() {
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
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            if (dungeon.getAdventurer(i).damagehealthby(2) >= 0) {
                dungeon.imprison(dungeon.getAdventurer(i).getAdventurerID());
                broadcastAdventurerImprisoned(dungeon.getAdventurer(i).getAdventurerID());

            } else {
                broadcastAdventurerDamaged(dungeon.getAdventurer(i).getAdventurerID(), 2);

            }
        }
    }

    private void calcMonsterTargetedDamage(Monster monster) {
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
        } // TODO handle when the target adventurer is not there in the queue
    }

    private void calcMonsterBasicDamage(Monster monster) {
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

    private void calcMonsterMultiDamage(Monster monster) {
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

    private void calcTrapTargetedDamage(int totalDefuseVal) {
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
        } // TODO Handle if the target Adventurer is not present in the queue
    }

    private void calcTrapBasicDamage(int totalDefuseVal) {
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

    private void calcTrapMultiDamage(int totalDefuseVal) {
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
        if (timeStamp.getSeason() < 8) {
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
                    broadcastNextYear(timeStamp.getYear());
                    return new CollectAndPlaceBidPhase(gd);

                }
            }
        }
    }

    /**
     * checks if the battleground to be conquered and adventurer flees
     */
    private void conquerTile() {
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
