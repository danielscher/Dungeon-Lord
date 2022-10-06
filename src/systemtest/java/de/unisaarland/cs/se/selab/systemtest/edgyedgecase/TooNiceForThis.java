package de.unisaarland.cs.se.selab.systemtest.edgyedgecase;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.OurSystemTestFramework;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;

/**
 * this test has niceness bids which cannot be fulfilled because the player is already too nice
 */
public class TooNiceForThis extends OurSystemTestFramework {

    public TooNiceForThis() {
        super(TooNiceForThis.class, false);
    }

    @Override
    public String createConfig() {
        // use modded config
        return Utils.loadResource(TooNiceForThis.class, "nice_config.json");
    }


    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();

        sendRegister(0, "0");
        assertConfig(0, config);
        sendRegister(1, "1");
        assertConfig(1, config);

        currSockets.clear();
        currSockets.add(0);
        currSockets.add(1);


        sendStartGame(0);

        gameStartedAsserterHelper();

        assertPlayerHelper(new int[]{0, 1});

        nextYearAsserterHelper(1);
        nextRoundAsserterHelper(1);

        adventurerDrawnAsserterHelper(29);
        adventurerDrawnAsserterHelper(23);
        monsterDrawnAsserterHelper(23);
        monsterDrawnAsserterHelper(13);
        monsterDrawnAsserterHelper(9);

        roomDrawnAsserterHelper(5);
        roomDrawnAsserterHelper(4);

        // TODO: continue

        // assert next year, next round , draw monster, etc..
        // can ignore
        this.sendLeave(0);
        this.sendLeave(1);
        this.sendLeave(2);
        this.sendLeave(3);
    }

}
