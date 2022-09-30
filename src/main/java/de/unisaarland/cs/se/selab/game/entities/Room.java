package de.unisaarland.cs.se.selab.game.entities;

import de.unisaarland.cs.se.selab.game.util.Location;

public class Room {

    private final int roomID;
    private final int activationCost;
    private final int foodProduction;
    private final int goldProduction;
    private final int impProduction;
    private final int niceness;

    private boolean activated;
    private final Location placementLoc;

    public Room(final int roomID, final int activationCost, final int foodProduction,
            final int goldProduction, final int impProduction, final int niceness,
            final Location placementLoc) {
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
