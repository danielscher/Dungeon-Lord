package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.nio.file.Path;
import java.util.Objects;

class CombatPhaseTest {

    GameData gd;
    CombatPhase cmtPhase;


    private void setdata() {
        final Path configPath = readFile("configuration.json");
        final AltConfig altConfig = new AltConfig(configPath, 123);
        gd = new GameData(altConfig,
                new ServerConnection<Action>(8080, 5000, new ActionFactoryImplementation()));

        gd.registerPlayer("player1", 1);
        gd.registerPlayer("player2", 2);
        final Player p1 = gd.getPlayerByPlayerId(0);
        final Player p2 = gd.getPlayerByPlayerId(1);
        p1.changeGoldBy(5);
        p1.changeFoodBy(5);
        p1.changeEvilnessBy(5);
        p1.getDungeon().addImps(10);
        p1.getDungeon().dig(0, 0);
        p1.getDungeon().dig(1, 0);


    }

    private Path readFile(final String fileName) {
        return Path.of(Objects.requireNonNull(getClass().getResource(fileName)).getPath());
    }

}
