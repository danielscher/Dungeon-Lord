package de.unisaarland.cs.se.selab.phase;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
<<<<<<< HEAD
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;
=======
import de.unisaarland.cs.se.selab.game.player.Player;
>>>>>>> 83c99704d1be4bcc0daa8872e806aa4a12c55961

public class GameEndPhase extends Phase {
    private ArrayList <Integer> darkSeedPlayerIDList;
    private ArrayList <Integer> hallsPlayerIDList;
    private ArrayList <Integer> tunnelPlayerIDList;
    private ArrayList <Integer> monsterPlayerIDList;
    private ArrayList <Integer> impsPlayerIDList;
    private ArrayList <Integer> richesPlayerIDList;
    //riches=food+gold
    private ArrayList <Integer> battlePlayerIDList;

    private ArrayList<Integer> winnerPlayerIDList;



    public GameEndPhase(final GameData gd) {
        super(gd);
    }

    public Phase run() throws TimeoutException {
        //TODO
        return  null;
    }

    public void evaluateScores(){
        // use this to calculate the final points and give
        // for(int i=0;i<)
        //  gd.getPlayerByPlayerID()

    }

    private void calculateFoodPoint(){
        for(int i=0; i<gd.getAllPlayerID().size();i++){
           int currFood= gd.getPlayerByPlayerID(i).getFood();
            gd.getPlayerByPlayerID(i).setPoints(currFood);
        }
    }

    private void calculateGoldPoint(){
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int beforePoint= gd.getPlayerByPlayerID(i).getPoints();
            int currGold= gd.getPlayerByPlayerID(i).getGold();
            gd.getPlayerByPlayerID(i).setPoints(currGold+beforePoint);
        }
    }

    public void setRichesTitles(){
        int maxriches= gd.getPlayerByPlayerID(0).getPoints();
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int currRiches= gd.getPlayerByPlayerID(i).getPoints();
            if(currRiches>maxriches){
                maxriches=currRiches;
            }
        }
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int riches= gd.getPlayerByPlayerID(i).getPoints();
            if(riches==maxriches){
                gd.getPlayerByPlayerID(i).addTitle(Title.THE_LORD_OF_RICHES);
                richesPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
            gd.getPlayerByPlayerID(i).setPoints(0);
            //clear the food and gold points since they are no longer needed
        }
    }
    public void setDarkSeedTitles(){
        int MaxEvilMeter= gd.getPlayerByPlayerID(0).getEvilLevel();
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int currEvil= gd.getPlayerByPlayerID(i).getEvilLevel();
            if(currEvil>MaxEvilMeter){
                MaxEvilMeter=currEvil;
            }
        } //find the player with max evil level
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int Evil= gd.getPlayerByPlayerID(i).getEvilLevel();
            if(Evil==MaxEvilMeter){
                gd.getPlayerByPlayerID(i).addTitle(Title.THE_LORD_OF_DARK_DEEDS);
                richesPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setHallsTitles(){
        // need method from Player/Dungeon
        int maxRooms= gd.getPlayerByPlayerID(0).getDungeon().getTotalDefuseVal();
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int currRooms= gd.getPlayerByPlayerID(i).getEvilLevel();
            if(currRooms>maxRooms){
                maxRooms=currRooms;
            }
        } //find the player with most rooms
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int rooms= gd.getPlayerByPlayerID(i).getEvilLevel();
            if(rooms==maxRooms){
                gd.getPlayerByPlayerID(i).addTitle(Title.THE_LORD_OF_HALLS);
                hallsPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setTunnelTitles(){

    }

    public void setMonsterTitles(){
        int maxHiredMonsters= gd.getPlayerByPlayerID(0).getDungeon().getHiredMonsters().size();
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int currMonsterNum= gd.getPlayerByPlayerID(i).getDungeon().getHiredMonsters().size();
            if(currMonsterNum>maxHiredMonsters){
                maxHiredMonsters=currMonsterNum;
            }
        } //find the player with most monsters
        for(int i=0; i<gd.getAllPlayerID().size();i++){
            int monsterNum= gd.getPlayerByPlayerID(i).getDungeon().getHiredMonsters().size();
            if(monsterNum==maxHiredMonsters){
                gd.getPlayerByPlayerID(i).addTitle(Title.THE_MONSTER_LORD);
                monsterPlayerIDList.get(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setImpsTitles(){}

    public void setBattlelordTitles(){}




}
