package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
GENERAL EXPLANATION:
after creating an instance of this class, the method parse() is to be called

parse() calls smaller parse methods (like parseMonsters(...)) which deal with parsing
the corresponding part of the config file
after everything's parsed, the validity of the parsed data is checked
 */

public class AltConfig {

    private final String path;
    private int maxPlayers;
    private int years;
    private int dungeonSideLength;
    private int initialFood = 3;
    private int initialGold = 3;
    private int initialImps = 3;
    private int initialEvilness = 5;

    private List<Monster> monsters = new ArrayList<Monster>();
    private List<Adventurer> adventurers = new ArrayList<Adventurer>();
    private List<Trap> traps = new ArrayList<Trap>();
    private List<Room> rooms = new ArrayList<Room>();

    private String configJSONString;

    public AltConfig(String path) {
        this.path = path;
    }


    /*
    ---------- parsing methods ----------
     */

    /*
    this method parses the config and stores all the collected data in this object
    return == success?
     */
    public boolean parse() {
        // try to find file path
        Path filePath = null;
        try {
            filePath = Paths.get(path);
        } catch (Exception e) {
            return false;
        }

        // try to read file as string
        String fileContent = "";
        try {
            fileContent = Files.readString(filePath);
            configJSONString = fileContent;
        } catch (IOException e) {
            return false;
        }

        // try to create JSON object
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(fileContent);
        } catch (Exception e) {
            return false;
        }

        // try to parse every part of the config file
        try {
            parseGeneralInfo(jsonObject);
            parseMonsters(jsonObject);
            parseAdventurers(jsonObject);
            parseRooms(jsonObject);
            parseTraps(jsonObject);
        } catch (Exception e) {
            // if anything fails, return false
            return false;
        }

