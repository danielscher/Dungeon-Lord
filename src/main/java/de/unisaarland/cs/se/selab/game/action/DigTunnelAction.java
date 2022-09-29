package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class DigTunnelAction extends Action {

    private final int row;
    private final int col;

    public DigTunnelAction(final int commID, final int row, final int col) {
        super(commID);
        this.row = row;
        this.col = col;
    }

    public int[] getCoords() {
        return new int[]{row, col};
    }

    @Override
    public void invoke(final Phase phase) {
        phase.exec(this);
    }

}
