package de.unisaarland.cs.se.selab.game.action;

public class TrapAction extends Action {

    private int trapID;

    public TrapAction(int commID, int trapID) {
        super(commID);
        this.trapID = trapID;
    }

    public int getTrapID() {
        return trapID;
    }
}
