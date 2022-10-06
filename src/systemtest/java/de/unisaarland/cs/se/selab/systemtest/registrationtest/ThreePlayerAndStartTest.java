package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class ThreePlayerAndStartTest extends SystemTest {

    public ThreePlayerAndStartTest() {
        super(ThreePlayerAndStartTest.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationFourPlayersTest.class, "configuration.json");
    }

    @Override
    public long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1, 2, 3, 4);
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();
        this.sendRegister(1, "Niklas");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);
        this.sendRegister(3, "3");
        this.assertConfig(3, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);

        this.sendRegister(4, "4");
        this.assertActionFailed(4);

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }
}
