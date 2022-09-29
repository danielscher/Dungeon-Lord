package de.unisaarland.cs.se.selab.phase;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;
import java.util.List;


public class GameEndPhase extends Phase {

    private final List<Integer> darkSeedPlayerIDList = new ArrayList<>();
    private final List<Integer> hallsPlayerIDList = new ArrayList<>();
    private final List<Integer> tunnelPlayerIDList = new ArrayList<>();
    private final List<Integer> monsterPlayerIDList = new ArrayList<>();
    private final List<Integer> impsPlayerIDList = new ArrayList<>();
    private final List<Integer> richesPlayerIDList = new ArrayList<>();
    //riches=food+gold
    private final List<Integer> battlePlayerIDList = new ArrayList<>();
    private final List<Integer> winnerPlayerIDList = new ArrayList<>();


    public GameEndPhase(final GameData gd) {
        super(gd);
    }

    @Override
    public Phase run() throws TimeoutException {
        //TODO
        return null;
    }

    public void setAllTitle() {
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
    }

    public void evaluateScoresNoTitle() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int hiredMonsterPoints = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters()
                    .size();
            final int roomsPoints = 2 * gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            final int conqueredTilePoints =
                    (-2) * gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            final int imprisonedAdvPoints =
                    2 * gd.getPlayerByPlayerId(i).getDungeon().getNumImprisonedAdventurers();
            gd.getPlayerByPlayerId(i).setPoints(0);
            final int tempPoints = hiredMonsterPoints + roomsPoints + conqueredTilePoints
                    + imprisonedAdvPoints;
            // points without titles
            gd.getPlayerByPlayerId(i).setPoints(tempPoints);
        } // set points with no title
    }

    public void evaluateScoresWithTitleDarkSeed() {
        if (darkSeedPlayerIDList.size() == 1) {
            final int currPlayerId = darkSeedPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : darkSeedPlayerIDList) {
                final int currPlayerId = darkSeedPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleRiches() {
        if (richesPlayerIDList.size() == 1) {
            final int currPlayerId = richesPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : richesPlayerIDList) {
                final int currPlayerId = richesPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleImps() {
        if (impsPlayerIDList.size() == 1) {
            final int currPlayerId = impsPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : impsPlayerIDList) {
                final int currPlayerId = impsPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleHalls() {
        if (hallsPlayerIDList.size() == 1) {
            final int currPlayerId = hallsPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : hallsPlayerIDList) {
                final int currPlayerId = hallsPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleBattle() {
        if (battlePlayerIDList.size() == 1) {
            final int currPlayerId = battlePlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : battlePlayerIDList) {
                final int currPlayerId = battlePlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleTunnel() {
        if (tunnelPlayerIDList.size() == 1) {
            final int currPlayerId = tunnelPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : tunnelPlayerIDList) {
                final int currPlayerId = tunnelPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleMonster() {
        if (monsterPlayerIDList.size() == 1) {
            final int currPlayerId = monsterPlayerIDList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : monsterPlayerIDList) {
                final int currPlayerId = monsterPlayerIDList.get(i);
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void calculateWinner() {  // set the winner to the list:
        int maxScores = gd.getPlayerByPlayerId(0).getPoints();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int currScores = gd.getPlayerByPlayerId(i).getPoints();
            if (currScores > maxScores) {
                maxScores = currScores;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int scores = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (scores == maxScores) {
                this.winnerPlayerIDList.add(i);
                // add player to the winner list
            }
        }

        for (final int i : winnerPlayerIDList) {
            final int winnerId = winnerPlayerIDList.get(i);
            final int winnerPoints = gd.getPlayerByPlayerId(winnerId).getPoints();
            broadcastGameEnd(winnerId, winnerPoints);
        }

    }

    private void calculateFoodPoint() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int currFood = gd.getPlayerByPlayerId(i).getFood();
            gd.getPlayerByPlayerId(i).setPoints(currFood);
        }
    }

    private void calculateGoldPoint() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int beforePoint = gd.getPlayerByPlayerId(i).getPoints();
            final int currGold = gd.getPlayerByPlayerId(i).getGold();
            gd.getPlayerByPlayerId(i).setPoints(currGold + beforePoint);
        }
    }

    public void setRichesTitles() {
        int maxriches = gd.getPlayerByPlayerId(0).getPoints();
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int currRiches = gd.getPlayerByPlayerId(i).getPoints();
            if (currRiches > maxriches) {
                maxriches = currRiches;
            }
        }
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int riches = gd.getPlayerByPlayerId(i).getPoints();
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
            final int currEvil = gd.getPlayerByPlayerId(i).getEvilLevel();
            if (currEvil > maxEvilMeter) {
                maxEvilMeter = currEvil;
            }
        } //find the player with max evil level
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int evil = gd.getPlayerByPlayerId(i).getEvilLevel();
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
            final int currRooms = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (currRooms > maxRooms) {
                maxRooms = currRooms;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int rooms = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            if (rooms == maxRooms) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_HALLS);
                hallsPlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setTunnelTitles() {
        // get the number of the rooms for player0
        final int mroom = gd.getPlayerByPlayerId(0).getDungeon().getNumRooms();
        final int muncontile = gd.getPlayerByPlayerId(0).getDungeon().getNumUnconqueredTiles();
        final int mcontile = gd.getPlayerByPlayerId(0).getDungeon().getNumConqueredTiles();
        int maxTiles = mcontile + muncontile - mroom;
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int currRoom = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            final int currUncontile = gd.getPlayerByPlayerId(i).getDungeon()
                    .getNumUnconqueredTiles();
            final int currContile = gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            final int currTileNum = currContile + currUncontile - currRoom;
            if (currTileNum > maxTiles) {
                maxTiles = currTileNum;
            }
        } //find the player with most rooms
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int room = gd.getPlayerByPlayerId(i).getDungeon().getNumRooms();
            final int uncontile = gd.getPlayerByPlayerId(i).getDungeon().getNumUnconqueredTiles();
            final int contile = gd.getPlayerByPlayerId(i).getDungeon().getNumConqueredTiles();
            final int tileNum = contile + uncontile - room;
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
            final int currMonsterNum = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters()
                    .size();
            if (currMonsterNum > maxHiredMonsters) {
                maxHiredMonsters = currMonsterNum;
            }
        } //find the player with most monsters
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int monsterNum = gd.getPlayerByPlayerId(i).getDungeon().getHiredMonsters().size();
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
            final int currImpsNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
            if (currImpsNum > maxImps) {
                maxImps = currImpsNum;
            }
        } //find the player with most imps
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int impsNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
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
            final int currUnconqueredTileNum = gd.getPlayerByPlayerId(i).getDungeon()
                    .getNumUnconqueredTiles();
            if (currUnconqueredTileNum > maxUnconqueredTilesNum) {
                maxUnconqueredTilesNum = currUnconqueredTileNum;
            }
        } //find the player with most imps
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int unconqueredTileNum = gd.getPlayerByPlayerId(i).getDungeon().getNumImps();
            if (unconqueredTileNum == maxUnconqueredTilesNum) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_BATTLELORD);
                battlePlayerIDList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public List<Integer> getPlayersID(final GameData gd) {
        return gd.getAllPlayerID();

    }

    public List<Integer> getWinnerPlayerIDList() {
        return winnerPlayerIDList;
    }
}
