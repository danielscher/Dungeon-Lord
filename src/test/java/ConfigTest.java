import de.unisaarland.cs.se.selab.game.Config;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

public class ConfigTest extends Config {

    private String mypath = "C:\\Users\\forgo\\group35\\src\\main\\resources\\configuration.json";
    Config cfg = new Config();

    @Test
    public void testParse() {
        cfg.display();
        cfg.setConfigFilePath(mypath);
        try {
            cfg.parse(mypath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        cfg.display();
    }

    @Test
    public void testgetter() {
        cfg.display();
        cfg.setConfigFilePath(mypath);
        try {
            cfg.parse(mypath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        cfg.display();
    }


}
