package de.unisaarland.cs.se.selab.game.Action;

public class ActivateRoomAction extends Action {

    private int roomID;

    public ActivateRoomAction(int commID, int roomID) {
        super(commID);
        this.roomID = roomID;
    }


    public int getRoomID() {
        return roomID;
    }
}