        // now check if the read values are valid
        return checkIfValid();
    }

    /*
    this method parses general game variables
     */
    private void parseGeneralInfo(JSONObject jsonObject) throws JSONException {
        maxPlayers = jsonObject.getInt("maxPlayers");
        years = jsonObject.getInt("years");
        dungeonSideLength = jsonObject.getInt("dungeonSideLength");

        // optional configuration values
        // try-catch prevents this method from throwing an exception on failure
        try {
            initialFood = jsonObject.getInt("initialFood");
        } catch (Exception e) {
            // if not present, ignore and use default value
        }
        try {
            initialGold = jsonObject.getInt("initialGold");
        } catch (Exception e) {
            // if not present, ignore and use default value
        }
        try {
            initialImps = jsonObject.getInt("initialImps");
        } catch (Exception e) {
            // if not present, ignore and use default value
        }
        try {
            initialEvilness = jsonObject.getInt("initialEvilness");
        } catch (Exception e) {
            // if not present, ignore and use default value
        }
    }

    /*
    this method parses all monster objects
     */
    private void parseMonsters(JSONObject jsonObject) throws JSONException {
        // get array of monsters
        JSONArray monsterJSONArray = jsonObject.getJSONArray("monsters");

        // iterate over array and create a monster object for each array object
        for (int i = 0; i < monsterJSONArray.length(); i++) {
            createMonster(monsterJSONArray.getJSONObject(i));
        }
    }

    /*
    this method is used to create a single instance of a monster with the parameters provided by
    one array entry
     */
    private void createMonster(JSONObject monsterJSONObj) throws JSONException {
        // optional values...
        // hunger is optional, therefor try-catch
        int hunger = 0;
        try {
            hunger = monsterJSONObj.getInt("hunger");
        } catch (Exception e) {
            // if not provided, ignore
        }

        // hunger is optional, therefor try-catch
        int evilness = 0;
        try {
            evilness = monsterJSONObj.getInt("evilness");
        } catch (Exception e) {
            // if not provided, ignore
        }

        // non optional values...
        int monsterId = monsterJSONObj.getInt("id");
        int damage = monsterJSONObj.getInt("damage");
        String attackStrategyString = monsterJSONObj.getString("attackStrategy");
        // TODO check if this actually works in a test
        Attack attackStrategy = Attack.valueOf(attackStrategyString);

        Monster monster = new Monster(monsterId, hunger, evilness, damage, attackStrategy);
        monsters.add(monster);  // add created monster to the list of all monsters
    }

    /*
    this method parses all adventurer objects
     */
    private void parseAdventurers(JSONObject jsonObject) throws JSONException {
        // get array of adventurers
        JSONArray adventurerJSONArray = jsonObject.getJSONArray("adventurers");

        // iterate over array and create an adventurer object for each array object
        for (int i = 0; i < adventurerJSONArray.length(); i++) {
            createAdventurer(adventurerJSONArray.getJSONObject(i));
        }
    }

    /*
    this method is used to create a single instance of am adventurer with the parameters provided by
    one array entry
     */
    private void createAdventurer(JSONObject adventurerJSONObj) throws JSONException {
        // optional values...
        // healValue is optional, if parser doesn't find value, take default value
        int healValue = 0;
        try {
            healValue = adventurerJSONObj.getInt("healValue");
        } catch (Exception e) {
            // ignore error and take default value
        }

        // defuseValue is optional, if parser doesn't find value, take default value
        int defuseValue = 0;
        try {
            defuseValue = adventurerJSONObj.getInt("defuseValue");
        } catch (Exception e) {
            // ignore error and take default value
        }

        // charge is optional, if parser doesn't find value, take default value
        boolean charge = false;
        try {
            charge = adventurerJSONObj.getBoolean("charge");
        } catch (Exception e) {
            // ignore error and take default value
        }

        // non optional values...
        int adventurerId = adventurerJSONObj.getInt("id");
        int difficulty = adventurerJSONObj.getInt("difficulty");
        int healthPoints = adventurerJSONObj.getInt("healthPoints");

        Adventurer adventurer = new Adventurer(adventurerId, difficulty, healthPoints, healValue,
                defuseValue, charge);
        adventurers.add(adventurer);  // add created adventurer to the list of all adventurers
    }

    /*
    this method parses all trap objects
     */
    private void parseTraps(JSONObject jsonObject) throws JSONException {
        // get array of traps
        JSONArray trapJSONArray = jsonObject.getJSONArray("traps");

        // iterate over array and create a trap object for each array object
        for (int i = 0; i < trapJSONArray.length(); i++) {
            createTrap(trapJSONArray.getJSONObject(i));
        }
    }

    /*
    this method is used to create a single instance of a trap with the parameters provided by
    one array entry
     */
    private void createTrap(JSONObject trapJSONObj) throws JSONException {
        int trapId = trapJSONObj.getInt("id");
        int damage = trapJSONObj.getInt("damage");
        String attackStrategyString = trapJSONObj.getString("attackStrategy");
        Attack attackStrategy = Attack.valueOf(attackStrategyString);
        int target = 0;

        // only try to get target if attack strategy is targeted
        if (attackStrategy == Attack.TARGETED) {
            target = trapJSONObj.getInt("target");
        }

        Trap trap = new Trap(trapId, damage, target, attackStrategy);
        traps.add(trap);
    }

    /*
    this method parses all room objects
     */
    private void parseRooms(JSONObject jsonObject) throws JSONException {
        // get array of rooms
        JSONArray roomJSONArray = jsonObject.getJSONArray("rooms");

        // iterate over array and create a room object for each array object
        for (int i = 0; i < roomJSONArray.length(); i++) {
            createRoom(roomJSONArray.getJSONObject(i));
        }
    }

    /*
    this method is used to create a single instance of a room with the parameters provided by
    one array entry
     */
    private void createRoom(JSONObject roomJSONObj) throws JSONException {

        // optional parameters...
        int food = 0;
        try {
            food = roomJSONObj.getInt("food");
        } catch (Exception e) {
            // ignore error and take default value
        }

        int gold = 0;
        try {
            gold = roomJSONObj.getInt("gold");
        } catch (Exception e) {
            // ignore error and take default value
        }

        int imps = 0;
        try {
            imps = roomJSONObj.getInt("imps");
        } catch (Exception e) {
            // ignore error and take default value
        }

        int niceness = 0;
        try {
            niceness = roomJSONObj.getInt("niceness");
        } catch (Exception e) {
            // ignore error and take default value
        }

        // non optional values...
        int roomId = roomJSONObj.getInt("id");
        int activation = roomJSONObj.getInt("activation");
        String restrictionString = roomJSONObj.getString("restriction");
        Location restriction = Location.valueOf(restrictionString);

        Room room = new Room(roomId, activation, food, gold, imps, niceness, restriction);
        rooms.add(room);
    }

    /*
    ---------- config validation methods ----------
     */


    /*
    this method is used to create a single instance of a room with the parameters provided by
    one array entry
     */
    private boolean checkIfValid() {
        // if any of the validations fail, return false
        if (!validateGeneral()) {
            return false;
        }
        if (!validateMonsters()) {
            return false;
        }
        if (!validateAdventurers()) {
            return false;
        }
        if (!validateTraps()) {
            return false;
        }
        return validateRooms(); // this is a simplification suggested by intellij
        // if none of the checks above failed, the config is correct
    }

    /*
    this method is used to validate the correctness of all provided values for the
    general game data
     */
    private boolean validateGeneral() {
        // NOTE: "x &= a" is the same as "x = x & a" which (because we have boolean operands)
        // is the same as "x = x && a"
        // because of the conjunction, if any of the conditions is false, res will be false
        boolean res = (maxPlayers >= 1);
        res &= (years >= 1);
        res &= (dungeonSideLength >= 1 && dungeonSideLength <= 15);
        res &= (initialFood >= 0);
        res &= (initialGold >= 0);
        res &= (initialImps >= 0);
        res &= (initialEvilness >= 0 && initialEvilness <= 15);
        res &= (monsters.size() >= (3 * 4 * years));
        res &= (adventurers.size() >= (3 * maxPlayers * years));
        res &= (traps.size() >= (4 * 4 * years));
        res &= (rooms.size() >= (2 * 4 * years));
        return res;
    }

    /*
    this method is used to validate the correctness of all provided values for the
    monsters
     */
    private boolean validateMonsters() {
        boolean res = true;
        // check each monster for correctness of parameters
        for (Monster monster : monsters) {
            res &= validateMonster(monster);
        }
        res &= allMonsterIdsUnique(); // check if all IDs unique
        return res;
    }

    /*
    this method validates the parameters of one monster
     */
    private boolean validateMonster(Monster monster) {
        boolean res = (monster.getMonsterID() >= 0);
        res &= (monster.getDamage() >= 1);
        res &= (monster.getAttack() != null); // TODO check if null can happen if string wrong
        res &= (monster.getHunger() >= 0);
        res &= (monster.getHunger() >= 0);
        res &= (monster.getEvilness() >= 0);
        return res;
    }

    /*
    this method checks if all monster IDs are unique
     */
    private boolean allMonsterIdsUnique() {
        Set<Integer> monsterIds = new HashSet<>(
                monsters.size()); // setting maxCapacity a priori increases performance
        for (Monster monster : monsters) {
            monsterIds.add(monster.getMonsterID());
        }
        return (monsterIds.size() == monsters.size());
    }

    /*
    this method is used to validate the correctness of all provided values for the
    adventurers
     */
    private boolean validateAdventurers() {
        boolean res = true;
        // check each adventurer for correctness of parameters
        for (Adventurer adventurer : adventurers) {
            res &= validateAdventurer(adventurer);
        }
        res &= allAdventurerIdsUnique(); // check if all IDs unique
        return res;
    }

    /*
    this method validates the parameters of one adventurer
     */
    private boolean validateAdventurer(Adventurer adventurer) {
        boolean res = (adventurer.getAdventurerID() >= 0);
        res &= (adventurer.getDifficulty() >= 1 && adventurer.getDifficulty() <= 8);
        res &= (adventurer.getMaxHealthPoints() >= 0);
        res &= (adventurer.getHealValue() >= 0);
        res &= (adventurer.getDefuseValue() >= 0);
        return res;
    }

    /*
    this method checks if all adventurer IDs are unique
     */
    private boolean allAdventurerIdsUnique() {
        Set<Integer> adventurerIds = new HashSet<>(
                adventurers.size()); // setting maxCapacity a priori increases performance
        for (Adventurer adventurer : adventurers) {
            adventurerIds.add(adventurer.getAdventurerID());
        }
        return (adventurerIds.size() == adventurers.size());
    }

    /*
    this method is used to validate the correctness of all provided values for the
    traps
     */
    private boolean validateTraps() {
        boolean res = true;
        // check each trap for correctness of parameters
        for (Trap trap : traps) {
            res &= validateTrap(trap);
        }
        res &= allTrapIdsUnique(); // check if all IDs unique
        return res;
    }

    /*
    this method validates the parameters of one trap
     */
    private boolean validateTrap(Trap trap) {
        boolean res = (trap.getTrapID() >= 0);
        res &= (trap.getDamage() >= 1);
        res &= (trap.getAttack() != null); // TODO test if null is even possible
        if (trap.getAttack() == Attack.TARGETED) {
            res &= (trap.getTarget() >= 1 && trap.getTarget() <= 3);
        }
        return res;
    }

    /*
    this method checks if all trap IDs are unique
     */
    private boolean allTrapIdsUnique() {
        Set<Integer> trapIds = new HashSet<>(
                traps.size()); // setting maxCapacity a priori increases performance
        for (Trap trap : traps) {
            trapIds.add(trap.getTrapID());
        }
        return (traps.size() == trapIds.size());
    }

    /*
    this method is used to validate the correctness of all provided values for the
    rooms
     */
    private boolean validateRooms() {
        boolean res = true;
        // check each trap for correctness of parameters
        for (Room room : rooms) {
            res &= validateRoom(room);
        }
        res &= allRoomIdsUnique(); // check if all IDs unique
        return res;
    }

    /*
    this method validates the parameters of one room
     */
    private boolean validateRoom(Room room) {
        boolean res = (room.getRoomID() >= 0);
        res &= (room.getActivationCost() >= 1);
        res &= (room.getPlacementLoc() != null);
        res &= (room.getFoodProduction() >= 0);
        res &= (room.getGoldProduction() >= 0);
        res &= (room.getImpProduction() >= 0);
        res &= (room.getNiceness() >= 0);
        return res;
    }

    /*
    this method checks if all room IDs are unique
     */
    private boolean allRoomIdsUnique() {
        Set<Integer> roomIds = new HashSet<>(
                rooms.size()); // setting maxCapacity a priori increases performance
        for (Room room : rooms) {
            roomIds.add(room.getRoomID());
        }
        return (rooms.size() == roomIds.size());
    }

    /*
    ----------  getters / draw methods ----------
     */

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getYears() {
        return years;
    }

    public int getDungeonSideLength() {
        return dungeonSideLength;
    }

    public int getInitialFood() {
        return initialFood;
    }

    public int getInitialGold() {
        return initialGold;
    }

    public int getInitialEvilness() {
        return initialEvilness;
    }

    public int getInitialImps() {
        return initialImps;
    }


    public String getConfigJSONString() {
        return configJSONString;
    }

    /*
    standard amount of 3
     */
    public List<Monster> drawMonsters() {
        return drawMonsters(3);
    }

    public List<Monster> drawMonsters(int amount) {
        int tailIndex = monsters.size();
        // get a list of Monsters
        List<Monster> drawnMonsters =
                new ArrayList<Monster>(monsters.subList(tailIndex - amount, tailIndex));

        monsters.subList(tailIndex - amount, tailIndex).clear(); // removes drawn monsters
        return drawnMonsters;
    }

    /*
    standard amount of 2
     */
    public List<Room> drawRooms() {
        return drawRooms(2);
    }


    public List<Room> drawRooms(int amount) {
        int tailIndex = rooms.size();
        // get a list of Rooms
        List<Room> drawnRooms =
                new ArrayList<Room>(rooms.subList(tailIndex - amount, tailIndex));

        rooms.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnRooms;
    }

    public List<Trap> drawTraps(int amount) {
        int tailIndex = traps.size();
        // get a list of Traps
        List<Trap> drawnTraps =
                new ArrayList<Trap>(traps.subList(tailIndex - amount, tailIndex));

        traps.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnTraps;
    }

    public List<Adventurer> drawAdventurers(int amount) {
        int tailIndex = adventurers.size();
        // get a list of Traps
        List<Adventurer> drawnAdventurers =
                new ArrayList<Adventurer>(adventurers.subList(tailIndex - amount, tailIndex));

        adventurers.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnAdventurers;
    }
}
