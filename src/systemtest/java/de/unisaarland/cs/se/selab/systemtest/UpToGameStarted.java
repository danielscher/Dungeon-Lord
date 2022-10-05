package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * Register 2 Players and Leave
 */
public class UpToGameStarted extends OurSystemTestFramework {

    UpToGameStarted() {
        super(UpToGameStarted.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(UpToGameStarted.class, "configuration.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        this.sendRegister(0, "0");
        this.assertConfig(0, config);
        this.sendRegister(1, "1");
        this.assertConfig(1, config);
        this.sendRegister(2, "2");
        this.assertConfig(2, config);
        this.sendRegister(3, "3");
        this.assertConfig(3, config);

        this.assertGameStarted(0);
        this.assertGameStarted(1);
        this.assertGameStarted(2);
        this.assertGameStarted(3);


        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
