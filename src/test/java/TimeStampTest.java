import static org.junit.jupiter.api.Assertions.assertEquals;

import de.unisaarland.cs.se.selab.game.TimeStamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeStampTest {

    TimeStamp ts = new TimeStamp();

    @BeforeEach
    public void resettimeStamp() {
        ts = new TimeStamp();
    }

    @Test
    public void testGetSeason() {
        assertEquals(1, ts.getSeason());
    }

    @Test
    public void testGetYear() {
        assertEquals(1, ts.getYear());
    }

    @Test
    public void testNextSeason() {
        ts.nextSeason();
        assertEquals(2, ts.getSeason());
    }

    @Test
    public void testNextYear() {
        ts.nextyear();
        assertEquals(2, ts.getYear());
    }

}
