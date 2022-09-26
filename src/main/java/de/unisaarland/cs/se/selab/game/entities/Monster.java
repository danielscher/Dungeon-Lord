package de.unisaarland.cs.se.selab.game.entities;

public class Monster {
    private String name;
    private int monsterID;
    private int hunger;
    private int evilness;
    private int damage;
    private Attack attack;
    private boolean availableThisYear;

    public Monster(int monsterID, int hunger, int evilness, int damage, Attack attack) {
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
