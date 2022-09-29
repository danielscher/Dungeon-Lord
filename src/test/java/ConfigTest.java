import de.unisaarland.cs.se.selab.game.Config;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

public class ConfigTest extends Config {

    private final String myPath = "C:\\Users\\forgo\\group35\\src\\main\\resources\\configuration.json";
    private final String myPathFail = "C:\\Users\\forgo\\group35\\src\\main\\resources\\config_broken.json";
    Config cfg = new Config();

    @Test
    public void testParseOk() {
        //    cfg.displayLog();
        cfg.setConfigFilePath(myPath);
        try {
            cfg.parse(myPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //    cfg.displayLog();
    }

    public void testParseNotOk() {
        //    cfg.displayLog();
        cfg.setConfigFilePath(myPathFail);
        try {
            cfg.parse(myPathFail);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //    cfg.displayLog();
    }

    public void testParseNotOkM1() {
        //    cfg.displayLog();
        cfg.setConfigFilePath(myPath);
        try {
            cfg.parse(myPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //    cfg.displayLog();
    }

    @Test
    public void testgetter() {
        //    cfg.displayLog();
        cfg.setConfigFilePath(myPath);
        try {
            cfg.parse(myPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        cfg.displayLog();
    }

    @Test
    public void testConvertAttack() {
    }

    @Test
    public void testConvertLocation() {
    }

    @Test
    public void testDeleteMonsters() {
    }

    @Test
    public void testDeleteAdventurers() {
    }

    @Test
    public void testDeleteTraps() {
    }

    @Test
    public void testDeleteRooms() {
    }

}
