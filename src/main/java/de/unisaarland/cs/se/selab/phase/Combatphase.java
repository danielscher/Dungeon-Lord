package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.action.MonsterAction;
import de.unisaarland.cs.se.selab.game.action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.action.TrapAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class Combatphase extends Phase {

    private Player currPlayingPlayer;

    public Combatphase(GameData gd) {
        super(gd);
    }

    public Phase run() {
        //TODO
        return null;
    }

    public void exec(TrapAction ta) {
        //TODO
    }

    public void exec(MonsterAction ma) {
        //TODO
    }

    public void exec(MonsterTargetedAction mta) {
        //TODO
    }

    public void exec(EndTurnAction eta) {
        //TODO
    }


}
