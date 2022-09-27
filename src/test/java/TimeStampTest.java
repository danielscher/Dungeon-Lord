import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import de.unisaarland.cs.se.selab.game.TimeStamp;
import org.junit.jupiter.api.Test;

public class TimeStampTest extends TimeStamp {
    TimeStamp t = new TimeStamp();
    private int season, year;
    public void TimeStamp(){
        this.season = 1;
        this.year = 1;
    }

    @Test
    public void testGetSeason(){
        assertEquals(1, t.getSeason());
    }

    @Test
    public void testGetYear(){
        assertEquals(1, t.getYear());
    }

    @Test
    public void testNextSeason(){
        t.nextSeason();
        assertEquals(2, t.getSeason());
    }

    @Test
    public void testNextYear(){
        t.nextyear();
        assertEquals(2, t.getYear());
    }

}
