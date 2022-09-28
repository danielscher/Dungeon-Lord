package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.MonsterAction;
import de.unisaarland.cs.se.selab.game.action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.action.TrapAction;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.HashMap;
import java.util.Map;

public class Combatphase extends Phase {

    private Player currPlayingPlayer;
    private int round;
    //selected Trap
    private Trap placedtrap = null;
    //placed monsters and the target position
    private final Map<Monster, Integer> placedMonsters = new HashMap<Monster, Integer>();
    private Dungeon dungeon = currPlayingPlayer.getDungeon();


    public Combatphase(GameData gd, Player player) {
        super(gd);
        this.currPlayingPlayer = player;
        this.round = 0;

    }

    public Phase run() throws TimeoutException {
        //send defend yourself
        gd.getServerConnection().sendDefendYourself(currPlayingPlayer.getCommID());
        //coordinate of the current battleground
        Coordinate battleground = dungeon.getCurrBattleGround();
        //if the tile is a room two monsters can be placed in total 3 nextActions
        if (dungeon.hasTileRoom(battleground)) {
            if (dungeon.getNumMonsters() > 1) {
                for (int i = 0; i < 3; i++) {
                    Action action = gd.getServerConnection().nextAction();
                    action.invoke(this);
                }
            } else if (dungeon.getNumMonsters() == 1) {
                for (int i = 0; i < 2; i++) {
                    Action action = gd.getServerConnection().nextAction();
                    action.invoke(this);
                }
            } else {
                Action action = gd.getServerConnection().nextAction();
                action.invoke(this);
            }
            //if the tile is not a room 1 monster and 1 trap in total 2 nextActions
        } else {
            if (dungeon.getNumMonsters() > 0) {
                for (int i = 0; i < 2; i++) {
                    Action action = gd.getServerConnection().nextAction();
                    action.invoke(this);
                }
                //if no HiredMonsters only the trap action is wanted
            } else {
                Action action = gd.getServerConnection().nextAction();
                action.invoke(this);
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
            } else {
                // check if the trap exists in the Duengeon
                if (dungeon.getTrapByID(ta.getTrapID()) != null) {
                    //placing a trap in a room always costs one coin of gold
                    if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                        if (currPlayingPlayer.changeGoldBy(-1)) {
                            placedtrap = dungeon.getTrapByID(ta.getTrapID());
                            broadcastGoldChanged(-1, currPlayingPlayer.getCommID());
                            broadcastTrapPlaced(currPlayingPlayer.getPlayerID(),
                                    ta.getTrapID());
                        } else {
                            gd.getServerConnection().sendActionFailed(ta.getCommID(),
                                    "player cannot afford one gold to place trap in the room");
                        }
                        //if it is a tile no gold changes
                    } else {
                        placedtrap = dungeon.getTrapByID(ta.getTrapID());
                        broadcastTrapPlaced(currPlayingPlayer.getPlayerID(), ta.getTrapID());

                    }


                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ta.getCommID(),
                                    "No available trap for the requested id");
                }
            }

        } else {
            gd.getServerConnection()
                    .sendActionFailed(ta.getCommID(), "CommId of the current player didn't match");

        }

    }

    public void exec(MonsterAction ma) {

        if (ma.getCommID() == currPlayingPlayer.getCommID()) {
            // check if 2 monsters are already placed
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(ma.getCommID(), "Two Monsters have been already placed");
                //if one monster is already placed
            } else if (placedMonsters.size() == 1) {
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                        placedMonsters.put(dungeon.getMonsterByID(ma.getMonster()), -1);
                        broadcastMonsterPlaced(ma.getMonster(),
                                currPlayingPlayer.getPlayerID());
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(ma.getCommID(),
                                        "One Monster is already placed in the Tile");

                    }
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ma.getCommID(),
                                    "No available monster for the requested id");

                }

            } else {
                //the tile and the room can have at least one monster
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    placedMonsters.put(dungeon.getMonsterByID(ma.getMonster()), -1);
                    broadcastMonsterPlaced(ma.getMonster(), currPlayingPlayer.getPlayerID());
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(ma.getCommID(),
                                    "No available monster for the requested id");
                }
            }


        } else {
            gd.getServerConnection()
                    .sendActionFailed(ma.getCommID(),
                            "CommID of the current player did not match");
        }
    }


    public void exec(MonsterTargetedAction mta) {
        if (mta.getCommID() == currPlayingPlayer.getCommID()) {
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(mta.getCommID(), "Two Monsters have been already placed");

            } else if (placedMonsters.size() == 1) {
                if (dungeon.getMonsterByID(mta.getMonster()) != null) {
                    //check the tile has room to place two monsters
                    if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                        //check the target monster has the TARGETED strategy
                        if (dungeon.getMonsterByID(mta.getMonster()).getAttack()
                                == Attack.TARGETED) {
                            placedMonsters.put(dungeon.getMonsterByID(mta.getMonster()),
                                    mta.getPosition());
                            broadcastMonsterPlaced(mta.getMonster(),
                                    currPlayingPlayer.getPlayerID());
                        } else {
                            gd.getServerConnection().sendActionFailed(mta.getCommID(),
                                    "The Monster Attack strategy is not Targeted");
                        }
                    } else {
                        gd.getServerConnection()
                                .sendActionFailed(mta.getCommID(),
                                        "One Monster is already placed in the Tile");

                    }
                } else {
                    gd.getServerConnection()
                            .sendActionFailed(mta.getCommID(),
                                    "No available monster for the requested id");

                }

            } else {
                if (dungeon.getMonsterByID(mta.getMonster()) != null) {
                    if (dungeon.getMonsterByID(mta.getMonster()).getAttack() == Attack.TARGETED) {
                        placedMonsters.put(dungeon.getMonsterByID(mta.getMonster()),
                                mta.getMonster());
                        broadcastMonsterPlaced(mta.getMonster(),
                                currPlayingPlayer.getPlayerID());
                    }

                } else {
                    gd.getServerConnection()
                            .sendActionFailed(mta.getCommID(),
                                    "No available monster for the requested id");
                }
            }


        } else {
            gd.getServerConnection()
                    .sendActionFailed(mta.getCommID(),
                            "CommID of the current player did not match");
        }
    }


    public void exec(EndTurnAction eta) {
        //TOD
    }


}
