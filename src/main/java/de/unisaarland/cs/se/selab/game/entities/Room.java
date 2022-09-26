package de.unisaarland.cs.se.selab.game.entities;

import de.unisaarland.cs.se.selab.game.util.Location;

public class Room {
    private String name;
    private int roomID, activationCost, foodProduction, goldProduction, impProduction, niceness;
    private boolean activated;
    private Location placementLoc;
}
