package de.unisaarland.cs.se.selab;
import de.unisaarland.cs.se.selab.comm.ServerConnection;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import java.util.*;
public class Config {
    private String configFilePath;
    private int maxPlayer, maxYear;
    private LinkedList<Monster> monsters;
    private LinkedList<Adventurer> adventurers;
    private LinkedList<Trap> traps;
    private LinkedList<Room> rooms;

    private boolean parse(){
        return false;
    }

    private boolean checkIfValid(){
        return false;
    }

    private Monster createMonster(/*...need parameter*/){

       // return NULL;
    }

    private Adventurer createAdventurer(/*...need parameter*/){

        //return NULL;
    }

    private void shuffle(){}

    public List<Adventurer> drawAdventurers( int amount){

        //return NULL;
    }
// i think we still need getter to pass the data to GameData class.
}
