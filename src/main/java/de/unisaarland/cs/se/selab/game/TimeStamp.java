package de.unisaarland.cs.se.selab.game;

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

}
