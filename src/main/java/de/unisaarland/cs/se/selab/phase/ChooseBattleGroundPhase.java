package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.Action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.player.Player;

public class ChooseBattleGroundPhase extends Phase{
    private Player currPlayingPlayer;

    public Phase run(){
        //TODO
        return new Combatphase();
    }

    private void exec(BattleGroundAction bga){
        //TODO
        //ServerConnection.sendNextRound(currPlayingPlayer.getCommID(),)
    }
}
