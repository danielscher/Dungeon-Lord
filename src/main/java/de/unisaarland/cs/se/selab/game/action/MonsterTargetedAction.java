package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class MonsterTargetedAction extends Action {

    private int monster;
    private int position;

    public MonsterTargetedAction(int commID, int monster, int position) {
        super(commID);
        this.monster = monster;
        this.position = position;
    }

    public int getMonster() {
        return monster;
    }

    public int getPosition() {
        return position;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
