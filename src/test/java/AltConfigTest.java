import static org.junit.jupiter.api.Assertions.assertTrue;

import de.unisaarland.cs.se.selab.game.AltConfig;
import de.unisaarland.cs.se.selab.game.Config;
import org.junit.jupiter.api.Test;

class AltConfigTest extends Config {

    private String mypath = "";
    AltConfig config = new AltConfig(mypath);

    @Test
    void testParse() {
        try {
            boolean parsedSuccessfully = config.parse();
            assertTrue(parsedSuccessfully, "config validation failed");
        } catch (Exception e) {
            assertTrue(false, "parsing failed" + e.toString());
        }
    }


}
