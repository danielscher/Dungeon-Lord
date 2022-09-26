package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class TimeStamp {

    private int season, year;

    public int getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }

    public void nextSeason(){
        this.season = season + 1;
    }

    public void nextyear(){
        this.year = year + 1;
    }

    public static class GameData {
        private Map<Integer, Integer> commIDToPlayerIDMap;
        private Map<Integer, Integer> playerIDToCommIDMap;
        private Map<Integer, Player> idToPlayerMap;
        private TimeStamp time;
        //(int_year,int_season),1-4,build season,5-8 combat round
        private BiddingSquare bs;
        private Deque<Adventurer> currAvailableAdventurers;
        private LinkedList<Monster> currAvailableMonsters;
        private LinkedList<Trap> currAvailableTraps;
        private LinkedList<Room> currAvailableRooms;
        private ServerConnection serverConn;
        private Config config;
        private int lastPlayerToStartBidding;
        private int idCounter;
        // next playerID

    // not sure which folder fit.

        private void addPlayer (Player x,int xx){}

        public boolean registerPlayer (String x,int xx){
            return false ;
        }
        //int is commID

        public Player getPlayerByCommID (int x) {

            //return NULL ;
        }


        public Player getPlayerByPlayerID (int x) {

            //return NULL ;
        }

        public int getPlayerIDByCommID (int x) {
            return -1 ;
        }

        public int getCommIDByPlayerID (int x) {
            return -1 ;
        }

        public ServerConnection getServerConnection(){

            //return NUll ;
        }

        public boolean checkIfRegistered (int x) {
            return false ;
            /* int is commID */
        }

        public int getNextStartPlayer (int x) {
            return -1 ;
            // side effect: increments counter
        }

        public void drawEntities() {

        }


    }
}
