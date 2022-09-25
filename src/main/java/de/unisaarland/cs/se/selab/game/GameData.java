package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.player.Player;
import java.util.*;

public class GameData {
    private Map<Integer,Integer> commIDToPlayerIDMap =new HashMap<Integer,Integer>();
    private Map<Integer,Integer> playerIDToCommIDMap =new HashMap<Integer,Integer>();
    private Map<Integer,Player> idToPlayerMap =new HashMap<Integer,Player>();
    private TimeStamp time = new TimeStamp();
    //bidding square
    private List<Adventurer> currAvailableAdventurers = new ArrayList<Adventurer>();
    private List<Monster> currAvailableMonsters = new ArrayList<Monster>();
    private List<Trap> currAvailableTraps = new ArrayList<Trap>();
    private List<Room> currAvailableRooms = new ArrayList<Room>();
    //Server connection
    //config
    private int lastPlayerToStartBidding, idCounter;

    private void addPlayer(Player player, int id){
        // TODO: 25.09.22
    }

    public boolean registerPlayer(String name, int CommID){
        // TODO: 25.09.22
        return true;
    }

    public Player getPlayerByCommID(int CommID){
        // TODO: 25.09.22
        Player hari = new Player();
        return hari;
    }

    public Player getPlayerByPlayerID(int PlayerID){
        // TODO: 25.09.22
        Player hari = new Player();
        return hari;
    }

    public int getPlayerIDByCommID(int CommID){
        // TODO: 25.09.22
        return 1;
    }

    public int getCommIDByPlayerID(int PlayerID){
        // TODO: 25.09.22
        return 1;
    }

    //get server connection

    public boolean checkIfRegistered(int CommID){
        // TODO: 25.09.22
        return true;
    }

    public int getNextStartPlayer(){
        // TODO: 25.09.22
        return 1;
    }

    public void drawEntities(){
        // TODO: 25.09.22  
    }

    

}

