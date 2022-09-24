package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.entities.Adventurer;
import de.unisaarland.cs.se.selab.game.entities.Monster;
import de.unisaarland.cs.se.selab.game.entities.Room;
import de.unisaarland.cs.se.selab.game.entities.Trap;

import java.util.*;

public class Dungeon {
    private Tile[][] grid = new Tile[15][15];
    private List<Monster> hiredMonsters = new ArrayList<Monster>();
    private List<Trap> traps = new ArrayList<Trap>();
    private Deque<Adventurer> adventurerQueue = new ArrayDeque<Adventurer>();
    private Queue<Adventurer> prison = new ArrayDeque<Adventurer>();
    private int[] currAdvPos = new int[2];
    private int[] currBattleGround = new int[2];
    private List<Room> rooms = new ArrayList<Room>();
    private int restingImps, tunnelDiggingImps, goldMiningImps, producingImps;
}

