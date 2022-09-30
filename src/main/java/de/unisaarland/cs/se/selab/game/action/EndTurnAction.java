package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class EndTurnAction extends Action {

    public EndTurnAction(final int commID) {
        super(commID);
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
