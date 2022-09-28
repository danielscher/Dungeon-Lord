import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.game.Config;
import de.unisaarland.cs.se.selab.game.util.Coordinate;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConfigTest extends Config {

    private String myPath00 ="C:\\Users\\forgo\\group35\\src\\main\\resources\\configuration.json";
    Config cfg= new Config ();

    @Test
    public void testParse() {
        cfg.display();
        cfg.setConfigFilePath(myPath00);
        try {
            assertTrue( cfg.parse(myPath00));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        cfg.display();
    }


}
