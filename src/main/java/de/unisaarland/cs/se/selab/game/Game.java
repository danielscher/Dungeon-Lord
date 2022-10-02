package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.phase.Phase;
import de.unisaarland.cs.se.selab.phase.RegPhase;
import java.nio.file.Path;

public class Game {

    private final ServerConnection<Action> sc;
    private final Path configPath;
    private final long seed;


    public Game(final ServerConnection<Action> sc, final Path configPath, final long seed) {
        this.sc = sc;
        this.configPath = configPath;
        this.seed = seed;
    }

    public boolean runGame() {
        final AltConfig config = new AltConfig(configPath, seed);
        if (!config.parse()) {
            return false;
        }
        final GameData gameData = new GameData(config, sc);

        Phase currPhase = new RegPhase(gameData);

        do {
            currPhase = currPhase.run();
        } while (currPhase != null);

        return true;
    }
}

