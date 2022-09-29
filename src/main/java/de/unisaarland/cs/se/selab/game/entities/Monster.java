package de.unisaarland.cs.se.selab.game.entities;

public class Monster {

    final private int monsterID;
    final private int hunger;
    final private int evilness;
    final private int damage;
    final private Attack attack;
    private boolean availableThisYear;

    public Monster(final int monsterID, final int hunger, final int evilness, final int damage, final Attack attack) {
        this.monsterID = monsterID;
        this.hunger = hunger;
        this.evilness = evilness;
        this.damage = damage;
        this.attack = attack;
        this.availableThisYear = true;
    }

    public int getMonsterID() {
        return monsterID;
    }

    public int getHunger() {
        return hunger;
    }

    public int getEvilness() {
        return evilness;
    }

    public int getDamage() {
        return damage;
    }

    public Attack getAttack() {
        return attack;
    }

    public boolean availableThisYear() {
        return availableThisYear;
    }

    public void makeUnavailable() {
        availableThisYear = false;
    }
}
