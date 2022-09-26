package de.unisaarland.cs.se.selab.game.player;

public class Tile {

    private boolean conquered, hasRoom;
    private int distanceToEntrance, numTraps, numMonsters;

    private Tile() {
        this.distanceToEntrance = -1;
    }

    protected void setDistanceToEntrance(int distanceToEntrance) {
        this.distanceToEntrance = distanceToEntrance;
    }

    protected int getDistanceToEntrance() {
        return distanceToEntrance;
    }

    public boolean getConquered() {
        return conquered;
    }
}
