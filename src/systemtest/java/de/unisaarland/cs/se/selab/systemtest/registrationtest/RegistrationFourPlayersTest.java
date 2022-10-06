package de.unisaarland.cs.se.selab.systemtest.registrationtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class RegistrationFourPlayersTest extends SystemTest {

    public RegistrationFourPlayersTest() {
        super(RegistrationFourPlayersTest.class, false);
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
        this.sendRegister(4, "4");
        this.assertConfig(4, config);

        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);
        this.assertGameStarted(4);
        this.sendStartGame(1);
        this.assertActionFailed(1); // FIXME assertion wrong, first player events will be
        // FIXME received

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
        this.sendLeave(4);
    }
}