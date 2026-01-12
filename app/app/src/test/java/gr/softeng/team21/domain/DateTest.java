package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Date} class.
 * Verifies the date manipulation logic, specifically adding and removing days.
 *
 * @author PAVLOS GRATSANIS
 */
public class DateTest {
    private Date date;

    /**
     * Sets up a Date instance before each test execution.
     * Initialized to 2/12/2025.
     */
    @Before
    public void setUp() throws Exception {
        date = new Date(2, 12, 2025);
    }

    /**
     * Tests the changeDays method with a positive integer.
     * Verifies that days are added correctly.
     */
    @Test
    public void addDays() {
        date.changeDays(20);
        assertEquals(22, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2025, date.getYear());
    }

    /**
     * Tests the changeDays method with a negative integer.
     * Verifies that days are subtracted correctly.
     */
    @Test
    public void removeDays() {
        date.changeDays(-1);
        assertEquals(1, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2025, date.getYear());
    }
}