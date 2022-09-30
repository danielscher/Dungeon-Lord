package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class LeaveAction extends Action {

    public LeaveAction(final int commID) {
        super(commID);
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
