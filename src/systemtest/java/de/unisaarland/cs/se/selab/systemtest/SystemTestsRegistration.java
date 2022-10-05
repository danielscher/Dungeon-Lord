package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;


final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new OurSystemTestFramework());
        manager.registerTest(new UpToGameStarted());
        manager.registerTest(new UpToPlayer());
        manager.registerTest(new UpToNextYear());
        manager.registerTest(new UpToNextRound());
        manager.registerTest(new UpToAdventurerDrawn());
        manager.registerTest(new UpToMonsterDrawn());
        manager.registerTest(new UpToRoomDrawn());
        manager.registerTest(new UpToBiddingStarted());
        manager.registerTest(new UpToActNow());
        manager.registerTest(new SystemTestTemplate());
    }
}
