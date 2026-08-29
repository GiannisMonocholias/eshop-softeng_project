package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link UpdateCatalogueEmployee} class.
 * Verifies domain logic and counter increments independently of DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeTest {

    private UpdateCatalogueEmployee employee;

    @Before
    public void setUp(){
        employee = new UpdateCatalogueEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"OPE_1",100,1000,8,
                EmployeeState.ACTIVE, new Date(3,5,2025));
    }

    /**
     * Verifies that a new employee starts with zero recorded catalogue updates.
     */
    @Test
    public void getTotalCatalogueUpdatesInitiallyZeroTest() {
        assertEquals(0, employee.getTotalCatalogueUpdates());
    }

    /**
     * Tests the successful increment of the employee's executed updates counter.
     */
    @Test
    public void testIncrementTotalCatalogueUpdates() {
        employee.incrementTotalCatalogueUpdates();
        employee.incrementTotalCatalogueUpdates();
        assertEquals(2, employee.getTotalCatalogueUpdates());
    }
}