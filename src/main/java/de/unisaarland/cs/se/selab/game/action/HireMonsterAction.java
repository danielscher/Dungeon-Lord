package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class HireMonsterAction extends Action {

    private int monster;

    public HireMonsterAction(int commID, int monster) {
        super(commID);
        this.monster = monster;
    }

    public int getMonster() {
        return monster;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
