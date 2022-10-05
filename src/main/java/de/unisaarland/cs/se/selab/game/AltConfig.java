package de.unisaarland.cs.se.selab.game;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Attack;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;
import de.unisaarland.cs.se.selab.game.util.Location;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * GENERAL EXPLANATION: after creating an instance of this class, the method parse() is to be
 * called
 * <p>
 * parse() calls smaller parse methods (like parseMonsters(...)) which deal with parsing the
 * corresponding part of the config file after everything's parsed, the validity of the parsed data
 * is checked
 */

public class AltConfig {

    private final Path path;
    private int maxPlayers;
    private int years;
    private int dungeonSideLength = 15;
    private int initialFood = 3;
    private int initialGold = 3;
    private int initialImps = 3;
    private int initialEvilness = 5;

    private final List<Monster> monsters = new ArrayList<>();
    private final List<Adventurer> adventurers = new ArrayList<>();
    private final List<Trap> traps = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();

    private String configJSONString;

    private final Random random;

    public AltConfig(final Path path, final long seed) {
        this.path = path;
        this.random = new Random(seed);
    }

    /*
     ---------- parsing methods ----------
     */

    /**
     * this method parses the config and stores all the collected data in this object return ==
     * success?
     */
    public boolean parse() {

        // try to read file as string
        String fileContent;
        try {
            fileContent = Files.readString(path);
            configJSONString = fileContent;
        } catch (IOException e) {
            return false;
        }

        // try to create JSON object
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(fileContent);
        } catch (JSONException e) {
            return false;
        }

        // copied from the forum
        try {
            getSchema(AltConfig.class, "main.schema").validate(jsonObject);
        } catch (ValidationException e) {
            return false;
        }

        // try to parse every part of the config file
        try {
            parseGeneralInfo(jsonObject);
            parseMonsters(jsonObject);
            parseAdventurers(jsonObject);
            parseRooms(jsonObject);
            parseTraps(jsonObject);
        } catch (JSONException e) {
            // if anything fails, return false
            return false;
        }

        shuffleLists();

