package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class LeaveAction extends Action {

    public LeaveAction(int commID) {
        super(commID);
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
