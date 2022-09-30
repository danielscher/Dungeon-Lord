package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class HireMonsterAction extends Action {

    private final int monster;

    public HireMonsterAction(final int commID, final int monster) {
        super(commID);
        this.monster = monster;
    }

    public int getMonster() {
        return monster;
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
