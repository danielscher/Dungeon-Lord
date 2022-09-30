package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class BattleGroundAction extends Action {

    private final int row;
    private final int col;

    public BattleGroundAction(final int commID, final int x, final int y) {
        super(commID);
        this.row = x;
        this.col = y;
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
