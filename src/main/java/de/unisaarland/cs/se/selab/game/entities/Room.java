package de.unisaarland.cs.se.selab.game.entities;

import de.unisaarland.cs.se.selab.game.util.Location;

public class Room {
<<<<<<< HEAD
    private String name;
    private int roomID, activationCost, foodProduction, goldProduction, impProduction, niceness;
=======

    private int roomID;
    private int activationCost;
    private int foodProduction;
    private int goldProduction;
    private int impProduction;
    private int niceness;
>>>>>>> c62f79381689a371ce5299546e55d2edbef794f4
    private boolean activated;
    private Location placementLoc;

    public Room(int roomID, int activationCost, int foodProduction, int goldProduction,
            int impProduction, int niceness, Location placementLoc) {
        this.roomID = roomID;
        this.activationCost = activationCost;
        this.foodProduction = foodProduction;
        this.goldProduction = goldProduction;
        this.impProduction = impProduction;
        this.niceness = niceness;
        this.placementLoc = placementLoc;
        this.activated = false;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getActivationCost() {
        return activationCost;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public int getGoldProduction() {
        return goldProduction;
    }

    public int getImpProduction() {
        return impProduction;
    }

    public int getNiceness() {
        return niceness;
    }

    public Location getPlacementLoc() {
        return placementLoc;
    }

    public boolean isActivated() {
        return activated;
    }

    public void deactivate() {
        activated = false;
    }

    public void activate() {
        activated = true;
    }
}
