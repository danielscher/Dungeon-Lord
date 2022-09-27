package de.unisaarland.cs.se.selab.phase;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;


public class GameEndPhase extends Phase {

    private ArrayList<Integer> darkSeedPlayerIDList;
    private ArrayList<Integer> hallsPlayerIDList;
    private ArrayList<Integer> tunnelPlayerIDList;
    private ArrayList<Integer> monsterPlayerIDList;
    private ArrayList<Integer> impsPlayerIDList;
    private ArrayList<Integer> richesPlayerIDList;
    //riches=food+gold
    private ArrayList<Integer> battlePlayerIDList;

    private ArrayList<Integer> winnerPlayerIDList;


    public GameEndPhase(final GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        //TODO
        return null;
    }

    public void evaluateScores() {
        // use this to calculate the final points and give
        // for(int i=0;i<)
        //  gd.getPlayerByPlayerId()
    }

    private void calculateFoodPoint() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currFood = gd.getPlayerByPlayerId(i).getFood();
            gd.getPlayerByPlayerId(i).setPoints(currFood);
        }
    }

    private void calculateGoldPoint() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int beforePoint = gd.getPlayerByPlayerId(i).getPoints();
            int currGold = gd.getPlayerByPlayerId(i).getGold();
            gd.getPlayerByPlayerId(i).setPoints(currGold + beforePoint);
        }
    }

    public void setRichesTitles() {
        int maxriches = gd.getPlayerByPlayerId(0).getPoints();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currRiches = gd.getPlayerByPlayerId(i).getPoints();
            if (currRiches > maxriches) {
                maxriches = currRiches;
            }
        }
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int riches = gd.getPlayerByPlayerId(i).getPoints();
            if (riches == maxriches) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_RICHES);
                richesPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
            gd.getPlayerByPlayerId(i).setPoints(0);
            //clear the food and gold points since they are no longer needed
        }
    }

    public void setDarkSeedTitles() {
        int maxEvilMeter = gd.getPlayerByPlayerId(0).getEvilLevel();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currEvil = gd.getPlayerByPlayerId(i).getEvilLevel();
            if (currEvil > maxEvilMeter) {
                maxEvilMeter = currEvil;
            }
        } //find the player with max evil level
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int evil = gd.getPlayerByPlayerId(i).getEvilLevel();
            if (evil == maxEvilMeter) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_DARK_DEEDS);
                richesPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setHallsTitles() {
        // need method from Player/Dungeon
        int maxRooms = gd.getPlayerByPlayerId(0).getDungeon().getTotalDefuseVal();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currRooms = gd.getPlayerByPlayerId(i).getEvilLevel();
            if (currRooms > maxRooms) {
                maxRooms = currRooms;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int rooms = gd.getPlayerByPlayerId(i).getEvilLevel();
            if (rooms == maxRooms) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_HALLS);
                hallsPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setTunnelTitles() {

    }

    public void setMonsterTitles() {
        int maxHiredMonsters = gd.getPlayerByPlayerId(0).getDungeon().getHiredMonsters().size();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currMonsterNum = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters().size();
            if (currMonsterNum > maxHiredMonsters) {
                maxHiredMonsters = currMonsterNum;
            }
        } //find the player with most monsters
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int monsterNum = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters().size();
            if (monsterNum == maxHiredMonsters) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_MONSTER_LORD);
                monsterPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setImpsTitles() {
    }

    public void setBattlelordTitles() {
    }


}
