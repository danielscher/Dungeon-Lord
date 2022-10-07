package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;
import de.unisaarland.cs.se.selab.systemtest.cambatphase.CombatAllPlayerEndTurn;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.ActivateRoomWhilePlacingBidFourthSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.ActivateRoomWithoutRoomFourthSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.AllKindsofBidsFourthSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenFood;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenGold;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenImp;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenMonster;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenNice;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenRoom;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenTrap;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BidTypeTakenTunnel;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.BlockedBidSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.PlayerLeftWhileChoosingMonsterThirdSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.PlayerLeftWhileDiggingTunnelSecondSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.PlayerLeftWhilePlacingBidFirstSeason;
import de.unisaarland.cs.se.selab.systemtest.collectplacebids.PlayerLeftWhilePlacingRoomThirdSeason;
import de.unisaarland.cs.se.selab.systemtest.combatphasefirstyear.CombatPhaseFirstRound;
import de.unisaarland.cs.se.selab.systemtest.edgyedgecase.InvalidActionInRegPhase;
import de.unisaarland.cs.se.selab.systemtest.edgyedgecase.TooNiceForThis;
import de.unisaarland.cs.se.selab.systemtest.edgyedgecase.TunnelDiggers;
import de.unisaarland.cs.se.selab.systemtest.evalroom.BiddingFoodTunnelRoom;
import de.unisaarland.cs.se.selab.systemtest.evalroom.BiddingOnRoomBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnGoldBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnImpsBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnImpsCantAffordSlot3;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnMonsterBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.BiddingOnTrapsBasic;
import de.unisaarland.cs.se.selab.systemtest.evaltomonster.PlaceBidOnMonsterAndLeave;
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
        manager.registerTest(new InvalidActionInRegPhase());
        /*

        // cambatphase package
        manager.registerTest(new CombatAllPlayerEndTurn());


        // systemtest package
        //manager.registerTest(new BrokenConfigTest());
        //manager.registerTest(new EmptyConfigTest());

        manager.registerTest(new FrameworkuptoBiddingSecondSeason());
        manager.registerTest(new OurSystemTestFramework());
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new FrameworkuptoBiddingFourthSeason());
        manager.registerTest(new FrameworkuptoBiddingSecondSeason());
        manager.registerTest(new FrameworkuptoBiddingThirdSeason());



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
        manager.registerTest(new EvalUpToTunnelEvalFoodTwoPlayerYaSa());
        manager.registerTest(new EvalUpToTunnelEvalFoodYaSa());
        manager.registerTest(new EvalUpToTunnelEvalNicenessYaSa());
        manager.registerTest(new EvalUpToTunnelEvalNoTunnelYaSa());
        manager.registerTest(new EvalUpToTunnelEvalOnlyTunnelYaSa());

        // evaluptomonster package
        manager.registerTest(new BiddingOnGoldBasic());
        manager.registerTest(new BiddingOnImpsBasic());
        manager.registerTest(new BiddingOnImpsCantAffordSlot3());
        manager.registerTest(new BiddingOnMonsterBasic());
        manager.registerTest(new BiddingOnTrapsBasic());
        manager.registerTest(new PlaceBidOnMonsterAndLeave());
        manager.registerTest(new Send4ImpsToMineGold());

        // collectandplacebids
        manager.registerTest(new ActivateRoomWhilePlacingBidFourthSeason());
        manager.registerTest(new ActivateRoomWithoutRoomFourthSeason());
        manager.registerTest(new AllKindsofBidsFourthSeason());
        manager.registerTest(new BidTypeTakenFood());
        manager.registerTest(new BidTypeTakenGold());
        manager.registerTest(new BidTypeTakenImp());
        manager.registerTest(new BidTypeTakenMonster());
        manager.registerTest(new BidTypeTakenNice());
        manager.registerTest(new BidTypeTakenRoom());
        manager.registerTest(new BidTypeTakenTrap());
        manager.registerTest(new BidTypeTakenTunnel());
        manager.registerTest(new BlockedBidSecondSeason());
        manager.registerTest(new PlayerLeftWhileChoosingMonsterThirdSeason());
        manager.registerTest(new PlayerLeftWhileDiggingTunnelSecondSeason());
        manager.registerTest(new PlayerLeftWhilePlacingBidFirstSeason());
        manager.registerTest(new PlayerLeftWhilePlacingRoomThirdSeason());


        // combat
        // combatphasefirstyear package
        manager.registerTest(new CombatPhaseFirstRound());

        // edgyedgycase package
        manager.registerTest(new TooNiceForThis());
        manager.registerTest(new TunnelDiggers());

        // evalroom package
        manager.registerTest(new BiddingFoodTunnelRoom());
        manager.registerTest(new BiddingOnRoomBasic());

         */



    }
}
