package de.unisaarland.cs.se.selab.game.entities;

public class Adventurer {

    private int adventurerID;
    private int difficulty;
    private int healthPoints;
    private int maxHealthPoints;
    private int healValue;
    private int defuseValue;
    private boolean charge;


    public Adventurer(int adventurerID, int difficulty, int maxHealthPoints, int healValue,
            int defuseValue, boolean charge) {
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

    public int damagehealthby(int n) {
        if ((healthPoints - n) <= 0) {
            healthPoints = 0;
            return (n - healthPoints);

        } else {
            healthPoints = healthPoints - n;
            return -1;
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
