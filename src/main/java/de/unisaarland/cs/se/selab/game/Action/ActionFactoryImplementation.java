package de.unisaarland.cs.se.selab.game.Action;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;


public abstract class ActionFactoryImplementation implements ActionFactory {

    public abstract Action createActivateRoom(int commID, int roomID);

    public abstract Action createBattleGround(int commID, int x, int y);

    public abstract Action createEndTurn(int commID);

    public abstract Action createLeave(int commID);

    public abstract Action createStartGame(int commID);

    public abstract Action createHireMonster(int commID, int monsterID);

    public abstract Action createMonster(int commID, int monsterID);

    public abstract Action createMonsterTargeted(int commID, int monsterID, int position);

    public abstract Action createRegister(int commID, String name);

    public abstract Action createPlaceBid(int commID, BidType bid, int slot);

    public abstract Action createDigTunnel(int commID, int x, int y);

    public abstract Action createTrap(int commID, int trapID);
}
