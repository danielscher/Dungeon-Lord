package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class RegAction extends Action {

    private String name;

    public RegAction(int commID, String name) {
        super(commID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
