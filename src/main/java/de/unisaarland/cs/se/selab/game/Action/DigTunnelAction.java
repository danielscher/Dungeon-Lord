package de.unisaarland.cs.se.selab.game.Action;

public class DigTunnelAction extends Action{
    private int x, y;

    public DigTunnelAction(int commID, int x, int y) {
        super(commID);
        this.x = x;
        this.y = y;
    }

    public int[] getCoords() {
        return new int[] {x, y};
    }

}
