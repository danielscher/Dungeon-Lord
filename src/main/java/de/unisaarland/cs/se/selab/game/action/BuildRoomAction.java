package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class BuildRoomAction extends Action {

    private final int roomID;
    private final int row;
    private final int col;

    public BuildRoomAction(final int commID, final int roomID, final int row, final int col) {
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

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
