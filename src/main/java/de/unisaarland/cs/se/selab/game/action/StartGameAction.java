package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class StartGameAction extends Action {

    public StartGameAction(int commID) {
        super(commID);
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
