package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.Game;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActionFactoryImplementation;
import java.util.Random;
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
    private static boolean isRunning = true;

    private static boolean parseCommandLineArgs(final String[] args) {
        final Options options = new Options();
        options.addOption("config", true, "the path to the config file");
        options.addOption("port", true, "port for the server connection");
        options.addOption("seed", true, "seed used for the random object");
        options.addOption("timeout", true, "time until server timeouts");

        final CommandLineParser clParser = new DefaultParser();
        try {
            final CommandLine cmd = clParser.parse(options, args);
            if (cmd.getArgs().length != 4) {
                throw new UnsupportedOperationException();
            }
            if (cmd.hasOption("config")) {
                final String path = cmd.getOptionValue("config");
            }
            if (cmd.hasOption("port")) {
                port = Integer.parseInt(cmd.getOptionValue("port"));
            }
            if (cmd.hasOption("seed")) {
                seed = Long.parseLong(cmd.getOptionValue("seed"));
            }
            if (cmd.hasOption("timeout")) {
                timeout = Integer.parseInt(cmd.getOptionValue("timeout"));
            }
            return true;
        } catch (ParseException e) {
            //FIXME: maybe this should be handled differently.
            return false;

        }
    }

    public static void main(final String[] args) {

        //parse command line arguments terminate if not successful.
        if (!parseCommandLineArgs(args)) {
            System.exit(1);
        }

        //create random obj
        final Random random = new Random(seed);

        Game game;
        try (ServerConnection<Action> sc = new ServerConnection<Action>(port, timeout,
                new ActionFactoryImplementation())) {
            //TODO : Initialize Game wirth the objects above + path.
            game = new Game(sc, path);
        }

        if(game.runGame()) {
            System.exit(0);
        } else {
            System.exit(1);
        }


    }

}
