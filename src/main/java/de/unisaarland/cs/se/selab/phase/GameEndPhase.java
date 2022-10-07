package de.unisaarland.cs.se.selab.phase;


import de.unisaarland.cs.se.selab.game.GameData;
import de.unisaarland.cs.se.selab.game.util.Title;
import java.util.ArrayList;
import java.util.List;


public class GameEndPhase extends Phase {

    private final List<Integer> darkSeedPlayerIdList = new ArrayList<>();
    private final List<Integer> hallsPlayerIdList = new ArrayList<>();
    private final List<Integer> tunnelPlayerIdList = new ArrayList<>();
    private final List<Integer> monsterPlayerIdList = new ArrayList<>();
    private final List<Integer> impsPlayerIdList = new ArrayList<>();
    private final List<Integer> richesPlayerIdList = new ArrayList<>();
    //riches=food+gold
    private final List<Integer> battlePlayerIdList = new ArrayList<>();
    private final List<Integer> winnerPlayerIdList = new ArrayList<>();


    public GameEndPhase(final GameData gd) {
        super(gd);
    }

    @Override
    public Phase run() {
        //TODO
        setAllTitle();
        setAllPoints();
        broadcastWinner();
        return null;
    }

    @Override
    public void gotInvalidActionFrom(final int commID) {
        // nobody is supposed to get an actNow here
    }

    public void setAllTitle() {
        this.setRichesTitles();
        this.setDarkSeedTitles();
        this.setHallsTitles();
        this.setTunnelTitles();
        this.setMonsterTitles();
        this.setImpsTitles();
        this.setBattleLordTitles();
        // give all titles to corresponding players, and now evaluate scores.
    }

    public void setAllPoints() {
        initializeAllPoints();
        evaluateScoresNoTitle();
        evaluateScoresWithTitleMonster();
        evaluateScoresWithTitleDarkSeed();
        evaluateScoresWithTitleBattle();
        evaluateScoresWithTitleImps();
        evaluateScoresWithTitleRiches();
        evaluateScoresWithTitleTunnel();
        evaluateScoresWithTitleHalls();
        evaluateWinner();
    }

    public void initializeAllPoints() {
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            gd.getPlayerByPlayerId(i).setPoints(0);
            // clear all the points, only use it before the eval points.
        }
    }

    // evaluate methods below just calculate the points one by one.
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
        if (darkSeedPlayerIdList.size() == 1) {
            final int currPlayerId = darkSeedPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : darkSeedPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleRiches() {
        if (richesPlayerIdList.size() == 1) {
            final int currPlayerId = richesPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : richesPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleImps() {
        if (impsPlayerIdList.size() == 1) {
            final int currPlayerId = impsPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : impsPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleHalls() {
        if (hallsPlayerIdList.size() == 1) {
            final int currPlayerId = hallsPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : hallsPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleBattle() {
        if (battlePlayerIdList.size() == 1) {
            final int currPlayerId = battlePlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : battlePlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleTunnel() {
        if (tunnelPlayerIdList.size() == 1) {
            final int currPlayerId = tunnelPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : tunnelPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateScoresWithTitleMonster() {
        if (monsterPlayerIdList.size() == 1) {
            final int currPlayerId = monsterPlayerIdList.get(0);
            final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
            gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 3);
        } else {
            for (final int i : monsterPlayerIdList) {
                final int currPlayerId = i;
                final int currPoints = gd.getPlayerByPlayerId(currPlayerId).getPoints();
                gd.getPlayerByPlayerId(currPlayerId).setPoints(currPoints + 2);
            }
        }
    }

    public void evaluateWinner() {  // set the winner to the list:
        int maxScores = gd.getPlayerByPlayerId(0).getPoints();
        for (int i = 0; i < gd.getNumCurrPlayers(); i++) {
            final int currScores = gd.getPlayerByPlayerId(i).getPoints();
            if (currScores > maxScores) {
                maxScores = currScores;
            }
        } //find the player with max points
        for (int i = 0; i < gd.getNumCurrPlayers(); i++) {
            final int scores = gd.getPlayerByPlayerId(i).getPoints();
            if (scores == maxScores) {
                this.winnerPlayerIdList.add(i);
                // add player to the winner list
            }
        }
    }

    public void broadcastWinner() {
        for (final int i : winnerPlayerIdList) {
            final int winnerId = i;
            final int winnerPoints = gd.getPlayerByPlayerId(winnerId).getPoints();
            broadcastGameEnd(winnerId, winnerPoints);
        }
    }

    // riches need gold+foods, and i use points field to store the value, which need to be clear

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
        this.calculateFoodPoint();
        this.calculateGoldPoint();
        int maxRiches = -1;
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int currRiches = gd.getPlayerByPlayerId(i).getPoints();
            if (currRiches > maxRiches) {
                maxRiches = currRiches;
            }
        }
        for (int i = 0; i < gd.getAllPlayerID().size(); i++) {
            final int riches = gd.getPlayerByPlayerId(i).getPoints();
            if (riches == maxRiches) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_LORD_OF_RICHES);
                richesPlayerIdList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
            gd.getPlayerByPlayerId(i).setPoints(0);
            //clear the food and gold points since they are no longer needed
        }
    }

    public void setDarkSeedTitles() {
        int maxEvilMeter = -1;
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
                richesPlayerIdList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setHallsTitles() {
        // get the number of the rooms for player0
        int maxRooms = -1;
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
                hallsPlayerIdList.add(i);
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
                tunnelPlayerIdList.add(i);
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
                monsterPlayerIdList.add(i);
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
                impsPlayerIdList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public void setBattleLordTitles() {
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
            final int unconqueredTileNum = gd.getPlayerByPlayerId(i).getDungeon()
                    .getNumUnconqueredTiles();
            if (unconqueredTileNum == maxUnconqueredTilesNum) {
                gd.getPlayerByPlayerId(i).addTitle(Title.THE_BATTLELORD);
                battlePlayerIdList.add(i);
                // add the title to players and add playerID to the list(later to check tie)
            }
        }
    }

    public List<Integer> getPlayersId(final GameData gd) {
        return gd.getAllPlayerID();
    }

    public List<Integer> getWinnerPlayerIdList() {
        return winnerPlayerIdList;
    }
}
