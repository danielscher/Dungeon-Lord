package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class TrapAction extends Action {

    private int trapID;

    public TrapAction(int commID, int trapID) {
        super(commID);
        this.trapID = trapID;
    }

    public int getTrapID() {
        return trapID;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
