package de.unisaarland.cs.se.selab.phase;
import de.unisaarland.cs.se.selab.game.Action.*;
import de.unisaarland.cs.se.selab.game.GameData;

public class RegPhase extends Phase{

    public RegPhase(GameData gd) {
        super(gd);
    }

    public CollectAndPlaceBidPhase run(){
        return new CollectAndPlaceBidPhase();
        //TODO
    }

    private void exec(RegAction ra){
    //TODO
    }

    private boolean checkForStartAction(){
        //TODO
        return false;
    }

    private void exec(StartGameAction sga){
    //TODO
    }
}
