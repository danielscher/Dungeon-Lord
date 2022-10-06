package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenFood;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenGold;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenImp;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenMonster;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenNice;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenRoom;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenTrap;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenTunnel;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BlockedBidSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnImpsBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnMonsterBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnTrapsBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.Send4ImpsToMineGold;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelAllPlayerLeftYaSa;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelEvalFoodTwoPlayerYaSa;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelEvalFoodYaSa;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelEvalNicenessYaSa;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelEvalNoTunnelYaSa;
import de.unisaarland.cs.se.selab.systemtest.evaluptotunnel.EvalUpToTunnelEvalOnlyTunnelYaSa;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.FivePlayerAndStartTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.RegistrationFourPlayersTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.ThreePlayerAndStartTest;

final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {

        // systemtest package
        // manager.registerTest(new BrokenConfigTest());
        // manager.registerTest(new EmptyConfigTest());
        manager.registerTest(new FrameworkuptoBiddingSecondSeason());
        manager.registerTest(new OurSystemTestFramework());
        manager.registerTest(new RegistrationTest());

        manager.registerTest(new UpToGameStarted());
        manager.registerTest(new UpToPlayer());
        manager.registerTest(new UpToNextYear());
        manager.registerTest(new UpToNextRound());
        manager.registerTest(new UpToAdventurerDrawn());
        manager.registerTest(new UpToMonsterDrawn());
        manager.registerTest(new UpToRoomDrawn());
        manager.registerTest(new UpToBiddingStarted());
        manager.registerTest(new UpToActNow());

        // registrationtest package
        manager.registerTest(new FivePlayerAndStartTest());
        manager.registerTest(new RegistrationFourPlayersTest());
        manager.registerTest(new ThreePlayerAndStartTest());

        // evaluptotunnel package
        manager.registerTest(new EvalUpToTunnelAllPlayerLeftYaSa());
        manager.registerTest(new EvalUpToTunnelEvalFoodYaSa());
        manager.registerTest(new EvalUpToTunnelEvalNicenessYaSa());
        manager.registerTest(new EvalUpToTunnelEvalFoodTwoPlayerYaSa());
        manager.registerTest(new EvalUpToTunnelEvalOnlyTunnelYaSa());
        manager.registerTest(new EvalUpToTunnelEvalNoTunnelYaSa());
        manager.registerTest(new Send4ImpsToMineGold());

        // evaluptomonster package
        manager.registerTest(new BiddingOnGoldBasic());
        manager.registerTest(new BiddingOnMonsterBasic());
        manager.registerTest(new BiddingOnTrapsBasic());
        manager.registerTest(new BiddingOnImpsBasic());

        // collectandplacebids
        manager.registerTest(new BidTypeTakenFood());
        manager.registerTest(new BidTypeTakenGold());
        manager.registerTest(new BidTypeTakenImp());
        manager.registerTest(new BidTypeTakenMonster());
        manager.registerTest(new BidTypeTakenNice());
        manager.registerTest(new BidTypeTakenRoom());
        manager.registerTest(new BidTypeTakenTrap());
        manager.registerTest(new BidTypeTakenTunnel());
        manager.registerTest(new BlockedBidSecondSeason());

    }
}
