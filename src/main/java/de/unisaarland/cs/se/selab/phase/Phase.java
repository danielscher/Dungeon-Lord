package de.unisaarland.cs.se.selab.phase;

<<<<<<< HEAD
import de.unisaarland.cs.se.selab.game.TimeStamp;
import de.unisaarland.cs.se.selab.game.GameData;
=======
import de.unisaarland.cs.se.selab.game.Action.*;
import de.unisaarland.cs.se.selab.game.GameData;

>>>>>>> 2ac4208b23ce756f708c52efdc81d35221c06be2

public abstract class Phase {

    private GameData gd;

    public Phase(GameData gd) {
        this.gd = gd;
    }

    public Phase run() {
        //return NULL;
        return null;
    }

    private void exec(RegAction x) {

    }

    private void exec(StartGameAction x) {

    }

    private void exec(LeaveAction x) {

    }

    private void exec(EndTurnAction x) {

    }

    private void exec(HireMonsterAction x) {

    }

    private void exec(PlaceBidAction x) {

    }

    private void exec(MonsterAction x) {

    }

    private void exec(MonsterTargetedAction x) {

    }

    private void exec(BattleGroundAction x) {

    }

    private void exec(DigTunnelAction x) {

    }

    private void exec(ActivateRoomAction x) {

    }

    private void exec(TrapAction x) {

    }

    private void exec(BuildRoomAction x) {

    }

    private void exec(Action x) {

    }


}
