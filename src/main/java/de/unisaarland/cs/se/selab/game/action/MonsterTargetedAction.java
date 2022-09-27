package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class MonsterTargetedAction extends Action {

    private int monster, position;

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
