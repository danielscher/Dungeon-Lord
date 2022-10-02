package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.Config;
import java.nio.file.Path;
import java.util.Objects;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class AltConfigTest extends Config {



    // use this test with a valid config

    /**
     * @throws JSONException when parsing fails
     */
    @Test
    void testParseCorrectConfig() {
        final Path configPath = readFile("configuration.json");
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
        final Path configPath = readFile("configuration.json");
        final AltConfig config = new AltConfig(configPath, 42);
        final boolean parsedSuccessfully = config.parse();
        assertTrue(parsedSuccessfully, "config validation failed");
    }

    private Path readFile(final String fileName) {
        return Path.of(Objects.requireNonNull(getClass().getResource(fileName)).getPath());
    }


}
