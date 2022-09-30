package de.unisaarland.cs.se.selab.game;

public class TimeStamp {

    private int season;
    private int year;

    public TimeStamp() {
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
        this.season = 1;
        this.year = year + 1;
    }

    public void timeTravel(){
        this.season = 5;
    }

}
