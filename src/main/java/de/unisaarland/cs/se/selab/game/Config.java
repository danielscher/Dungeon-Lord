package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import org.json.JSONException;
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
        log.fine("error in general information: -5,-6 " + "/n");
        log.fine("maxYear =" + maxYear + "/n");
        log.fine("maxPlayer =" + maxPlayer + "/n");
        log.fine("number of rooms =" + rooms.size() + "/n");
        log.fine("number of traps =" + traps.size() + "/n");
        log.fine("number of adventurers =" + adventurers.size() + "/n");
        log.fine("number of monsters =" + monsters.size() + "/n");
        log.fine("invalid information in =" + parserResult + "/n");
        /*
        System.out.println("Help and Log: " + "/n");
        System.out.println("error in Monsters: -1,10,11,12 " + "/n");
        System.out.println("error in Adventurers: -2,20,21,22 " + "/n");
        System.out.println("error in Traps: -3,30,31,32 " + "/n");
        System.out.println("error in Rooms: -4,40,41,42 " + "/n");
        System.out.println("error in general information: -5,-6 " + "/n");
        System.out.println("maxYear =" + maxYear + "/n");
        System.out.println("maxPlayer =" + maxPlayer + "/n");
        System.out.println("number of rooms =" + rooms.size() + "/n");
        System.out.println("number of traps =" + traps.size() + "/n");
        System.out.println("number of adventurers =" + adventurers.size() + "/n");
        System.out.println("number of monsters =" + monsters.size() + "/n");
        System.out.println("invalid information in =" + parserResult + "/n");
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
        parserAndCheckMonsterToList(obj);
        parserAndCheckAdventurerToList(obj);
        parserAndCheckTrapToList(obj);
        parserAndCheckRoomToList(obj);
        checkGeneralInfoValid();
        checkUniqueId();
        displayLog();
    }

    public void parseFromFile(final String configFilePath) throws FileNotFoundException {
        parserResult = 0;
        final StringBuilder builder = new StringBuilder();
        //    try (BufferedReader br = new BufferedReader(new FileReader(configFilePath))) {

        try (BufferedReader br = Files.newBufferedReader(Paths.get(configFilePath),
                StandardCharsets.UTF_8)) {
            String s;
            while ((s = br.readLine()) != null) {
                builder.append(s);
            }

        } catch (IOException e) {
            // e.printStackTrace();
            log.fine(e.toString());

        }
        final String allJSONdata = builder.toString();
        //  System.out.println(allJSONdata);
        obj = new JSONObject(allJSONdata);

        maxYear = obj.getInt("years");
        maxPlayer = obj.getInt("maxPlayers");
        dungeonSidelength = obj.getInt("dungeonSideLength");
        initFood = obj.getInt("initialFood");
        initGold = obj.getInt("initialGold");
        initImps = obj.getInt("initialImps");
        // end of parse to json object
        // return parserResult;
    }

    public void parserAndCheckMonsterToList(final JSONObject obj) {
        final JSONArray monsterArray = obj.getJSONArray("monsters");
        final int monArrLen = monsterArray.length();
        monsters = new ArrayList<>();
        for (int i = 0; i < monArrLen; i++) {
            final JSONObject monsterObj = monsterArray.getJSONObject(i);
            final int id = (Integer) monsterObj.get("id");
            //  final String name = (String) monsterObj.get("name");

            int hunger;
            try {
                //  String hungerValString = monsterObj.getString("hunger");
                //  hunger = Integer.valueOf(hungerValString); //(Integer) monsterObj.get("hunger");
                hunger = monsterObj.getInt("hunger");
            } catch (JSONException e) {
                hunger = 0;
                //    System.out.println("this monster does not have hunger"); do nothing
            }

            int evilness;
            try {
                //    String evilnessValString = monsterObj.getString("evilness");
                //    evilness = Integer.valueOf(evilnessValString);
                evilness = monsterObj.getInt("evilness");
            } catch (JSONException e) {
                evilness = 0;
                //    System.out.println("this monster does not have evilness"); do nothing
            }

            //  int evilness = (Integer) monsterObj.get("evilness");
            final int damage = monsterObj.getInt("damage");
            //   Attack attackStrategy = (Attack) monsterObj.get("attackStrategy");
            final String attackStrategyString = (String) monsterObj.get("attackStrategy");
            // Attack attackStrategy = Attack.valueOf(attackStrategyString);

            final Attack attackStrategy = convertStringToAttack(attackStrategyString);
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
            final Monster currMonster = new Monster(id, hunger, evilness, damage, attackStrategy);
            monsters.add(currMonster);
        } //end of monster for loop+ add to list
        // return parserResult;
    }

    public void parserAndCheckAdventurerToList(final JSONObject obj) {
        final JSONArray adventurerArray = obj.getJSONArray("adventurers");
        final int advArrLen = adventurerArray.length();
        adventurers = new ArrayList<>();
        for (int i = 0; i < advArrLen; i++) {
            final JSONObject adventurerObj = adventurerArray.getJSONObject(i);
            final int id = adventurerObj.getInt("id");
            //  String name = (String) adventurerObj.get("name");
            //  int healValue = (Integer) adventurerObj.get("healValue");
            int healValue;
            try {
                healValue = adventurerObj.getInt("healValue");
            } catch (JSONException e) {
                healValue = 0;
                //    System.out.println("not a priest");
            }

            //  int defuseValue = (Integer) adventurerObj.get("defuseValue");
            int defuseValue;
            try {
                defuseValue = adventurerObj.getInt("defuseValue");
            } catch (JSONException e) {
                //    System.out.println("not a thief");
                defuseValue = 0;
            }

            //   boolean charge = (Boolean) adventurerObj.get("charge");
            boolean charge;
            try {
                charge = adventurerObj.getBoolean("charge");
            } catch (JSONException e) {
                //    System.out.println("not in charge");
                charge = false;
            }

            final int difficulty = adventurerObj.getInt("difficulty");
            final int healthPoints = adventurerObj.getInt("healthPoints");

            if (difficulty < 0 || difficulty > 8 || healthPoints < 1 || healValue < 0
                    || defuseValue < 0) {
                parserResult = 21;
                break;
            }
            // invalid value of an adventurer

            if (healValue > 0 && defuseValue > 0) {
                parserResult = 22;
                break;
            }
            // cannot be priest and thief at the same time
            final Adventurer currAdventurer = new Adventurer(id, difficulty, healthPoints,
                    healValue, defuseValue, charge);
            adventurers.add(currAdventurer);
        } //end of the adventurer forloop +++ add to list
        // return parserResult;
    }

    public void parserAndCheckTrapToList(final JSONObject obj) {
        final JSONArray trapArray = obj.getJSONArray("traps");
        final int trpArrLen = trapArray.length();
        traps = new ArrayList<>();
        for (int i = 0; i < trpArrLen; i++) {
            final JSONObject trapObj = trapArray.getJSONObject(i);
            final int id = trapObj.getInt("id");
            //  String name = (String) trapObj.get("name");
            final int damage = trapObj.getInt("damage");
            //  Attack attackStrategy = (Attack) trapObj.get("attackStrategy");
            final String attackStrategyString = (String) trapObj.get("attackStrategy");
            final Attack attackStrategy = convertStringToAttack(attackStrategyString);

            //    int target = (Integer) trapObj.get("target");
            int target;
            try {
                //    String targetValString = trapObj.getString("target");
                //    target = Integer.valueOf(targetValString);
                target = trapObj.getInt("target");
                //    System.out.println("traget string and value?"+target);
            } catch (JSONException e) {
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
        // return parserResult;
    }

    public void parserAndCheckRoomToList(final JSONObject obj) {
        final JSONArray roomArray = obj.getJSONArray("rooms");
        final int roomArrLen = roomArray.length();
        rooms = new ArrayList<>();
        for (int i = 0; i < roomArrLen; i++) {
            final JSONObject roomObj = roomArray.getJSONObject(i);
            //    String name = (String) roomObj.get("name");
            //  int foodProduction = (Integer) roomObj.get("food");
            int foodProduction;
            try {
                foodProduction = roomObj.getInt("food");
            } catch (JSONException e) {
                //    System.out.println("not produce food");
                foodProduction = 0;
            }

            //    int goldProduction = (Integer) roomObj.get("gold");
            int goldProduction;
            try {

                goldProduction = roomObj.getInt("gold");
            } catch (JSONException e) {
                //    System.out.println("not produce gold");
                goldProduction = 0;
            }
            //    int impProduction = (Integer) roomObj.get("imps");
            int impProduction;
            try {
                impProduction = roomObj.getInt("imps");
            } catch (JSONException e) {
                //    System.out.println("not produce imps");
                impProduction = 0;
            }
            //    int niceness = (Integer) roomObj.get("niceness");
            int niceness;
            try {

                niceness = roomObj.getInt("niceness");
            } catch (JSONException e) {
                //    System.out.println("no niceness");
                niceness = 0;
            }
            final int id = (Integer) roomObj.get("id");

            if (id < 0) {
                parserResult = 41;
                break;
            }
            final int activationCost = (Integer) roomObj.get("activation");
            //    Location placementLoc = (Location) roomObj.get("restriction");
            final String locationString = (String) roomObj.get("restriction");
            final Location placementLoc = convertStringToLocation(locationString);
            final Room currRoom = new Room(id, activationCost, foodProduction, goldProduction,
                    impProduction, niceness, placementLoc);
            rooms.add(currRoom);
        } // end of rooms

        // return parserResult;
    }


    public void checkGeneralInfoValid() {
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
        if (initGold < 0 || initFood < 0 || initImps < 0 || configFilePath == null) {
            parserResult = -7;
            // init number invalid
        }

        // !!! try to check the duplicate elements, but get error...
        //   HashSet<Trap> checkTraps = new HashSet<>(Arrays.asList(traps));
        // return parserResult;
    }

    public void checkUniqueId() {
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
        // return parserResult;
    }


    public void shuffle() {
        // TODO
    }

    public int getParserResult(){
        return parserResult;
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
        final int amount = 3;
        final int tailIndex = monsters.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        // List<Monster> drawnMonsters = monsters.subList(tailIndex - amount, tailIndex);
        final List<Monster> drawnMonsters = monsters.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        monsters.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnMonsters;
    }

    public List<Room> drawRooms() {
        final int amount = 2;
        final int tailIndex = rooms.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        final List<Room> drawnRooms = rooms.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        rooms.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnRooms;
    }

    public List<Trap> drawTraps(final int amountPlaceTrapBids) {
        final int tailIndex = traps.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        final List<Trap> drawnTraps = traps.subList(tailIndex - amountPlaceTrapBids, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        traps.subList(tailIndex - amountPlaceTrapBids, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnTraps;
    }

    public List<Adventurer> drawAdventurers(final int amount) {
        final int tailIndex = adventurers.size();
        // Gets items in range [i,j) "[": including, ")":excluding
        final List<Adventurer> drawnAdventurers = adventurers.subList(tailIndex - amount, tailIndex)
                .stream().toList();  // workaround to get a copy of this list
        adventurers.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnAdventurers;
    }
    // get general information from Config files
    public int getMaxPlayers() {
        return maxPlayer;
    }
    // i think we still need getter to pass the data to GameData class.

    public int getMaxYear() {
        return maxYear;
    }

    public int getDungeonSidelength() {
        return dungeonSidelength;
    }

    public int getInitFood() {
        return initFood;
    }

    public int getInitImps() {
        return initImps;
    }

    public int getInitGold() {
        return initGold;
    }

}

