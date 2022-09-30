package de.unisaarland.cs.se.selab.game.util;

public class Coordinate {

    private final int xpos;
    private final int ypos;

    public Coordinate(final int xpos, final int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public int getxpos() {
        return xpos;
    }

    public int getypos() {
        return ypos;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        } else {
            final Coordinate coordinate = (Coordinate) obj;
            // only return true if x and y are the same
            return (coordinate.xpos == this.xpos && coordinate.ypos == this.ypos);
        }
    }

    @Override
    public int hashCode() {
        return (xpos * 100 + ypos);
    }
}
