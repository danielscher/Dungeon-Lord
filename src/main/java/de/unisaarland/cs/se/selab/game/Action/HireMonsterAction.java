package de.unisaarland.cs.se.selab.game.Action;

public class HireMonsterAction extends Action{
    private int monster;

    public HireMonsterAction(int commID, int monster) {
        super(commID);
        this.monster = monster;
    }

    public int getMonster() {
        return monster;
    }
}
