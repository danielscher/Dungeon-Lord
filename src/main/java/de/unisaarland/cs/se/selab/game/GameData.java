package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.action.Action;
import de.unisaarland.cs.se.selab.game.action.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
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

class NoSuchplayerIdException extends Exception {
    public NoSuchplayerIdException() {
    }
}
*/

public class GameData {

    private final Map<Integer, Integer> commIdToPlayerIdMap = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> playerIdToCommIDMap = new HashMap<Integer, Integer>();
    private final Map<Integer, Player> idToPlayerMap = new HashMap<Integer, Player>();
    private final TimeStamp time = new TimeStamp();
    private final BiddingSquare biddingSquare = new BiddingSquare();
    private List<Adventurer> currAvailableAdventurers = new ArrayList<Adventurer>();
    private List<Monster> currAvailableMonsters = new ArrayList<Monster>();
    private List<Trap> currAvailableTraps = new ArrayList<Trap>();
    private List<Room> currAvailableRooms = new ArrayList<Room>();
    private final ServerConnection<Action> serverconnection = new ServerConnection<Action>(8080,
            5000, new ActionFactoryImplementation());
    private final Config config = new Config();
    private int lastPlayerToStartBidding;
    private int idCounter;

    public GameData() {
        this.lastPlayerToStartBidding = 0;
        this.idCounter = 0;
    }

    private void addPlayer(Player player, int id) {
        int commId = player.getCommID();
        idToPlayerMap.put(id, player);
        commIdToPlayerIdMap.put(commId, id);
        playerIdToCommIDMap.put(id, commId);
    }

    public TimeStamp getTime() {
        return time;
    }

    public boolean registerPlayer(String name, int commId) {
        Boolean res = false;

        List<Integer> commList = new ArrayList<Integer>(commIdToPlayerIdMap.keySet());
        if (commList.contains(commId)) {
            res = false;
        } else {
            Player player = new Player(name, idCounter, commId);
            this.addPlayer(player, idCounter);
            this.idCounter = idCounter + 1;
            res = true;
        }

        return res;

    }

    public Player getPlayerByCommID(int commId) {
        int playerId = commIdToPlayerIdMap.get(commId);
        return idToPlayerMap.get(playerId);
    }

    public Player getPlayerByPlayerId(int playerId) {
        return idToPlayerMap.get(playerId);
    }

    public int getPlayerIdByCommID(int commId) {
        return commIdToPlayerIdMap.get(commId);
    }

    public int getCommIDByPlayerId(int playerId) {
        return playerIdToCommIDMap.get(playerId);
    }

    public ServerConnection<Action> getServerConnection() {
        return this.serverconnection;
    }

    /*
    returns a set of all commIds
     */
    public Set<Integer> getCommIDSet() {
        return commIdToPlayerIdMap.keySet();
    }

    public boolean checkIfRegistered(int commId) {
        Boolean res = false;
        List<Integer> commList = new ArrayList<Integer>(commIdToPlayerIdMap.keySet());
        if (commList.contains(commId)) {
            res = true;
        }
        return res;
    }

    public int getNextStartPlayer() {
        List<Integer> playerList = new ArrayList<Integer>(playerIdToCommIDMap.keySet());
        playerList.sort(Comparator.naturalOrder());
        int pos = playerList.indexOf(lastPlayerToStartBidding);
        if (pos == playerList.size() - 1) {
            this.lastPlayerToStartBidding = playerList.get(0);
        } else {
            this.lastPlayerToStartBidding = playerList.get(pos + 1);
        }

        return lastPlayerToStartBidding;

    }

    public void drawEntities() {
        discardMonster();   //discard old unsold monsters
        addDrawnMonsters(); //draw 3 new monsters
        discardRoom();  //discard old unsold rooms
        addDrawnRooms(); //draw 2 new rooms
        if (time.getSeason() < 4) {
            addDrawnAdventurers(); //draw as many new adv as players
        }
    }

    // Getters:
    public List<Adventurer> getCurrAvailableAdventurers() {
        return currAvailableAdventurers;
    }

    public List<Monster> getCurrAvailableMonsters() {
        return currAvailableMonsters;
    }

    public List<Room> getCurrAvailableRooms() {
        return currAvailableRooms;
    }

    public List<Trap> getCurrAvailableTraps() {
        return currAvailableTraps;
    }

    public Trap getOneCurrAvailableTrap() {
        Trap trap = currAvailableTraps.get(0);
        currAvailableTraps.remove(0);
        return trap;
    }

    public BiddingSquare getBiddingSquare() {
        return biddingSquare;
    }

    public List<Integer> getAllPlayerID() {
        List<Integer> playerIDList = new ArrayList<Integer>(idToPlayerMap.keySet());
        return playerIDList;
    }

    public int getMaxPlayers() {
        return config.getMaxPlayer();
    }

    public int getNumCurrPlayers() {
        //TODO: returns the number of currently registered players.
        return 1;
    }

    public void discardMonster() { // removes all monsters from currMonsters list.
        for (Monster m : currAvailableMonsters) {
            currAvailableMonsters.remove(m);
        }
    }

    public void addRoom(Room r) {
        currAvailableRooms.add(r);
    }

    public void addMonster(Monster m) {
        currAvailableMonsters.add(m);
    }

    public void discardRoom() {
        for (Room r : currAvailableRooms) {
            currAvailableRooms.remove(r);
        }
    }

    private void addDrawnAdventurers() {
        List<Adventurer> drawnAdv = config.drawAdventurers(getNumCurrPlayers());
        for (Adventurer adv : drawnAdv) {
            currAvailableAdventurers.add(adv);
        }
    }

    public void addDrawnTraps() { // Adds drawn traps to the curr available.
        List<Trap> drawnTraps = config.drawTraps(getNumCurrPlayers());
        for (Trap t : drawnTraps) {
            currAvailableTraps.add(t);
        }
    }

    private void addDrawnMonsters() {
        List<Monster> drawnMonsters = config.drawMonsters();
        for (Monster mon : drawnMonsters) {
            currAvailableMonsters.add(mon);
        }
    }

    private void addDrawnRooms() {
        List<Room> drawnRooms = config.drawRooms();
        for (Room room : drawnRooms) {
            currAvailableRooms.add(room);
        }
    }

    public void removePlayer(int commId) {
        int playerId = getPlayerIdByCommID(commId);
        this.playerIdToCommIDMap.remove(playerId);
        this.idToPlayerMap.remove(playerId);
        this.commIdToPlayerIdMap.remove(commId);
    }


}
