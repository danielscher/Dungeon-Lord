package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.MonsterAction;
import de.unisaarland.cs.se.selab.game.action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.action.TrapAction;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Dungeon;
import de.unisaarland.cs.se.selab.game.player.Player;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class Combatphase extends Phase {

    private Player currPlayingPlayer;
    private int round;

    private Trap placedtrap = null;
    private List<Monster> placedMonsters = new ArrayList<>();
    private Dungeon dungeon = currPlayingPlayer.getDungeon();


    public Combatphase(GameData gd, Player player) {
        super(gd);
        this.currPlayingPlayer = player;
        this.round = 0;

    }

    public Phase run() throws TimeoutException {
        gd.getServerConnection().sendDefendYourself(currPlayingPlayer.getCommID());
        Coordinate battleground = dungeon.getCurrBattleGround();
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

        } else {
            if (dungeon.getNumMonsters() > 0) {
                for (int i = 0; i < 2; i++) {
                    Action action = gd.getServerConnection().nextAction();
                    action.invoke(this);
                }
            } else {
                Action action = gd.getServerConnection().nextAction();
                action.invoke(this);
            }

        }
        return new GameEndPhase(gd);
    }


    public void exec(TrapAction ta) {
        if (ta.getCommID() == currPlayingPlayer.getCommID()) {
            if (placedtrap != null) {
                gd.getServerConnection()
                        .sendActionFailed(ta.getCommID(), "Trap has been already placed");
            } else {
                if (dungeon.getTrapByID(ta.getTrapID()) != null) {
                    placedtrap = dungeon.getTrapByID(ta.getTrapID());
                    this.broadcastTrapPlaced(currPlayingPlayer.getPlayerID(), ta.getTrapID());
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
            if (placedMonsters.size() >= 2) {
                gd.getServerConnection()
                        .sendActionFailed(ma.getCommID(), "Two Monsters have been already placed");

            } else if (placedMonsters.size() == 1) {
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    if (dungeon.hasTileRoom(dungeon.getCurrBattleGround())) {
                        placedMonsters.add(dungeon.getMonsterByID(ma.getMonster()));
                        this.broadcastMonsterPlaced(ma.getMonster(),
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
                if (dungeon.getMonsterByID(ma.getMonster()) != null) {
                    placedMonsters.add(dungeon.getMonsterByID(ma.getMonster()));
                    this.broadcastMonsterPlaced(ma.getMonster(), currPlayingPlayer.getPlayerID());
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
        //TODO
    }

    public void exec(EndTurnAction eta) {
        //nothing to do
    }


}
