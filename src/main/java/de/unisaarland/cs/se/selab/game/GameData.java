package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.Action.Action;
import de.unisaarland.cs.se.selab.game.Action.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
class NoSuchCommIDException extends Exception {
    public NoSuchCommIDException() {
    }
}

class NoSuchPlayerIDException extends Exception {
    public NoSuchPlayerIDException() {
    }
}
*/

public class GameData {

    private final Map<Integer, Integer> commIDToPlayerIDMap = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> playerIDToCommIDMap = new HashMap<Integer, Integer>();
    private final Map<Integer, Player> idToPlayerMap = new HashMap<Integer, Player>();
    private final TimeStamp time = new TimeStamp();
    private final BiddingSquare biddingSquare = new BiddingSquare();
    private final List<Adventurer> currAvailableAdventurers = new ArrayList<Adventurer>();
    private final List<Monster> currAvailableMonsters = new ArrayList<Monster>();
    private final List<Trap> currAvailableTraps = new ArrayList<Trap>();
    private final List<Room> currAvailableRooms = new ArrayList<Room>();
    private final ServerConnection<Action> serverconnection = new ServerConnection<Action>(8080,
            5000, new ActionFactoryImplementation());
    private final Config config = new Config();
    private int lastPlayerToStartBidding, idCounter;

    public GameData() {
        this.lastPlayerToStartBidding = 0;
        this.idCounter = 0;
    }

    private void addPlayer(Player player, int id) {
        int CommId = player.getCommID();
        idToPlayerMap.put(id, player);
        commIDToPlayerIDMap.put(CommId, id);
        playerIDToCommIDMap.put(id, CommId);
    }

    public boolean registerPlayer(String name, int CommID) {
        Boolean res = false;

        List<Integer> CommList = new ArrayList<Integer>(commIDToPlayerIDMap.keySet());
        if (CommList.contains(CommID)) {
            res = false;
        } else {
            Player player = new Player(name, idCounter, CommID);
            this.addPlayer(player, idCounter);
            this.idCounter = idCounter + 1;
            res = true;
        }

        return res;

    }

    public Player getPlayerByCommID(int CommID) {
        int playerID = commIDToPlayerIDMap.get(CommID);
        return idToPlayerMap.get(playerID);
    }

    public Player getPlayerByPlayerID(int playerID) {
        return idToPlayerMap.get(playerID);
    }

    public int getPlayerIDByCommID(int CommID) {
        return commIDToPlayerIDMap.get(CommID);
    }

    public int getCommIDByPlayerID(int PlayerID) {
        return playerIDToCommIDMap.get(PlayerID);
    }

    public ServerConnection<Action> getServerConnection() {
        return this.serverconnection;
    }

    /*
    returns a set of all commIDs
     */
    public Set<Integer> getCommIDSet() {
        return commIDToPlayerIDMap.keySet();
    }

    public boolean checkIfRegistered(int CommID) {
        Boolean res = false;
        List<Integer> CommList = new ArrayList<Integer>(commIDToPlayerIDMap.keySet());
        if (CommList.contains(CommID)) {
            res = true;
        }
        return res;
    }

    public int getNextStartPlayer() {
        List<Integer> PlayerList = new ArrayList<Integer>(playerIDToCommIDMap.keySet());
        PlayerList.sort(Comparator.naturalOrder());
        int pos = PlayerList.indexOf(lastPlayerToStartBidding);
        if (pos == PlayerList.size() - 1) {
            this.lastPlayerToStartBidding = PlayerList.get(0);
        } else {
            this.lastPlayerToStartBidding = PlayerList.get(pos + 1);
        }

        return lastPlayerToStartBidding;

    }

    public void drawEntities() {
        // TODO: 25.09.22  
    }

    public BiddingSquare getBiddingSquare() {
        return biddingSquare;
    }

    public List<Integer> getAllPlayerID() {
        List<Integer> playerIDList = new ArrayList<Integer>(idToPlayerMap.keySet());
        return playerIDList;
    }

    public void removePlayer(int CommId) {
        int playerId = getPlayerIDByCommID(CommId);
        this.playerIDToCommIDMap.remove(playerId);
        this.idToPlayerMap.remove(playerId);
        this.commIDToPlayerIDMap.remove(CommId);


    }

    public Config getConfig() {
        return config;
    }
}

