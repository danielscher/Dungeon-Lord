package de.unisaarland.cs.se.selab.game.Action;

public class BuildRoomAction extends Action {
    private int roomID, x, y;

    public BuildRoomAction(int commID, int roomID, int x, int y) {
        super(commID);
        this.roomID = roomID;
        this.x = x;
        this.y = y;
    }

    public int getRoomID() {
        return roomID;
    }

    public int[] getcoords() {
        return new int[]{x, y};
    }
}
