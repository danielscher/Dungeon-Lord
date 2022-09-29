package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class MonsterTargetedAction extends Action {

    private final int monster;
    private final int position;

    public MonsterTargetedAction(final int commID, final int monster, final int position) {
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

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
