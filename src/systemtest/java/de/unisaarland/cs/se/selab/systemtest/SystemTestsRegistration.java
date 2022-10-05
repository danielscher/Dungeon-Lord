package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;


final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new OurSystemTestFramework());
        manager.registerTest(new SystemTestTemplate());

        //this below are Eval up to tunnel edge cases.
        manager.registerTest(new EvalUpToTunnelAllPlayerLeft());
    }
}
