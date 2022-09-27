package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.RegAction;
import de.unisaarland.cs.se.selab.game.action.StartGameAction;

public class RegPhase extends Phase {

    public RegPhase(GameData gd) {
        super(gd);
    }

    public CollectAndPlaceBidPhase run() {
        return null;
        //TODO
    }

    private void exec(RegAction ra) {
        //TODO
    }

    private void exec(StartGameAction sga) {
        //TODO
    }

    private boolean checkForStartAction() {
        //TODO
        return false;
    }
}
