package de.unisaarland.cs.se.selab.game.Action;

public abstract class Action {
    private int commID;

    public Action(int commID) {
        this.commID = commID;
    }

    public int getCommID() {
        return commID;
    }

}
