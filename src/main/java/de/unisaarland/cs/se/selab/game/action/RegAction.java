package de.unisaarland.cs.se.selab.game.action;

public class RegAction extends Action {

    private String name;

    public RegAction(int commID, String name) {
        super(commID);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
