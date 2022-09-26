package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.Action.EndTurnAction;
import de.unisaarland.cs.se.selab.game.Action.MonsterAction;
import de.unisaarland.cs.se.selab.game.Action.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.game.Action.TrapAction;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;

public class Combatphase extends Phase{
    private Player currPlayingPlayer;

    public Combatphase(GameData gd) {
        super(gd);
    }

    public Phase run(){
        //TODO
        return null;
    }

    private void exec(TrapAction ta){
        //TODO
    }

    private void exec(MonsterAction ma){
        //TODO
    }

    private void exec(MonsterTargetedAction mta){
        //TODO
    }

    private void exec(EndTurnAction eta){
        //TODO
    }


}
