package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class BattleGroundAction extends Action {

    private int row;
    private int col;

    public BattleGroundAction(int commID, int x, int y) {
        super(commID);
        this.row = x;
        this.col = y;
    }

    public int[] getCoords() {
        return new int[]{row, col};
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
