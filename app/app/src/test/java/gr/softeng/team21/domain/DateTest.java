package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Date;

public class DateTest {
    private Date date;
    @Before
    public void setUp ( ) throws Exception {
        date = new Date(2, 12, 2025);
    }

    @Test
    public void addDays ( ) {
        date.changeDays (20);
        assertEquals(22, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2025, date.getYear());

    }
    @Test
    public void romeveDays ( ) {
        date.changeDays (-1);
        assertEquals(1, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2025, date.getYear());

    }
}