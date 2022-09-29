package de.unisaarland.cs.se.selab.game.action;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;


public class ActionFactoryImplementation implements ActionFactory<Action> {

    @Override
    public Action createActivateRoom(final int commID, final int roomID) {
        return new ActivateRoomAction(commID, roomID);
    }

    @Override
    public Action createBattleGround(final int commID, final int x, final int y) {
        return new BattleGroundAction(commID, x, y);
    }

    @Override
    public Action createBuildRoom(final int commID, final int x, final int y, final int roomID) {
        return new BuildRoomAction(commID, roomID, x, y);
    }

    @Override
    public Action createEndTurn(final int commID) {
        return new EndTurnAction(commID);
    }

    @Override
    public Action createLeave(final int commID) {
        return new LeaveAction(commID);
    }

    @Override
    public Action createStartGame(final int commID) {
        return new StartGameAction(commID);
    }

    @Override
    public Action createHireMonster(final int commID, final int monsterID) {
        return new HireMonsterAction(commID, monsterID);
    }

    @Override
    public Action createMonster(final int commID, final int monsterID) {
        return new MonsterAction(commID, monsterID);
    }

    @Override
    public Action createMonsterTargeted(final int commID, final int monsterID, final int position) {
        return new MonsterTargetedAction(commID, monsterID, position);
    }

    @Override
    public Action createRegister(final int commID, final String name) {
        return new RegAction(commID, name);
    }

    @Override
    public Action createPlaceBid(final int commID, final BidType bid, final int slot) {
        return new PlaceBidAction(commID, bid, slot);
    }

    @Override
    public Action createDigTunnel(final int commID, final int x, final int y) {
        return new DigTunnelAction(commID, x, y);
    }

    @Override
    public Action createTrap(final int commID, final int trapID) {
        return new TrapAction(commID, trapID);
    }
}
