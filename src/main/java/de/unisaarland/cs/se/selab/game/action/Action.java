package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public abstract class Action {

    final private int commID;

    public Action(final int commID) {
        this.commID = commID;
    }

    public int getCommID() {
        return commID;
    }

    public void invoke(final Phase phase) {
        phase.exec(this);
    }

}

