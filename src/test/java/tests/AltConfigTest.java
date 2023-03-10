package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class AltConfigTest {

    // use this test with a valid config

    /**
     * @throws JSONException when parsing fails
     */

    @Test
    void testParseCorrectConfig() {
        URI fileIdentifier;
        try {
            fileIdentifier = getClass().getClassLoader().getResource("configuration.json")
                    .toURI();
        } catch (URISyntaxException e) {
            // empty
            return;
        }
        final Path configPath = Path.of(fileIdentifier);
        final AltConfig config = new AltConfig(configPath, 42);
        final boolean parsedSuccessfully = config.parse();
        assertTrue(parsedSuccessfully, "config validation failed");
    }

    // use this test with an invalid config

    /**
     * @throws JSONException when parsing fails
     */
    @Test
    void testParseIncorrectConfig() {
        final Path configPath = Path.of("src\\main\\resources\\config_broken.json");
        final AltConfig config = new AltConfig(configPath, 42);
        final boolean parsedSuccessfully = config.parse();
        assertFalse(parsedSuccessfully, "config validation failed");
    }


}
