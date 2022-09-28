package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class MonsterAction extends Action {

    private int monster;

    public MonsterAction(int commID, int monster) {
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
