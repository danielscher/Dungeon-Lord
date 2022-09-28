package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public abstract class Action {

    private int commID;

    public Action(int commID) {
        this.commID = commID;
    }

    public int getCommID() {
        return commID;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }

}

