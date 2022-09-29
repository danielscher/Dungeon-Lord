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
import java.util.Collections;
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
    private int idCounter;

    public GameData() {
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
        List<Integer> commList = new ArrayList<Integer>(commIdToPlayerIdMap.keySet());
        if (commList.contains(commId)) {
            return false;
        } else {
            Player player = new Player(name, idCounter, commId);
            this.addPlayer(player, idCounter);
            this.idCounter = idCounter + 1;
            return true;
        }
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
        List<Integer> commList = new ArrayList<Integer>(commIdToPlayerIdMap.keySet());
        return commList.contains(commId);
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

    public Monster getCurrAvailableMonster(int monsterId) {
        Monster chosenMonster;
        for (Monster mon : currAvailableMonsters) {
            if (mon.getMonsterID() == monsterId) {
                chosenMonster = mon;
                currAvailableMonsters.remove(mon);
                return chosenMonster;
            }
        }
        return null;
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
        return new ArrayList<Integer>(idToPlayerMap.keySet());
    }

    public List<Player> getAllPlayerSortedByID() {
        List<Player> allPlayers = new ArrayList<Player>(idToPlayerMap.values());
        allPlayers.sort(Comparator.comparing(Player::getPlayerID));
        return allPlayers;
    }

    public int getMaxPlayers() {
        return config.getMaxPlayer();
    }

    public int getNumCurrPlayers() {
        //TODO: returns the number of currently registered players.
        return 1;
    }

    public void discardMonster() { // removes all monsters from currMonsters list.
        currAvailableMonsters.clear();
    }

    public void discardRoom() {
        currAvailableRooms.clear();
    }

    private void addDrawnAdventurers() {
        List<Adventurer> drawnAdv = config.drawAdventurers(getNumCurrPlayers());
        currAvailableAdventurers.addAll(drawnAdv);
    }

    public void clearAdventurers() {
        currAvailableAdventurers.clear();
    }

    public void addDrawnTraps(int amountPlaceTrapBids) { // Adds drawn traps to the curr available.
        List<Trap> drawnTraps = config.drawTraps(amountPlaceTrapBids);
        currAvailableTraps.addAll(drawnTraps);
    }

    private void addDrawnMonsters() {
        List<Monster> drawnMonsters = config.drawMonsters();
        currAvailableMonsters.addAll(drawnMonsters);
    }

    private void addDrawnRooms() {
        List<Room> drawnRooms = config.drawRooms();
        currAvailableRooms.addAll(drawnRooms);
    }

    public void removePlayer(int commId) {
        int playerId = getPlayerIdByCommID(commId);
        this.playerIdToCommIDMap.remove(playerId);
        this.idToPlayerMap.remove(playerId);
        this.commIdToPlayerIdMap.remove(commId);
    }
}
