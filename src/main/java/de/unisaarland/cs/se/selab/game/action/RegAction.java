package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class RegAction extends Action {

    private final String name;

    public RegAction(final int commID, final String name) {
        super(commID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
