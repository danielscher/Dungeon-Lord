package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.util.ArrayList;
import java.util.List;
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
    private boolean parserResult;
    private boolean configResult;
    private ArrayList<Monster> monsters;
    private ArrayList<Adventurer> adventurers;
    private ArrayList<Trap> traps;
    private ArrayList<Room> rooms;


    private boolean parse() {
        parserResult = true;
        JSONObject obj = new JSONObject(".\\resources\\configuration.json");
        maxYear = (Integer) obj.get("years");
        maxPlayer = (Integer) obj.get("maxPlayers");
        dungeonSidelength = (Integer) obj.get("dungeonSidelength");
        initFood = (Integer) obj.get("initialFood");
        initGold = (Integer) obj.get("initialGold");
        initImps = (Integer) obj.get("initialImps");

        JSONArray monsterArray = obj.getJSONArray("monsters");
        int monArrLen = monsterArray.length();
        for (int i = 0; i < monArrLen; i++) {
            JSONObject monsterObj = monsterArray.getJSONObject(i);
            int id = (Integer) monsterObj.get("id");
            String name = (String) monsterObj.get("name");
            int hunger = (Integer) monsterObj.get("hunger");
            int evilness = (Integer) monsterObj.get("evilness");
            int damage = (Integer) monsterObj.get("damage");
            Attack attackStrategy = (Attack) monsterObj.get("attackStrategy");
            if (evilness < 0 || hunger < 0) {
                parserResult = false;
                break;
            }
            // invalid evil/hunger

            if (damage < 1 || attackStrategy == null) {
                parserResult = false;
                break;
            }
            // valid damage, attack not set
        }

        JSONArray adventurerArray = obj.getJSONArray("adventurers");
        int advArrLen = adventurerArray.length();
        for (int i = 0; i < advArrLen; i++) {
            JSONObject adventurerObj = adventurerArray.getJSONObject(i);
            int id = (Integer) adventurerObj.get("id");
            String name = (String) adventurerObj.get("name");
            int difficulty = (Integer) adventurerObj.get("difficulty");
            int healthPoints = (Integer) adventurerObj.get("healthPoints");
            int healValue = (Integer) adventurerObj.get("healValue");
            int defuseValue = (Integer) adventurerObj.get("defuseValue");
            boolean charge = (boolean) adventurerObj.get("charge");
            if (difficulty < 0 || healthPoints < 1 || healValue < 0 || defuseValue < 0) {
                parserResult = false;
                break;
            }
            // invalid value of an adventurer

            if (healValue > 0 && defuseValue > 0) {
                parserResult = false;
                break;
            }
            // cannot be priest and thief at the same time
        }

        JSONArray trapArray = obj.getJSONArray("traps");
        int trpArrLen = trapArray.length();
        for (int i = 0; i < trpArrLen; i++) {
            JSONObject trapObj = trapArray.getJSONObject(i);
            int id = (Integer) trapObj.get("id");
            String name = (String) trapObj.get("name");
            int damage = (Integer) trapObj.get("damage");
            Attack attackStrategy = (Attack) trapObj.get("attackStrategy");
            int target = (Integer) trapObj.get("target");
            if (id < 0 || damage < 1) {
                parserResult = false;
                break;
            }
            //traps should have valid damage and id
            if (attackStrategy == Attack.TARGETED && (target < 1 || target > 3)) {
                parserResult = false;
                break;
            }
            //target traps should have 1,2,3 as goals.
        }

        JSONArray roomArray = obj.getJSONArray("rooms");
        int roomArrLen = roomArray.length();
        for (int i = 0; i < roomArrLen; i++) {
            JSONObject roomObj = roomArray.getJSONObject(i);
            int id = (Integer) roomObj.get("id");
            String name = (String) roomObj.get("name");
            int activationCost = (Integer) roomObj.get("activation");
            Location placementLoc = (Location) roomObj.get("restriction");
            int foodProduction = (Integer) roomObj.get("food");
            int goldProduction = (Integer) roomObj.get("gold");
            int impProduction = (Integer) roomObj.get("imps");
            int niceness = (Integer) roomObj.get("niceness");
            if (id < 0) {
                parserResult = false;
                break;
            }
        }

        return parserResult;
    }


    private boolean checkIfValid() {
        configResult = true;
        if (maxYear < 1 || maxPlayer < 1) {
            configResult = false;
            //not enough years/player,
        }
        if (traps.size() < 4 * 4 * maxYear) {
            configResult = false;
            //not enough traps
        }

        if (rooms.size() < 2 * 4 * maxYear) {
            configResult = false;
            //not enough rooms
        }

        if (monsters.size() < 3 * 4 * maxYear) {
            configResult = false;
            //not enough monsters
        }
        if (adventurers.size() < 3 * 3 * maxPlayer) {
            configResult = false;
            //not enough rooms
        }

        if (dungeonSidelength < 1 || dungeonSidelength > 15) {
            configResult = false;
            // dungeon too small or big
        }
        // !!! try to check the duplicate elements, but get error...
        //   HashSet<Trap> checkTraps = new HashSet<>(Arrays.asList(traps));

        return configResult;
    }

    private Monster createMonster(/*...need parameter*/) {
        // return NULL;
        return null;
    }

    private Adventurer createAdventurer(/*...need parameter*/) {
        return null;
    }

    private void shuffle() {
    }

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
