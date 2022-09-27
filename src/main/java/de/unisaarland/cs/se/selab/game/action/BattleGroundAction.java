package de.unisaarland.cs.se.selab.game.action;

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
}
