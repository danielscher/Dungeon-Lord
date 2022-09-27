package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;


public class ActionFactoryImplementation implements ActionFactory<Action> {


    public Action createActivateRoom(int commID, int roomID) {
        return new ActivateRoomAction(commID, roomID);
    }

    public Action createBattleGround(int commID, int x, int y) {
        return new BattleGroundAction(commID, x, y);
    }

    @Override
    public Action createBuildRoom(int commID, int x, int y, int roomID) {
        return new BuildRoomAction(commID, roomID, x, y);
    }

    public Action createEndTurn(int commID) {
        return new EndTurnAction(commID);
    }

    public Action createLeave(int commID) {
        return new LeaveAction(commID);
    }

    public Action createStartGame(int commID) {
        return new StartGameAction(commID);
    }

    public Action createHireMonster(int commID, int monsterID) {
        return new HireMonsterAction(commID, monsterID);
    }

    public Action createMonster(int commID, int monsterID) {
        return new MonsterAction(commID, monsterID);
    }

    public Action createMonsterTargeted(int commID, int monsterID, int position) {
        return new MonsterTargetedAction(commID, monsterID, position);
    }

    public Action createRegister(int commID, String name) {
        return new RegAction(commID, name);
    }

    public Action createPlaceBid(int commID, BidType bid, int slot) {
        return new PlaceBidAction(commID, bid, slot);
    }

    public Action createDigTunnel(int commID, int x, int y) {
        return new DigTunnelAction(commID, x, y);
    }

    public Action createTrap(int commID, int trapID) {
        return new TrapAction(commID, trapID);
    }
}
