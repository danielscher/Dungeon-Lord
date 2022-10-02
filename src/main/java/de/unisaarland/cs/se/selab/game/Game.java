package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.phase.Phase;
import de.unisaarland.cs.se.selab.phase.RegPhase;
import java.io.FileNotFoundException;

public class Game {

    private GameData gameData;
    private Phase currPhase;
    private final ServerConnection<Action> sc;
    private final String configPath;


    public Game(final ServerConnection<Action> sc, String configPath) {
        this.sc = sc;
        this.configPath = configPath;
    }

    public boolean runGame() {
        final AltConfig config = new AltConfig(configPath);
        if(!config.parse()) {
            return false;
        }
        gameData = new GameData(config);
        try  {
            for(currPhase = new RegPhase(gameData); currPhase != null; currPhase = currPhase.run()) {
                // TODO try catch
            }
        } catch ()
        return true;
    }


}