        // now check if the read values are valid
        return checkIfValid();
    }

    /**
     * this method parses general game variables
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void parseGeneralInfo(final JSONObject jsonObject) {
        maxPlayers = jsonObject.getInt("maxPlayers");
        years = jsonObject.getInt("years");
        dungeonSideLength = jsonObject.getInt("dungeonSideLength");

        // optional configuration values
        // try-catch prevents this method from throwing an exception on failure
        try {
            initialFood = jsonObject.getInt("initialFood");
        } catch (JSONException e) {
            // if not present, ignore and use default value
            initialFood = 3;
        }
        try {
            initialGold = jsonObject.getInt("initialGold");
        } catch (JSONException e) {
            // if not present, ignore and use default value
            initialGold = 3;
        }
        try {
            initialImps = jsonObject.getInt("initialImps");
        } catch (JSONException e) {
            // if not present, ignore and use default value
            initialImps = 3;
        }
        try {
            initialEvilness = jsonObject.getInt("initialEvilness");
        } catch (JSONException e) {
            // if not present, ignore and use default value
            initialEvilness = 5;
        }
    }

    /**
     * this method parses all monster objects
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void parseMonsters(final JSONObject jsonObject) {
        // get array of monsters
        final JSONArray monsterJSONArray = jsonObject.getJSONArray("monsters");

        // iterate over array and create a monster object for each array object
        for (int i = 0; i < monsterJSONArray.length(); i++) {
            createMonster(monsterJSONArray.getJSONObject(i));
        }
    }

    /**
     * this method is used to create a single instance of a monster with the parameters provided by
     * one array entry
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void createMonster(final JSONObject monsterJSONObj) {
        // optional values...
        // hunger is optional, therefor try-catch
        int hunger;
        try {
            hunger = monsterJSONObj.getInt("hunger");
        } catch (JSONException e) {
            // if not provided, ignore
            hunger = 0;
        }

        // hunger is optional, therefor try-catch
        int evilness;
        try {
            evilness = monsterJSONObj.getInt("evilness");
        } catch (JSONException e) {
            // if not provided, ignore
            evilness = 0;
        }

        // non optional values...
        final int monsterId = monsterJSONObj.getInt("id");
        final int damage = monsterJSONObj.getInt("damage");
        final String attackStrategyString = monsterJSONObj.getString("attackStrategy");
        // TODO check if this actually works in a test
        final Attack attackStrategy = Attack.valueOf(attackStrategyString);

        final Monster monster = new Monster(monsterId, hunger, evilness, damage, attackStrategy);
        monsters.add(monster);  // add created monster to the list of all monsters
    }

    /**
     * this method parses all adventurer objects
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void parseAdventurers(final JSONObject jsonObject) {
        // get array of adventurers
        final JSONArray adventurerJSONArray = jsonObject.getJSONArray("adventurers");

        // iterate over array and create an adventurer object for each array object
        for (int i = 0; i < adventurerJSONArray.length(); i++) {
            createAdventurer(adventurerJSONArray.getJSONObject(i));
        }
    }

    /**
     * this method is used to create a single instance of an adventurer with the parameters provided
     * by one array entry
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void createAdventurer(final JSONObject adventurerJSONObj) {
        // optional values...
        // healValue is optional, if parser doesn't find value, take default value
        int healValue;
        try {
            healValue = adventurerJSONObj.getInt("healValue");
        } catch (JSONException e) {
            // ignore error and take default value
            healValue = 0;
        }

        // defuseValue is optional, if parser doesn't find value, take default value
        int defuseValue;
        try {
            defuseValue = adventurerJSONObj.getInt("defuseValue");
        } catch (JSONException e) {
            // ignore error and take default value
            defuseValue = 0;
        }

        // charge is optional, if parser doesn't find value, take default value
        boolean charge;
        try {
            charge = adventurerJSONObj.getBoolean("charge");
        } catch (JSONException e) {
            // ignore error and take default value
            charge = false;
        }

        // non optional values...
        final int adventurerId = adventurerJSONObj.getInt("id");
        final int difficulty = adventurerJSONObj.getInt("difficulty");
        final int healthPoints = adventurerJSONObj.getInt("healthPoints");

        final Adventurer adventurer = new Adventurer(adventurerId, difficulty, healthPoints,
                healValue,
                defuseValue, charge);
        adventurers.add(adventurer);  // add created adventurer to the list of all adventurers
    }

    /**
     * this method parses all trap objects
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void parseTraps(final JSONObject jsonObject) {
        // get array of traps
        final JSONArray trapJSONArray = jsonObject.getJSONArray("traps");

        // iterate over array and create a trap object for each array object
        for (int i = 0; i < trapJSONArray.length(); i++) {
            createTrap(trapJSONArray.getJSONObject(i));
        }
    }

    /**
     * this method is used to create a single instance of a trap with the parameters provided by one
     * array entry
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void createTrap(final JSONObject trapJSONObj) {
        final int trapId = trapJSONObj.getInt("id");
        final int damage = trapJSONObj.getInt("damage");
        final String attackStrategyString = trapJSONObj.getString("attackStrategy");
        final Attack attackStrategy = Attack.valueOf(attackStrategyString);
        int target = 0;

        // only try to get target if attack strategy is targeted
        if (attackStrategy == Attack.TARGETED) {
            target = trapJSONObj.getInt("target");
        }

        final Trap trap = new Trap(trapId, damage, target, attackStrategy);
        traps.add(trap);
    }

    /**
     * this method parses all room objects
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void parseRooms(final JSONObject jsonObject) {
        // get array of rooms
        final JSONArray roomJSONArray = jsonObject.getJSONArray("rooms");

        // iterate over array and create a room object for each array object
        for (int i = 0; i < roomJSONArray.length(); i++) {
            createRoom(roomJSONArray.getJSONObject(i));
        }
    }

    /**
     * this method is used to create a single instance of a room with the parameters provided by one
     * array entry
     *
     * @throws JSONException when the JSON parser fails to read the fields
     */
    private void createRoom(final JSONObject roomJSONObj) {

        // optional parameters...
        int food;
        try {
            food = roomJSONObj.getInt("food");
        } catch (JSONException e) {
            // ignore error and take default value
            food = 0;
        }

        int gold;
        try {
            gold = roomJSONObj.getInt("gold");
        } catch (JSONException e) {
            // ignore error and take default value
            gold = 0;
        }

        int imps;
        try {
            imps = roomJSONObj.getInt("imps");
        } catch (JSONException e) {
            // ignore error and take default value
            imps = 0;
        }

        int niceness;
        try {
            niceness = roomJSONObj.getInt("niceness");
        } catch (JSONException e) {
            // ignore error and take default value
            niceness = 0;
        }

        // non optional values...
        final int roomId = roomJSONObj.getInt("id");
        final int activation = roomJSONObj.getInt("activation");
        final String restrictionString = roomJSONObj.getString("restriction");
        final Location restriction = Location.valueOf(restrictionString);

        final Room room = new Room(roomId, activation, food, gold, imps, niceness, restriction);
        rooms.add(room);
    }

    /*
     ---------- config validation methods ----------
     */


    /**
     * this method is used to create a single instance of a room with the parameters provided by one
     * array entry
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

    /**
     * this method is used to validate the correctness of all provided values for the general game
     * data
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

    /**
     * this method is used to validate the correctness of all provided values for the monsters
     */
    private boolean validateMonsters() {
        boolean res = true;
        // check each monster for correctness of parameters
        for (final Monster monster : monsters) {
            res &= validateMonster(monster);
        }
        res &= allMonsterIdsUnique(); // check if all IDs unique
        return res;
    }

    /**
     * this method validates the parameters of one monster
     */
    private boolean validateMonster(final Monster monster) {
        boolean res = (monster.getMonsterID() >= 0);
        res &= (monster.getDamage() >= 1);
        res &= (monster.getAttack() != null); // TODO check if null can happen if string wrong
        res &= (monster.getHunger() >= 0);
        res &= (monster.getHunger() >= 0);
        res &= (monster.getEvilness() >= 0);
        return res;
    }

    /**
     * this method checks if all monster IDs are unique
     */
    private boolean allMonsterIdsUnique() {
        final Set<Integer> monsterIds = new HashSet<>(
                monsters.size()); // setting maxCapacity a priori increases performance
        for (final Monster monster : monsters) {
            monsterIds.add(monster.getMonsterID());
        }
        return (monsterIds.size() == monsters.size());
    }

    /**
     * this method is used to validate the correctness of all provided values for the adventurers
     */
    private boolean validateAdventurers() {
        boolean res = true;
        // check each adventurer for correctness of parameters
        for (final Adventurer adventurer : adventurers) {
            res &= validateAdventurer(adventurer);
        }
        res &= allAdventurerIdsUnique(); // check if all IDs unique
        return res;
    }

    /**
     * this method validates the parameters of one adventurer
     */
    private boolean validateAdventurer(final Adventurer adventurer) {
        boolean res = (adventurer.getAdventurerID() >= 0);
        res &= (adventurer.getDifficulty() >= 1 && adventurer.getDifficulty() <= 8);
        res &= (adventurer.getMaxHealthPoints() >= 0);
        res &= (adventurer.getHealValue() >= 0);
        res &= (adventurer.getDefuseValue() >= 0);
        return res;
    }

    /**
     * this method checks if all adventurer IDs are unique
     */
    private boolean allAdventurerIdsUnique() {
        final Set<Integer> adventurerIds = new HashSet<>(
                adventurers.size()); // setting maxCapacity a priori increases performance
        for (final Adventurer adventurer : adventurers) {
            adventurerIds.add(adventurer.getAdventurerID());
        }
        return (adventurerIds.size() == adventurers.size());
    }

    /**
     * this method is used to validate the correctness of all provided values for the traps
     */
    private boolean validateTraps() {
        boolean res = true;
        // check each trap for correctness of parameters
        for (final Trap trap : traps) {
            res &= validateTrap(trap);
        }
        res &= allTrapIdsUnique(); // check if all IDs unique
        return res;
    }

    /**
     * this method validates the parameters of one trap
     */
    private boolean validateTrap(final Trap trap) {
        boolean res = (trap.getTrapID() >= 0);
        res &= (trap.getDamage() >= 1);
        res &= (trap.getAttack() != null); // TODO test if null is even possible
        if (trap.getAttack() == Attack.TARGETED) {
            res &= (trap.getTarget() >= 1 && trap.getTarget() <= 3);
        }
        return res;
    }

    /**
     * this method checks if all trap IDs are unique
     */
    private boolean allTrapIdsUnique() {
        final Set<Integer> trapIds = new HashSet<>(
                traps.size()); // setting maxCapacity a priori increases performance
        for (final Trap trap : traps) {
            trapIds.add(trap.getTrapID());
        }
        return (traps.size() == trapIds.size());
    }

    /**
     * this method is used to validate the correctness of all provided values for the rooms
     */
    private boolean validateRooms() {
        boolean res = true;
        // check each trap for correctness of parameters
        for (final Room room : rooms) {
            res &= validateRoom(room);
        }
        res &= allRoomIdsUnique(); // check if all IDs unique
        return res;
    }

    /**
     * this method validates the parameters of one room
     */
    private boolean validateRoom(final Room room) {
        boolean res = (room.getRoomID() >= 0);
        res &= (room.getActivationCost() >= 1);
        res &= (room.getPlacementLoc() != null);
        res &= (room.getFoodProduction() >= 0);
        res &= (room.getGoldProduction() >= 0);
        res &= (room.getImpProduction() >= 0);
        res &= (room.getNiceness() >= 0);
        return res;
    }

    /**
     * this method checks if all room IDs are unique
     */
    private boolean allRoomIdsUnique() {
        final Set<Integer> roomIds = new HashSet<>(
                rooms.size()); // setting maxCapacity a priori increases performance
        for (final Room room : rooms) {
            roomIds.add(room.getRoomID());
        }
        return (rooms.size() == roomIds.size());
    }

    /**
     * ----------  getters / draw methods ----------
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

    /**
     * standard amount of 3
     */
    public List<Monster> drawMonsters() {
        return drawMonsters(3);
    }

    public List<Monster> drawMonsters(final int amount) {
        final int tailIndex = monsters.size();
        // get a list of Monsters
        final List<Monster> drawnMonsters =
                new ArrayList<>(monsters.subList(tailIndex - amount, tailIndex));

        monsters.subList(tailIndex - amount, tailIndex).clear(); // removes drawn monsters
        return drawnMonsters;
    }

    /**
     * standard amount of 2
     */
    public List<Room> drawRooms() {
        return drawRooms(2);
    }


    public List<Room> drawRooms(final int amount) {
        final int tailIndex = rooms.size();
        // get a list of Rooms
        final List<Room> drawnRooms =
                new ArrayList<>(rooms.subList(tailIndex - amount, tailIndex));

        rooms.subList(tailIndex - amount, tailIndex).clear(); // removes this sublist from list.
        return drawnRooms;
    }

    public List<Trap> drawTraps(final int amount) {
        final int tailIndex = traps.size();
        // get a list of Traps
        final List<Trap> drawnTraps =
                new ArrayList<>(traps.subList(tailIndex - amount, tailIndex));

        traps.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnTraps;
    }

    public List<Adventurer> drawAdventurers(final int amount) {
        final int tailIndex = adventurers.size();
        // get a list of Traps
        final List<Adventurer> drawnAdventurers =
                new ArrayList<>(adventurers.subList(tailIndex - amount, tailIndex));

        adventurers.subList(tailIndex - amount, tailIndex)
                .clear(); // removes this sublist from list.
        return drawnAdventurers;
    }

    private void shuffleLists() {
        Collections.shuffle(monsters, random);
        Collections.shuffle(adventurers, random);
        Collections.shuffle(traps, random);
        Collections.shuffle(rooms, random);
    }

    /**
     * method provided in the forum
     */
    static Schema getSchema(final Class<?> subclass, final String name) {
        return SchemaLoader.builder()
                .schemaClient(SchemaClient.classPathAwareClient()).schemaJson(new JSONObject(
                        Objects.requireNonNull(
                                loadResource(subclass, "schema/" + name))))
                .resolutionScope("classpath://schema/").build().load().build();
    }

    /**
     * method provided in the forum
     */
    public static String loadResource(final Class<?> subclass, final String name) {
        LoggerFactory.getLogger(subclass)
                .trace("loading {}", subclass.getClassLoader().getResource(name));
        try (final var input = new InputStreamReader(
                Objects.requireNonNull(subclass.getClassLoader().getResourceAsStream(name)),
                StandardCharsets.UTF_8)) {
            try (final BufferedReader reader = new BufferedReader(input)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (final IOException e) {
            LoggerFactory.getLogger(subclass).error("{}", e.getMessage(), e);
            return null;
        }
    }
}
