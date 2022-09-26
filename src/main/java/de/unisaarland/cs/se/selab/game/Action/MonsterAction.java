package de.unisaarland.cs.se.selab.game.Action;

public class MonsterAction extends Action{

    private int monster;

    public MonsterAction(int commID, int monster){
        super(commID);
        this.monster = monster;
    }

    public int getMonster(){
        return monster;
    }
}
