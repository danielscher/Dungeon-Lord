package de.unisaarland.cs.se.selab.game.action;

public class BuildRoomAction extends Action {

    private int roomID;
    private int row;
    private int col;

    public BuildRoomAction(int commID, int roomID, int row, int col) {
        super(commID);
        this.roomID = roomID;
        this.row = row;
        this.col = col;
    }

    public int getRoomID() {
        return roomID;
    }

    public int[] getcoords() {
        return new int[]{row, col};
    }
}
