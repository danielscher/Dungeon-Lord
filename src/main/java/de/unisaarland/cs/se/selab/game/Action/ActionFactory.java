package de.unisaarland.cs.se.selab.game.Action;

import de.unisaarland.cs.se.selab.comm.ActionFactory;

public class ActionFactoryc implements ActionFactory {

    @Override
    public Action creatActivateRoomAction(int commID, int roomID){
        return new ActivateRoomAction(commID,roomID);
    }
    Action creatBattleGroundAction();

    Action createEndTurnAction();

    Action creatLeaveAction();

    Action creatStartGameAction();

    Action creatHireMonsterAction();

    Action creatMonsterAction();

    Action creatMonsterTargetAction();

    Action creatRegAction();

    Action creatPlaceBidAction();

    Action creatDigTunnelAction();

    Action creatTrapAction();

}
