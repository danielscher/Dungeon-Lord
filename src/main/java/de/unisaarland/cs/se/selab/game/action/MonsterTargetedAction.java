package de.unisaarland.cs.se.selab.game.action;

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
}
