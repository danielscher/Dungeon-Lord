package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.game.util.Coordinate;
import de.unisaarland.cs.se.selab.phase.Phase;

public class BattleGroundAction extends Action {

    private int row;
    private int col;

    public BattleGroundAction(int commID, int x, int y) {
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

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
