package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.comm.ServerConnection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class TimeStamp {

    private int season, year;

    public TimeStamp(){
        this.season = 1;
        this.year = 1;
    }

    public int getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }

    public void nextSeason() {
        this.season = season + 1;
    }

    public void nextyear() {
        this.year = year + 1;
    }

  
}
