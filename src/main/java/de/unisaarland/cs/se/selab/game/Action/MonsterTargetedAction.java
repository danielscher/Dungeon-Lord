package de.unisaarland.cs.se.selab.game.Action;

public class MonsterTargetedAction extends Action{

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
}
