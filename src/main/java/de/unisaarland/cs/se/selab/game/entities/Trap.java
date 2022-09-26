package de.unisaarland.cs.se.selab.game.entities;

public class Trap {
    private String name;
    private int trapID;
    private int damage;
    private int target;
    private Attack attack;
    private boolean availableThisYear;

    public Trap(int trapID, int damage, int target, Attack attack) {
        this.trapID = trapID;
        this.damage = damage;
        this.target = target;
        this.attack = attack;
        this.availableThisYear = true;
    }

    public int getTrapID() {
        return trapID;
    }

    public int getDamage() {
        return damage;
    }

    public int getTarget() {
        return target;
    }

    public Attack getAttack() {
        return attack;
    }

    public boolean isAvailableThisYear() {
        return availableThisYear;
    }

    public void setUnavailable() {
        availableThisYear = false;
    }

    public void setAvailable() {
        availableThisYear = true;
    }
}
