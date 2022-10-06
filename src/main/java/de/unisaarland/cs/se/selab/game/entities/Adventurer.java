package de.unisaarland.cs.se.selab.game.entities;

public class Adventurer {

    private final int adventurerID;
    private final int difficulty;
    private int healthPoints;
    private final int maxHealthPoints;
    private final int healValue;
    private final int defuseValue;
    private final boolean charge;


    public Adventurer(final int adventurerID, final int difficulty, final int maxHealthPoints,
            final int healValue, final int defuseValue, final boolean charge) {
        this.adventurerID = adventurerID;
        this.difficulty = difficulty;
        this.maxHealthPoints = maxHealthPoints;
        this.healthPoints = maxHealthPoints; // on creation adv has full hp
        this.healValue = healValue;
        this.defuseValue = defuseValue;
        this.charge = charge;
    }

    public int getAdventurerID() {
        return adventurerID;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getHealValue() {
        return healValue;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getDefuseValue() {
        return defuseValue;
    }

    public boolean getCharge() {
        return charge;
    }

    /**
     * this method damage the adventurer and return the rest damage. -1 means no damage left, didn't
     * die 0 means no damage left, died >0 means damage left, died
     */
    public int damagehealthby(final int damage) {
        if (healthPoints > damage) {
            // in this case the adventurer survives the attack
            healthPoints -= damage;
            return -1;
        } else {
            // in this case the adventurer doesn't survive
            final int leftoverDamage = healthPoints - damage;
            healthPoints = 0;
            return leftoverDamage;
        }
    }

    /*
    this method heals the adventurer by the given amount
    but only until his maximum health points are reached
    the return indicates the leftover heal value, which wasn't used
     */
    public int healBy(final int amount) {
        if (amount <= 0) {
            // if the given heal amount is negative or zero, don't heal
            return 0;
        } else {
            if (amount + healthPoints > maxHealthPoints) {
                // in this case there is leftover heal amount
                final int healedAmount = maxHealthPoints - healthPoints;
                healthPoints = maxHealthPoints;
                return amount - healedAmount;
            } else {
                // in this case the amount wasn't sufficient to fully heal the adventurer
                healthPoints += amount;
                return 0;
            }
        }
    }


}
