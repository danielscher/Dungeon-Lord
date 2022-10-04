package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.RegAction;
import de.unisaarland.cs.se.selab.game.action.StartGameAction;
import java.util.Set;

public class RegPhase extends Phase {


    private final int maxPlayers = gd.getMaxPlayers();

    /*
    whenever we get the startGame action this is set to true
     */
    private boolean isStarted; // init to false.

    public RegPhase(final GameData gd) {
        super(gd);
    }

    /*
    loop over the maximum players and ask the nextAction from the serverconnection
     */
    @Override
    public Phase run() {
        while (gd.getNumCurrPlayers() < maxPlayers) {
            if (isStarted) {
                break;
            }
            try {

                gd.getServerConnection().nextAction().invoke(this);
            } catch (TimeoutException e) {
                broadcastRegistrationAborted();
                return null;
            }
        }
        final Set<Integer> commIDs = gd.getCommIDSet();
        //send the registered players for all players
        for (final Integer commID : commIDs) {
            this.broadcastPlayer(gd.getPlayerByCommID(commID).getName(),
                    gd.getPlayerIdByCommID(commID));
        }
        return new CollectAndPlaceBidPhase(gd);
    }

    @Override
    public void exec(final RegAction ra) {
        if (gd.checkIfRegistered(ra.getCommID())) {
            gd.getServerConnection().sendRegistrationAborted(ra.getCommID()); // FIXME action failed
        } else {
            final boolean res = gd.registerPlayer(ra.getName(), ra.getCommID());
            if (!res) {
                gd.getServerConnection().sendRegistrationAborted(ra.getCommID());
            }
            final String configString = gd.getConfigString();
            gd.getServerConnection().sendConfig(ra.getCommID(), configString);
        }
    }

    @Override
    public void exec(final StartGameAction sga) {
        this.isStarted = true;
        this.broadcastGameStarted();
    }
}
