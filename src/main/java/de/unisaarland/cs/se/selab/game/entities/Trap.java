package de.unisaarland.cs.se.selab.game.entities;

public class Trap {

    private final int trapID;
    private final int damage;
    private final int target;
    private final Attack attack;
    private boolean availableThisYear;

    public Trap(final int trapID, final int damage, final int target, final Attack attack) {
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
