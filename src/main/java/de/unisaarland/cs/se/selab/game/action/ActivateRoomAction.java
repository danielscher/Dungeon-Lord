package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class ActivateRoomAction extends Action {

    private final int roomID;

    public ActivateRoomAction(final int commID, final int roomID) {
        super(commID);
        this.roomID = roomID;
    }


    public int getRoomID() {
        return roomID;
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }
}
