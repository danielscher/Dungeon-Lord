package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.game.util.Coordinate;
import de.unisaarland.cs.se.selab.phase.Phase;

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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
