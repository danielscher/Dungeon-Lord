package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class ActivateRoomAction extends Action {

    private int roomID;

    public ActivateRoomAction(int commID, int roomID) {
        super(commID);
        this.roomID = roomID;
    }


    public int getRoomID() {
        return roomID;
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
