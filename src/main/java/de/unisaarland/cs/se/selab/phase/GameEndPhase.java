package de.unisaarland.cs.se.selab.phase;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;
import java.util.List;


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
        // use this to calculate the final points and give titles
        this.calculateFoodPoint();
        this.calculateGoldPoint();
        this.setRichesTitles();
        // the above order is important
        this.setDarkSeedTitles();
        this.setHallsTitles();
        this.setTunnelTitles();
        this.setMonsterTitles();
        this.setImpsTitles();
        this.setBattlelordTitles();
        // give all titles to corresponding players, and now evaluate scores.
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int hiredMonsterPoints = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters()
                    .size();
            int roomsPoints = 2 * gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            int conqueredTilePoints =
                    (-2) * gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            int imprisonedAdventurerPoints =
                    2 * gd.getPlayerByPlayerId(i).getDungeon().getNumImprisonedAdventurers();
            gd.getPlayerByPlayerId(i).setPoints(0);
            int tempPoints = hiredMonsterPoints + roomsPoints + conqueredTilePoints
                    + imprisonedAdventurerPoints;
            // points without titles
            gd.getPlayerByPlayerId(i).setPoints(tempPoints);

            // below use a for loop to eval the title point of each player
            for (Title t : gd.getPlayerByPlayerId(i).getTitles()) {
                if (t == Title.THE_BATTLELORD && battlePlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_BATTLELORD && battlePlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_LORD_OF_DARK_DEEDS && darkSeedPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_LORD_OF_DARK_DEEDS && darkSeedPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_LORD_OF_HALLS && hallsPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_LORD_OF_HALLS && hallsPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_TUNNEL_LORD && tunnelPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_TUNNEL_LORD && tunnelPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_MONSTER_LORD && monsterPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_MONSTER_LORD && monsterPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_LORD_OF_IMPS && impsPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_LORD_OF_IMPS && impsPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }

                if (t == Title.THE_LORD_OF_RICHES && richesPlayerIDList.size() == 1) {
                    int p = gd.getPlayerByPlayerId(i).getPoints();
                    p = p + 3;
                    gd.getPlayerByPlayerId(i).setPoints(p);
                } else if (t == Title.THE_LORD_OF_RICHES && richesPlayerIDList.size() > 1) {
                    int ptie = gd.getPlayerByPlayerId(i).getPoints();
                    ptie = ptie + 2;
                    gd.getPlayerByPlayerId(i).setPoints(ptie);
                }
            } // end of each player
        } // end of all players
        // set the winner to the list:
        int maxScores = gd.getPlayerByPlayerId(0).getPoints();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currScores = gd.getPlayerByPlayerId(i).getPoints();
            if (currScores > maxScores) {
                maxScores = currScores;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int scores = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (scores == maxScores) {
                this.winnerPlayerIDList.add(i);
                // add player to the winner list
            }
        }

        for (int i = 0; i < winnerPlayerIDList.size(); i++) {
            int winnerId = winnerPlayerIDList.get(i);
            int winnerPoints = gd.getPlayerByPlayerId(winnerId).getPoints();
            broadcastGameEnd(winnerId, winnerPoints);
        }

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
                richesPlayerIDList.add(i);
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
                richesPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setHallsTitles() {
        // get the number of the rooms for player0
        int maxRooms = gd.getPlayerByPlayerId(0).getDungeon().getNumRooms();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currRooms = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (currRooms > maxRooms) {
                maxRooms = currRooms;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int rooms = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (rooms == maxRooms) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_HALLS);
                hallsPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setTunnelTitles() {
        // get the number of the rooms for player0
        int mroom = gd.getPlayerByPlayerId(0).getDungeon().getNumRooms();
        int muncontile = gd.getPlayerByPlayerId(0).getDungeon().getNumUnconqueredTiles();
        int mcontile = gd.getPlayerByPlayerId(0).getDungeon().getNumConqueredTiles();
        int maxTiles = mcontile + muncontile - mroom;
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currRoom = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            int currUncontile = gd.getPlayerByPlayerId(i).getDungeon().getNumUnconqueredTiles();
            int currContile = gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            int currTileNum = currContile + currUncontile - currRoom;
            if (currTileNum > maxTiles) {
                maxTiles = currTileNum;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int room = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            int uncontile = gd.getPlayerByPlayerId(i).getDungeon().getNumUnconqueredTiles();
            int contile = gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            int tileNum = contile + uncontile - room;
            if (tileNum == maxTiles) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_TUNNEL_LORD);
                tunnelPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
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
                monsterPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setImpsTitles() {
        int maxImps = gd.getPlayerByPlayerId(0).getDungeon().getNumImps();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currImpsNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
            if (currImpsNum > maxImps) {
                maxImps = currImpsNum;
            }
        } //find the player with most imps
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int impsNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
            if (impsNum == maxImps) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_IMPS);
                impsPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setBattlelordTitles() {
        int maxUnconqueredTilesNum = gd.getPlayerByPlayerId(0).getDungeon()
                .getNumUnconqueredTiles();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int currUnconqueredTileNum = gd.getPlayerByPlayerId(i).getDungeon()
                    .getNumUnconqueredTiles();
            if (currUnconqueredTileNum > maxUnconqueredTilesNum) {
                maxUnconqueredTilesNum = currUnconqueredTileNum;
            }
        } //find the player with most imps
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            int unconqueredTileNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
            if (unconqueredTileNum == maxUnconqueredTilesNum) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_BATTLELORD);
                battlePlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public List<Integer> getPlayersID(GameData gd) {
        return gd.getAllPlayerID();

    }

}
