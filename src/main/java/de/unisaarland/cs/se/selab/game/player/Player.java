package de.unisaarland.cs.se.selab.game.player;

import de.unisaarland.cs.se.selab.game.util.BidType;
import de.unisaarland.cs.se.selab.game.util.Title;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int playerID, commID;
    private Dungeon dungeon;
    private BidType[] currBids = new BidType[3];
    private BidType[] blockedBids = new BidType[3];
    private List<Title> titles = new ArrayList<Title>();
    private int points;
    private int evilLevel, gold, food;
}
