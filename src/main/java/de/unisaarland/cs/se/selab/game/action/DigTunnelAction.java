package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class DigTunnelAction extends Action {

    private int row;
    private int col;

    public DigTunnelAction(int commID, int row, int col) {
        super(commID);
        this.row = row;
        this.col = col;
    }

    public int[] getCoords() {
        return new int[]{row, col};
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }

}
