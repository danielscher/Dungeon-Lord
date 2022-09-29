package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.TimeStamp;
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

    private Player currPlayingPlayer;
    private TimeStamp timeStamp = gd.getTime();

    //selected Trap
    private Trap placedtrap = null;
    //placed monsters and the target position
    private final Map<Monster, Integer> placedMonsters = new HashMap<Monster, Integer>();
    private Dungeon dungeon = currPlayingPlayer.getDungeon();
    private boolean actionfailed = false;


    public CombatPhase(GameData gd, Player player) {
        super(gd);
        this.currPlayingPlayer = player;


    }

    public Phase run() throws TimeoutException {
        //send defend yourself
        gd.getServerConnection().sendDefendYourself(currPlayingPlayer.getCommID());
        //coordinate of the current battleground
        Coordinate battleground = dungeon.getCurrBattleGround();
        //if the tile is a room two monsters can be placed in total 3 nextActions
        if (dungeon.hasTileRoom(battleground)) {
            //in this case the player can place 2 monster and one trap
            if (dungeon.getNumAvailableMonsters() > 1 && dungeon.getNumAvailableTraps() > 0) {
                for (int i = 0; i < 3; i++) {
                    gd.getServerConnection().nextAction().invoke(this);
                    if (actionfailed) {
                        i -= 1;
                        actionfailed = false;
                    }
                }
                //player can place 2 monsters or player can place a monster and a trap
            } else if (
                    (dungeon.getNumAvailableMonsters() == 1 && dungeon.getNumAvailableTraps() > 0)
                            || (
                            dungeon.getNumAvailableMonsters() > 1
                                    && dungeon.getNumAvailableTraps() == 0)) {
                for (int i = 0; i < 2; i++) {
                    gd.getServerConnection().nextAction().invoke(this);
                    if (actionfailed) {
                        i -= 1;
                        actionfailed = false;
                    }
                }
                //player can place 1 monster or a trap
            } else if (
                    (dungeon.getNumAvailableMonsters() == 1 && dungeon.getNumAvailableTraps() == 0)
                            || (
                            dungeon.getNumAvailableMonsters() == 0
                                    && dungeon.getNumAvailableTraps() == 1)) {
                while (true) {
                    gd.getServerConnection().nextAction().invoke(this);
                    if (actionfailed = false) {
                        break;
                    }
                }
            }
            //if the tile is not a room 1 monster and 1 trap in total 2 nextActions
        } else {
            //if player has available monster and traps
            if ((dungeon.getNumAvailableMonsters() > 0) && (dungeon.getNumAvailableTraps() > 0)) {
                for (int i = 0; i < 2; i++) {
                    gd.getServerConnection().nextAction().invoke(this);
                    if (actionfailed) {
                        i -= 1;
                        actionfailed = false;
                    }
                }
                //if the player has only one monster or only one trap
            } else if (
                    (dungeon.getNumAvailableMonsters() > 0 && dungeon.getNumAvailableTraps() == 0)
                            || (
                            dungeon.getNumAvailableMonsters() == 0
                                    && dungeon.getNumAvailableTraps() > 0)) {
                while (true) {
                    gd.getServerConnection().nextAction().invoke(this);
                    if (actionfailed = false) {
                        break;
                    }
                }
            }

        }
        //Fighting
        int totalDefuseVal = 0;
        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            totalDefuseVal += dungeon.getAdventurer(i).getDefuseValue();
        }
        // trap damages
        if (placedtrap != null) {
            if (totalDefuseVal < placedtrap.getDamage()) {
                switch (placedtrap.getAttack()) {
                    case TARGETED:
                        if (dungeon.getAdventurer(placedtrap.getTarget()) != null) {
                            if (dungeon.getAdventurer(placedtrap.getTarget())
                                    .damagehealthby(placedtrap.getDamage() - totalDefuseVal) >= 0) {
                                dungeon.imprison(dungeon.getAdventurer(placedtrap.getTarget())
                                        .getAdventurerID());
                                broadcastAdventurerImprisoned(
                                        dungeon.getAdventurer(placedtrap.getTarget())
                                                .getAdventurerID());
                            } else {
                                broadcastAdventurerDamaged(
                                        dungeon.getAdventurer(placedtrap.getTarget())
                                                .getAdventurerID(),
                                        placedtrap.getDamage() - totalDefuseVal
                                );
                            }
                        } // to Handle if the target Adventurer is not present in the queue
                        break;
                    case BASIC:
                        if (dungeon.getAdventurer(0)
                                .damagehealthby(placedtrap.getDamage() - totalDefuseVal) >= 0) {
                            dungeon.imprison(dungeon.getAdventurer(0).getAdventurerID());
                            broadcastAdventurerImprisoned(
                                    dungeon.getAdventurer(0).getAdventurerID());
                        } else {
                            broadcastAdventurerDamaged(
                                    dungeon.getAdventurer(0).getAdventurerID(),
                                    placedtrap.getDamage() - totalDefuseVal
                            );
                        }
                        break;
                    case MULTI:
                        int res = placedtrap.getDamage() - totalDefuseVal;
                        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
                            int current_reduction = res;
                            res = dungeon.getAdventurerById(i).damagehealthby(res);
                            if (res >= 0) {
                                dungeon.imprison(dungeon.getAdventurerById(i).getAdventurerID());
                                broadcastAdventurerImprisoned(
                                        dungeon.getAdventurer(i).getAdventurerID());
                            } else {
                                broadcastAdventurerDamaged(
                                        dungeon.getAdventurer(i).getAdventurerID(),
                                        current_reduction);
                                break;
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

        }

        //Monster damages

        if (placedMonsters.size() > 0) {
            for (Monster monster : placedMonsters.keySet()) {
                switch (monster.getAttack()) {
                    case TARGETED:
                        Adventurer adTargeted = dungeon.getAdventurer(placedMonsters.get(monster));
                        if (adTargeted != null) {
                            if (adTargeted.damagehealthby(monster.getDamage()) >= 0) {
                                dungeon.imprison(adTargeted.getAdventurerID());
                                broadcastAdventurerImprisoned(adTargeted.getAdventurerID());

                            } else {
                                broadcastAdventurerDamaged(adTargeted.getAdventurerID(),
                                        monster.getDamage());
                            }
                        }// To handle when the target adventurer is not there in the queue
                        break;

                    case BASIC:
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
                        break;
                    case MULTI:
                        int res = monster.getDamage();

                        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
                            int current_reduction = res;
                            res = dungeon.getAdventurerById(i).damagehealthby(res);
                            if (res >= 0) {
                                dungeon.imprison(dungeon.getAdventurerById(i).getAdventurerID());
                                broadcastAdventurerImprisoned(
                                        dungeon.getAdventurer(i).getAdventurerID());

                            } else {
                                broadcastAdventurerDamaged(
                                        dungeon.getAdventurer(i).getAdventurerID(),
                                        current_reduction);
                                break;
                            }
                        }
                        break;
                    default:
                        break;


                }

            }

        }

        //fatigue damage by 2 every round

        for (int i = 0; i < dungeon.getNumAdventurersInQueue(); i++) {
            if (dungeon.getAdventurer(i).damagehealthby(2) >= 0) {
                dungeon.imprison(dungeon.getAdventurer(i).getAdventurerID());
                broadcastAdventurerImprisoned(
                        dungeon.getAdventurer(i).getAdventurerID());

            } else {
                broadcastAdventurerDamaged(dungeon.getAdventurer(i).getAdventurerID(),
                        2);

            }
        }

        //Conquer if one of the adventurer is alive the tile is conquered

        if (dungeon.getAdventurer(0) != null) {
            Coordinate coordinate = dungeon.getCurrBattleGround();
            dungeon.getGrid()[coordinate.getxpos()][coordinate.getypos()].setConquered();
            broadcastTunnelConquered(dungeon.getAdventurer(0).getAdventurerID(),
                    coordinate.getxpos(), coordinate.getypos());

            currPlayingPlayer.changeEvilnessBy(-1);

            if (dungeon.getNumUnconqueredTiles() == 0) {
                if (dungeon.getNumImprisonedAdventurers() != 0) {
                    //fly the first adventurer
                    broadcastAdventurerFled(dungeon.fleeadventureinQueue().getAdventurerID());
                }
                // TODO: 29.09.22
            }
        }

        return new GameEndPhase(gd);
    }


    public void exec(TrapAction ta) {
        if (ta.getCommID() == currPlayingPlayer.getCommID()) {
            // check whether trap is already placed
            if (placedtrap != null) {
                gd.getServerConnection()
                        .sendActionFailed(ta.getCommID(), "Trap has been already placed");
                actionfailed = true;


            } else {
                // check if the trap exists in the Duengeon
                if (dungeon.getTrapByID(ta.getTrapID()) != null) {
                    //check the availability of the trap this year
                    if (dungeon.getTrapByID(ta.getTrapID()).isAvailableThisYear()) {
                        //placing a trap in a room always costs one coin of gold
                        if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                            if (currPlayingPlayer.changeGoldBy(-1)) {
                                placedtrap = dungeon.getTrapByID(ta.getTrapID());
                                broadcastGoldChanged(-1, currPlayingPlayer.getCommID());
                                broadcastTrapPlaced(currPlayingPlayer.getPlayerID(),
                                        ta.getTrapID());
                                dungeon.getTrapByID(ta.getTrapID()).setUnavailable();
                            } else {
                                gd.getServerConnection().sendActionFailed(ta.getCommID(),
                                        "player cannot afford one gold to place trap in the room");
                                actionfailed = true;
                            }
                            //if it is a tile no gold changes
                        } else {
                            placedtrap = dungeon.getTrapByID(ta.getTrapID());
                            broadcastTrapPlaced(currPlayingPlayer.getPlayerID(), ta.getTrapID());
                            dungeon.getTrapByID(ta.getTrapID()).setUnavailable();

                        }
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(ta.getCommID(),
                                        "Trap is not available this year");
                        actionfailed = true;
                    }

                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ta.getCommID(),
                                    "No available trap for the requested id");
                    actionfailed = true;
                }
            }

        } else {
            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "CommId of the current player didn't match");
            actionfailed = true;

        }

    }

    public void exec(MonsterAction ma) {

        if (ma.getCommID() == currPlayingPlayer.getCommID()) {
            // check if 2 monsters are already placed
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(ma.getCommID(), "Two Monsters have been already placed");
                actionfailed = true;
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
                            gd.getServerConnection()
                                    .sendActionFailed(ma.getCommID(),
                                            "One Monster is already placed in the Tile");
                            actionfailed = true;

                        }
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(ma.getCommID(),
                                        "Monster is not available this year");
                        actionfailed = true;
                    }
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ma.getCommID(),
                                    "No available monster for the requested id");
                    actionfailed = true;

                }

            } else {
                //the tile and the room can have at least one monster
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    if (dungeon.getMonsterByID(ma.getMonster()).availableThisYear()) {
                        placedMonsters.put(dungeon.getMonsterByID(ma.getMonster()), -1);
                        broadcastMonsterPlaced(ma.getMonster(), currPlayingPlayer.getPlayerID());
                        dungeon.getMonsterByID(ma.getMonster()).setUnavailable();
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(ma.getCommID(),
                                        "Monster is not available this year");
                        actionfailed = true;
                    }
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ma.getCommID(),
                                    "No available monster for the requested id");
                    actionfailed = true;
                }
            }


        } else {
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(),
                            "CommID of the current player did not match");
            actionfailed = true;
        }
    }


    public void exec(MonsterTargetedAction mta) {
        if (mta.getCommID() == currPlayingPlayer.getCommID()) {
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(mta.getCommID(), "Two Monsters have been already placed");
                actionfailed = true;

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
                                gd.getServerConnection().sendActionFailed(mta.getCommID(),
                                        "The Monster Attack strategy is not Targeted");
                                actionfailed = true;
                            }
                        } else {
                            gd.getServerConnection()
                                    .sendActionFailed(mta.getCommID(),
                                            "One Monster is already placed in the Tile");
                            actionfailed = true;

                        }
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(mta.getCommID(),
                                        "Monster is not available this year");
                        actionfailed = true;

                    }
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(mta.getCommID(),
                                    "No available monster for the requested id");
                    actionfailed = true;

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
                        gd.getServerConnection()
                                .sendActionFailed(mta.getCommID(),
                                        "Monster is not available this year");
                        actionfailed = true;
                    }

                } else {
                    gd.getServerConnection()
                            .sendActionFailed(mta.getCommID(),
                                    "No available monster for the requested id");
                    actionfailed = true;
                }
            }


        } else {
            gd.getServerConnection()
                    .sendActionFailed(mta.getCommID(),
                            "CommID of the current player did not match");
            actionfailed = true;
        }
    }


    public void exec(EndTurnAction eta) {
        if (currPlayingPlayer.getCommID() != eta.getCommID()) {
            gd.getServerConnection()
                    .sendActionFailed(eta.getCommID(), "CommID of the player didn't match");
        }

    }


}
