import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.TimeStamp;
import org.junit.jupiter.api.Test;

public class TimeStampTest {
    TimeStamp t = new TimeStamp();
    private int season, year;
    public void TimeStamp(){
        this.season = 1;
        this.year = 1;
    }

    @Test
    public void TestGetSeason(){
        assertEquals(1, t.getSeason());
    }

    @Test
    public void TestGetYear(){
        assertEquals(1, t.getYear());
    }

    @Test
    public void TestNextSeason(){
        t.nextSeason();
        assertEquals(2, t.getSeason());
    }

    @Test
    public void TestNextYear(){
        t.nextyear();
        assertEquals(2, t.getYear());
    }

}
