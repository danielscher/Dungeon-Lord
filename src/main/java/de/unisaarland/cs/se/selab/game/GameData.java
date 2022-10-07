package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.game.action.Action;
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

public class GameData {

    private final Map<Integer, Integer> commIdToPlayerIdMap = new HashMap<>();
    private final Map<Integer, Integer> playerIdToCommIDMap = new HashMap<>();
    private final Map<Integer, Player> idToPlayerMap = new HashMap<>();
    private final TimeStamp time = new TimeStamp();
    private final BiddingSquare biddingSquare = new BiddingSquare();
    private final List<Adventurer> currAvailableAdventurers = new ArrayList<>();
    private final List<Monster> currAvailableMonsters = new ArrayList<>();
    private final List<Trap> currAvailableTraps = new ArrayList<>();
    private final List<Room> currAvailableRooms = new ArrayList<>();

    private final ServerConnection<Action> serverConnection;
    // private final Config config;
    private final AltConfig config;
    private int firstBidder;
    private int idCounter;

    public GameData(final AltConfig config, final ServerConnection<Action> serverConnection) {
        this.firstBidder = 0;
        this.idCounter = 0;
        this.config = config;
        this.serverConnection = serverConnection;
    }

    private void addPlayer(final Player player, final int id) {
        final int commId = player.getCommID();
        idToPlayerMap.put(id, player);
        commIdToPlayerIdMap.put(commId, id);
        playerIdToCommIDMap.put(id, commId);
    }

    public TimeStamp getTime() {
        return time;
    }

    public boolean registerPlayer(final String name, final int commId) {
        final List<Integer> commList = new ArrayList<>(commIdToPlayerIdMap.keySet());
        if (commList.contains(commId)) {
            return false;
        } else {
            final int initFood = config.getInitialFood();
            final int initGold = config.getInitialGold();
            final int initImps = config.getInitialImps();
            final int initEvilness = config.getInitialEvilness();
            final int gridSideLength = config.getDungeonSideLength();
            final Player player = new Player(name, idCounter, commId, initFood, initGold, initImps,
                    initEvilness, gridSideLength);
            this.addPlayer(player, idCounter);
            this.idCounter = idCounter + 1;
            return true;
        }
    }

    public Player getPlayerByCommID(final int commId) {
        final int playerId = commIdToPlayerIdMap.get(commId);
        return idToPlayerMap.get(playerId);
    }

    public Player getPlayerByPlayerId(final int playerId) {
        return idToPlayerMap.get(playerId);
    }

    public int getPlayerIdByCommID(final int commId) {
        return commIdToPlayerIdMap.get(commId);
    }

    public int getCommIDByPlayerId(final int playerId) {
        return playerIdToCommIDMap.get(playerId);
    }

    public ServerConnection<Action> getServerConnection() {
        return this.serverConnection;
    }

    /*
    returns a set of all commIds
     */
    public Set<Integer> getCommIDSet() {
        return commIdToPlayerIdMap.keySet();
    }

    public boolean checkIfRegistered(final int commId) {
        final List<Integer> commList = new ArrayList<>(commIdToPlayerIdMap.keySet());
        return commList.contains(commId);
    }

    public int nextFirstBidder() {
        final List<Integer> playerList = new ArrayList<>(playerIdToCommIDMap.keySet());
        playerList.sort(Comparator.naturalOrder());

        int nextBidder = -1;
        for (final Integer i : playerList) {
            if (i > firstBidder) {
                nextBidder = i;
                break;
            }
        }

        if (nextBidder == -1) {
            // in this case there is no higher player Id --> take lowest
            try {
                nextBidder = playerList.get(0);
            } catch (IndexOutOfBoundsException e) {
                // do not change nextBidder
                nextBidder = -1;
            }
        }
        firstBidder = nextBidder;
        return nextBidder;
    }


    public int getNextCombatPlayer(final int lastplayerid) {
        final List<Integer> playerList = new ArrayList<>(playerIdToCommIDMap.keySet());
        playerList.sort(Comparator.naturalOrder());
        final int pos = playerList.indexOf(lastplayerid);
        if (pos == playerList.size() - 1) {
            return -1;
        } else {
            return (playerList.get(pos + 1));
        }
    }


    public int getFirstBidder() {
        return firstBidder;
    }

    public void setFirstBidder() {
        firstBidder = nextFirstBidder();
    }

    public List<Integer> getOrderedPlayerIdList() {
        final List<Integer> res = getAllPlayerID();
        res.sort(Comparator.naturalOrder());
        return res;
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


    public Monster getAndRemoveMonster(final int monsterId) {
        Monster chosenMonster;
        for (final Monster mon : currAvailableMonsters) {
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
        final Trap trap = currAvailableTraps.get(0);
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
        final List<Player> allPlayers = new ArrayList<>(idToPlayerMap.values());
        Collections.sort(allPlayers, Comparator.comparing(Player::getPlayerID));
        return allPlayers;
    }

    public int getMaxPlayers() {
        return config.getMaxPlayers();
    }

    public int getMaxYears() {
        // return config.getMaxYear(); // for Can's config
        return config.getYears(); // for Henry's config
    }

    public int getNumCurrPlayers() {
        return getAllPlayerID().size();
    }

    public void discardMonster() { // removes all monsters from currMonsters list.
        currAvailableMonsters.clear();
    }


    public void discardRoom() {
        currAvailableRooms.clear();
    }

    private void addDrawnAdventurers() {
        final List<Adventurer> drawnAdv = config.drawAdventurers(getNumCurrPlayers());
        currAvailableAdventurers.addAll(drawnAdv);
    }

    public void clearAdventurers() {
        currAvailableAdventurers.clear();
    }

    public void addDrawnTraps(final int amountPlaceTrapBids) {
        // Adds drawn traps to the curr available.
        final List<Trap> drawnTraps = config.drawTraps(amountPlaceTrapBids);
        currAvailableTraps.addAll(drawnTraps);
    }

    private void addDrawnMonsters() {
        final List<Monster> drawnMonsters = config.drawMonsters();
        currAvailableMonsters.addAll(drawnMonsters);
    }

    private void addDrawnRooms() {
        final List<Room> drawnRooms = config.drawRooms();
        currAvailableRooms.addAll(drawnRooms);
    }

    public void removePlayer(final int commId) {
        final int playerId = getPlayerIdByCommID(commId);
        this.playerIdToCommIDMap.remove(playerId);
        this.idToPlayerMap.remove(playerId);
        this.commIdToPlayerIdMap.remove(commId);
    }

    public String getConfigString() {
        return config.getConfigJSONString();
    }


}
