package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.Action.BattleGroundAction;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.player.Player;

public class ChooseBattleGroundPhase extends Phase{
    private Player currPlayingPlayer;

    public ChooseBattleGroundPhase(GameData gd) {
        super(gd);
    }

    public Phase run(){
        //TODO
        return null;
    }

    private void exec(BattleGroundAction bga){
        //TODO
        //ServerConnection.sendNextRound(currPlayingPlayer.getCommID(),)
    }
}
