package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class Config {

    private String configFilePath;
    private int maxPlayer = -1;
    private int maxYear = -1;
    private int dungeonSidelength = -1;
    private int initFood = -1;
    private int initGold = -1;
    private int initImps = -1;
    private int parserResult;

    private List<Monster> monsters;
    private List<Adventurer> adventurers;
    private List<Trap> traps;
    private List<Room> rooms;

    private JSONObject obj;

    final Logger log = Logger.getLogger(Config.class.getName());

    public void setConfigFilePath(final String s) {
        this.configFilePath = s;
    }

    public void displayLog() {
    //  final Logger log = Logger.getLogger(Config.class.getName());
        log.fine("Help and Log: " + "/n");
        log.fine("error in Monsters: -1,10,11,12 " + "/n");
        log.fine("error in Adventurers: -2,20,21,22 " + "/n");
        log.fine("error in Traps: -3,30,31,32 " + "/n");
        log.fine("error in Rooms: -4,40,41,42 " + "/n");
        log.fine("error in general Informations: -5,-6 " + "/n");
        log.fine("maxYear =" + maxYear + "/n");
        log.fine("maxPlayer =" + maxPlayer + "/n");
        log.fine("number of rooms =" + rooms.size() + "/n");
        log.fine("number of traps =" + traps.size() + "/n");
        log.fine("number of adventurers =" + adventurers.size() + "/n");
        log.fine("number of monsters =" + monsters.size() + "/n");
        log.fine("invalid infomation in =" + parserResult + "/n");
        /*
        System.out.println("Help and Log: " + "/n");
        System.out.println("error in Monsters: -1,10,11,12 " + "/n");
        System.out.println("error in Adventurers: -2,20,21,22 " + "/n");
        System.out.println("error in Traps: -3,30,31,32 " + "/n");
        System.out.println("error in Rooms: -4,40,41,42 " + "/n");
        System.out.println("error in general Informations: -5,-6 " + "/n");
        System.out.println("maxYear =" + maxYear + "/n");
        System.out.println("maxPlayer =" + maxPlayer + "/n");
        System.out.println("number of rooms =" + rooms.size() + "/n");
        System.out.println("number of traps =" + traps.size() + "/n");
        System.out.println("number of adventurers =" + adventurers.size() + "/n");
        System.out.println("number of monsters =" + monsters.size() + "/n");
        System.out.println("invalid infomation in =" + parserResult + "/n");
        */

    }

    public Attack convertStringToAttack(final String s) {
    //   Attack attackStrategy = Attack.valueOf(s);
        return Attack.valueOf(s);
    }

    public Location convertStringToLocation(final String s) {
    //    Location loc = Location.valueOf(s);
        return Location.valueOf(s);
    }

    public void parse(final String configFilePath) throws FileNotFoundException {
        this.parseFromFile(configFilePath);
        parserAndcheckMonsterToList(obj);
        parserAndcheckAdventurerToList(obj);
        parserAndcheckTrapToList(obj);
        parserAndcheckRoomToList(obj);
        checkGeneralInfoValid();
        checkUniqueId();
        displayLog();
    }

    public int parseFromFile(final String configFilePath) throws FileNotFoundException {
        parserResult = 0;
        final StringBuilder builder = new StringBuilder();
    //    try (BufferedReader br = new BufferedReader(new FileReader(configFilePath))) {

        try (BufferedReader br = Files.newBufferedReader(Paths.get(configFilePath), StandardCharsets.UTF_8)) {
            String s;
            while ((s = br.readLine()) != null) {
                builder.append(s);
            }

        } catch (IOException e) {
            // e.printStackTrace();
            log.fine(e.toString());

        }
        final String allJSONdata = builder.toString();
          System.out.println(allJSONdata);
        obj = new JSONObject(allJSONdata);

        maxYear = obj.getInt("years");
        maxPlayer = obj.getInt("maxPlayers");
        dungeonSidelength = obj.getInt("dungeonSideLength");
        initFood = obj.getInt("initialFood");
        initGold = obj.getInt("initialGold");
        initImps = obj.getInt("initialImps");
        // end of parse to json object
        return parserResult;
    }

    public int parserAndcheckMonsterToList(final JSONObject obj) {
        final JSONArray monsterArray = obj.getJSONArray("monsters");
        final int monArrLen = monsterArray.length();
        monsters = new ArrayList<Monster>();
        for (int i = 0; i < monArrLen; i++) {
            final JSONObject monsterObj = monsterArray.getJSONObject(i);
            final int id = (Integer) monsterObj.get("id");
        //  final String name = (String) monsterObj.get("name");
            int hunger = 0;
            try {
                //  String hungerValString = monsterObj.getString("hunger");
                //  hunger = Integer.valueOf(hungerValString); //(Integer) monsterObj.get("hunger");
                hunger = monsterObj.getInt("hunger");
            } catch (Exception e) {
                hunger = 0;
                //    System.out.println("this monster does not have hunger"); do nothing
            }

            int evilness = 0;
            try {
                //    String evilnessValString = monsterObj.getString("evilness");
                //    evilness = Integer.valueOf(evilnessValString);
                evilness = monsterObj.getInt("evilness");
            } catch (Exception e) {
                evilness = 0;
                //    System.out.println("this monster does not have evilness"); do nothing
            }

            //  int evilness = (Integer) monsterObj.get("evilness");
            int damage = (Integer) monsterObj.get("damage");
            //   Attack attackStrategy = (Attack) monsterObj.get("attackStrategy");
            String attackStrategyString = (String) monsterObj.get("attackStrategy");
            // Attack attackStrategy = Attack.valueOf(attackStrategyString);

            Attack attackStrategy = convertStringToAttack(attackStrategyString);
            if (evilness < 0 || hunger < 0) {
                parserResult = 11;
                break;
            }
            // invalid evil/hunger

            if (damage < 1 || attackStrategy == null) {
                parserResult = 12;
                break;
            }
            // valid damage, attack not set
            final  Monster currMonster = new Monster(id, hunger, evilness, damage, attackStrategy);
            monsters.add(currMonster);
        } //end of monster for loop+ add to list
        return parserResult;
    }

    public int parserAndcheckAdventurerToList(JSONObject obj) {
        JSONArray adventurerArray = obj.getJSONArray("adventurers");
        int advArrLen = adventurerArray.length();
        adventurers = new ArrayList<Adventurer>();
        for (int i = 0; i < advArrLen; i++) {
            JSONObject adventurerObj = adventurerArray.getJSONObject(i);
            final int id = (Integer) adventurerObj.get("id");
        //  String name = (String) adventurerObj.get("name");
            //  int healValue = (Integer) adventurerObj.get("healValue");
            int healValue = 0;
            try {
                healValue = adventurerObj.getInt("healValue");
            } catch (Exception e) {
                healValue = 0;
                //    System.out.println("not a priest");
            }

            //  int defuseValue = (Integer) adventurerObj.get("defuseValue");
            int defuseValue = 0;
            try {
                defuseValue = adventurerObj.getInt("defuseValue");
            } catch (Exception e) {
                //    System.out.println("not a thief");
                defuseValue = 0;
            }

            //   boolean charge = (Boolean) adventurerObj.get("charge");
            boolean charge = false;
            try {
                charge = adventurerObj.getBoolean("charge");
            } catch (Exception e) {
                //    System.out.println("not in charge");
                charge = false;
            }

            int difficulty = adventurerObj.getInt("difficulty");
            int healthPoints = adventurerObj.getInt("healthPoints");

            if (difficulty < 0 || difficulty > 8) {
                parserResult = 21;
                break;
            }

            if (healthPoints < 1 || healValue < 0 || defuseValue < 0) {
                parserResult = 22;
                break;
            }
            // invalid value of an adventurer

            if (healValue > 0 && defuseValue > 0) {
                parserResult = 23;
                break;
            }
            // cannot be priest and thief at the same time
            final Adventurer currAdventurer = new Adventurer(id, difficulty, healthPoints,
                    healValue, defuseValue, charge);
            adventurers.add(currAdventurer);
        } //end of the adventurer forloop +++ add to list
        return parserResult;
    }

    public int parserAndcheckTrapToList(JSONObject obj) {
        JSONArray trapArray = obj.getJSONArray("traps");
        int trpArrLen = trapArray.length();
        traps = new ArrayList<Trap>();
        for (int i = 0; i < trpArrLen; i++) {
            JSONObject trapObj = trapArray.getJSONObject(i);
            int id = (Integer) trapObj.get("id");
        //  String name = (String) trapObj.get("name");
            int damage = (Integer) trapObj.get("damage");
            //  Attack attackStrategy = (Attack) trapObj.get("attackStrategy");
            String attackStrategyString = (String) trapObj.get("attackStrategy");
            Attack attackStrategy = convertStringToAttack(attackStrategyString);

            //    int target = (Integer) trapObj.get("target");
            int target = 0;
            try {
                //    String targetValString = trapObj.getString("target");
                //    target = Integer.valueOf(targetValString);
                target = trapObj.getInt("target");
                //    System.out.println("traget string and value?"+target);
            } catch (Exception e) {
                target = 0;
            }

            if (id < 0 || damage < 1) {
                parserResult = 31;
                break;
            }
            //traps should have valid damage and id
            if (attackStrategy == Attack.TARGETED && (target < 1 || target > 3)) {
                parserResult = 32;
                break;
            }
            //target traps should have 1,2,3 as goals.
            final Trap currTrap = new Trap(id, damage, target, attackStrategy);
            traps.add(currTrap);
        } // end of traps forloop
        return parserResult;
    }

    public int parserAndcheckRoomToList(JSONObject obj) {
        JSONArray roomArray = obj.getJSONArray("rooms");
        int roomArrLen = roomArray.length();
        rooms = new ArrayList<Room>();
        for (int i = 0; i < roomArrLen; i++) {
            JSONObject roomObj = roomArray.getJSONObject(i);
        //    String name = (String) roomObj.get("name");
            //  int foodProduction = (Integer) roomObj.get("food");
            int foodProduction = 0;
            try {
                foodProduction = roomObj.getInt("food");
            } catch (Exception e) {
                //    System.out.println("not produce food");
                foodProduction = 0;
            }

            //    int goldProduction = (Integer) roomObj.get("gold");
            int goldProduction = 0;
            try {

                goldProduction = roomObj.getInt("gold");
            } catch (Exception e) {
                //    System.out.println("not produce gold");
                goldProduction = 0;
            }
            //    int impProduction = (Integer) roomObj.get("imps");
            int impProduction = 0;
            try {
                impProduction = roomObj.getInt("imps");
            } catch (Exception e) {
                //    System.out.println("not produce imps");
                impProduction = 0;
            }
            //    int niceness = (Integer) roomObj.get("niceness");
            int niceness = 0;
            try {

                niceness = roomObj.getInt("niceness");
            } catch (Exception e) {
                //    System.out.println("no niceness");
                niceness = 0;
            }
            int id = (Integer) roomObj.get("id");

            if (id < 0) {
                parserResult = 41;
                break;
            }
            int activationCost = (Integer) roomObj.get("activation");
            //    Location placementLoc = (Location) roomObj.get("restriction");
            String locationString = (String) roomObj.get("restriction");
            Location placementLoc = convertStringToLocation(locationString);
            final Room currRoom = new Room(id, activationCost, foodProduction, goldProduction,
                    impProduction, niceness, placementLoc);
            rooms.add(currRoom);
        } // end of rooms

        return parserResult;
    }


    public int checkGeneralInfoValid() {
        //not enough years/player
        if (maxYear < 1 && maxPlayer < 1) {
            parserResult = -5;
        }

        if (traps.size() < 4 * 4 * maxYear) {
            parserResult = -3;
            //not enough traps
        }

        if (rooms.size() < 2 * 4 * maxYear) {
            parserResult = -4;
            //not enough rooms
        }

        if (monsters.size() < 3 * 4 * maxYear) {
            parserResult = -1;
            //not enough monsters
        }
        if (adventurers.size() < 3 * 3 * maxPlayer) {
            parserResult = -2;
            //not enough rooms
        }

        if (dungeonSidelength < 1 || dungeonSidelength > 15) {
            parserResult = -6;
            // dungeon too small or big
        }
        if (initGold < 0 || initFood < 0 || initImps < 0 || configFilePath==null) {
            parserResult = -7;
            // init number invalid
        }

        // !!! try to check the duplicate elements, but get error...
        //   HashSet<Trap> checkTraps = new HashSet<>(Arrays.asList(traps));

        return parserResult;
    }

    public int checkUniqueId() {
        final Set<Integer> monsterIds = new HashSet<>(
                monsters.size()); // setting maxCapacity a priori increases performance
        for (final Monster m : monsters) {
            monsterIds.add(m.getMonsterID());
        }
        if (monsterIds.size() != monsters.size()) {
            parserResult = 10;
        }
        final Set<Integer> adventurerIds = new HashSet<>(
                adventurers.size());
        for (final Adventurer a : adventurers) {
            adventurerIds.add(a.getAdventurerID());
        }
        if (adventurerIds.size() != adventurers.size()) {
            parserResult = 20;
        }

        final Set<Integer> trapIds = new HashSet<>(
                traps.size());
        for (final Trap t : traps) {
            trapIds.add(t.getTrapID());
        }
        if (trapIds.size() != traps.size()) {
            parserResult = 30;
        }

        final Set<Integer> roomIds = new HashSet<>(
                rooms.size());
        for (final Room r : rooms) {
            roomIds.add(r.getRoomID());
        }
        if (roomIds.size() != rooms.size()) {
            parserResult = 40;
        }
        return parserResult;
    }


    private void shuffle() {
    }

    //below are 4 getters.
    public List<Adventurer> getAllAdventurers() {
        return this.adventurers;
    }

    public List<Monster> getAllMonsters() {
        return this.monsters;
    }

    public List<Room> getAllRooms() {
        return this.rooms;
    }

    public List<Trap> getAllTraps() {
        return this.traps;
    }
    //maybe setter to give data to gamedata class?

    public List<Monster> drawMonsters() {
        int amount = 3;
        int tailIndex = monsters.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        // List<Monster> drawnMonsters = monsters.subList(tailIndex - amount, tailIndex);
        List<Monster> drawnMonsters = monsters.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        monsters.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnMonsters;
    }

    public List<Room> drawRooms() {
        int amount = 2;
        int tailIndex = rooms.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        List<Room> drawnRooms = rooms.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        rooms.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnRooms;
    }

    public List<Trap> drawTraps(int amountPlaceTrapBids) {
        int tailIndex = traps.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        List<Trap> drawnTraps = traps.subList(tailIndex - amountPlaceTrapBids, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        traps.subList(tailIndex - amountPlaceTrapBids, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnTraps;
    }

    public List<Adventurer> drawAdventurers(int amount) {
        int tailIndex = adventurers.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        List<Adventurer> drawnAdventurers = adventurers.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        adventurers.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnAdventurers;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }
    // i think we still need getter to pass the data to GameData class.


}

