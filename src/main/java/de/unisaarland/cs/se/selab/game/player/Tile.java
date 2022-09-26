package de.unisaarland.cs.se.selab.game.player;

public class Tile {

    private boolean conquered, hasRoom;
    private int distanceToEntrance, numTraps, numMonsters;

    public Tile() {
        this.distanceToEntrance = -1;
    }

    protected void setDistanceToEntrance(int distanceToEntrance) {
        this.distanceToEntrance = distanceToEntrance;
    }

    protected int getDistanceToEntrance() {
        return distanceToEntrance;
    }

    public boolean isConquered() {
        return conquered;
    }

    public void setConquered() {
        this.conquered = true;
    }

    public boolean hasRoom() {
        return hasRoom;
    }
}
