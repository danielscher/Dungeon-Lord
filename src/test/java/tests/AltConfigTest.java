package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.Config;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class AltConfigTest extends Config {

    // use this test with a valid config

    /**
     * @throws JSONException when parsing fails
     */
    @Test
    void testParseCorrectConfig() {
        final String configPath = "INSERT PATH HERE"; // TODO insert path
        final AltConfig config = new AltConfig(configPath, 42);
        final boolean parsedSuccessfully = config.parse();
        assertTrue(parsedSuccessfully, "config validation failed");
    }

    // use this test with an invalid config

    /**
     *
     * @throws JSONException when parsing fails
     */
    @Test
    void testParseIncorrectConfig() {
        final String configPath = "INSERT PATH HERE"; // TODO insert path
        final AltConfig config = new AltConfig(configPath, 42);
        final boolean parsedSuccessfully = config.parse();
        assertTrue(parsedSuccessfully, "config validation failed");
    }


}
