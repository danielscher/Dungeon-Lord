package de.unisaarland.cs.se.selab.phase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.RegAction;
import de.unisaarland.cs.se.selab.game.action.StartGameAction;
import java.util.Set;

public class RegPhase extends Phase {


    private int maxPlayers = gd.getConfig().getMaxPlayer();
    private boolean isStarted = false;

    public RegPhase(GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        for (int i = 0; i < maxPlayers; i++) {
            if (isStarted == true) {
                break;
            }
            Action action = gd.getServerConnection().nextAction();
            action.invoke(this);
        }
        Set<Integer> commIDs = gd.getCommIDSet();
        //send the registered players for all players
        for (Integer commID : commIDs) {
            this.broadcastPlayer(gd.getPlayerByCommID(commID).getName(),
                    gd.getPlayerIdByCommID(commID));
        }
        return new CollectAndPlaceBidPhase(gd);
    }

    public void exec(RegAction ra) {
        if (gd.checkIfRegistered(ra.getCommID())) {
            gd.getServerConnection().sendRegistrationAborted(ra.getCommID());
        } else {
            boolean res = gd.registerPlayer(ra.getName(), ra.getCommID());
            if (res == false) {
                gd.getServerConnection().sendRegistrationAborted(ra.getCommID());
            }
        }
    }

    public void exec(StartGameAction sga) {
        this.isStarted = true;
        this.broadcastGameStarted();
    }


    private boolean checkForStartAction() {
        return this.isStarted;
    }

}
