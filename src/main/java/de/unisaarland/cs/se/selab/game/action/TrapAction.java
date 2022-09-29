package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class TrapAction extends Action {

    private final int trapID;

    public TrapAction(final int commID, final int trapID) {
        super(commID);
        this.trapID = trapID;
    }

    public int getTrapID() {
        return trapID;
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
