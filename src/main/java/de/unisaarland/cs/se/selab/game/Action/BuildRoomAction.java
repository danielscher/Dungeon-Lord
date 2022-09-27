package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

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

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
