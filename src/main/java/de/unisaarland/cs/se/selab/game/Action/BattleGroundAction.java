package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.phase.Phase;

public class BattleGroundAction extends Action {

    private int x, y;

    public BattleGroundAction(int commID, int x, int y) {
        super(commID);
        this.x = x;
        this.y = y;
    }

    public int[] getCoords() {
        return new int[]{x, y};
    }

    public void invoke(Phase phase) {
        phase.exec(this);
    }
}
