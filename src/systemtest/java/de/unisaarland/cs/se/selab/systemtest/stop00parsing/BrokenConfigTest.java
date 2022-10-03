package de.unisaarland.cs.se.selab.systemtest.stop00parsing;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class BrokenConfigTest extends SystemTest {

    protected BrokenConfigTest() {
        super(BrokenConfigTest.class, true);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "config_broken.json");
    }

    @Override
    protected long createSeed() {
        return 0;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        // fail to parser, ParserMessage != SUCCESS
    }
}
