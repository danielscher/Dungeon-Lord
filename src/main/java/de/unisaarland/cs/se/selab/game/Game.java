package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.phase.Phase;
import de.unisaarland.cs.se.selab.phase.RegPhase;

public class Game {

    private final GameData gameData = new GameData();
    private Phase currPhase = new RegPhase(gameData);
    private final ServerConnection<Action> sc;


    public Game(final ServerConnection<Action> sc) {
        this.sc = sc;
    }

    public boolean runGame() {
        try {
            currPhase = currPhase.run();
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }


}

