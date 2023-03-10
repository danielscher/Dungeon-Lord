package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActionFactoryImplementation;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static String path;
    private static int port = 8080;
    private static long seed;
    private static int timeout = 5000;

    private static boolean parseCommandLineArgs(final String[] args) throws ParseException {
        if (args.length != 8) {
            throw new UnsupportedOperationException();
        }
        final Options options = new Options();
        options.addOption("c", "config", true, "config path");
        options.addOption("p", "port", true, "port for the server connection");
        options.addOption("s", "seed", true, "seed used for the random object");
        options.addOption("t", "timeout", true, "time until server timeouts");

        final CommandLineParser clParser = new DefaultParser();
        final CommandLine cmd = clParser.parse(options, args);

        if (cmd.hasOption("config")) {
            path = cmd.getOptionValue("config");
        }
        if (cmd.hasOption("port")) {
            port = Integer.parseInt(cmd.getOptionValue("port"));
        }
        if (cmd.hasOption("seed")) {
            seed = Long.parseLong(cmd.getOptionValue("seed"));
        }
        if (cmd.hasOption("timeout")) {
            timeout = Integer.parseInt(cmd.getOptionValue("timeout"));
            if (timeout < 0) {
                timeout = -1;
            } else {
                timeout *= 1000; // conversion of s to ms
            }

        }
        return true;
    }

    public static void main(final String[] args) {

        //parse command line arguments terminate if not successful.
        try {
            if (!parseCommandLineArgs(args)) {
                System.exit(1);
            }
        } catch (ParseException e) {
            System.exit(1);
        }

        Path pathToConfig;
        try {
            pathToConfig = Paths.get(path);
            Game game;
            try (ServerConnection<Action> sc = new ServerConnection<Action>(port, timeout,
                    new ActionFactoryImplementation())) {
                //TODO : Initialize Game wirth the objects above + path.
                game = new Game(sc, pathToConfig, seed);
                if (game.runGame()) {
                    System.exit(0);
                } else {
                    System.exit(1);
                }
            }

        } catch (InvalidPathException e) {
            System.exit(1);
        }
    }

}
