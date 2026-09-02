package gr.softeng.team21.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link OrderPreparationEmployee} domain class.
 * Validates proper initialization and correct functionality of the employee's
 * performance tracking statistics.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployeeTest {

    private OrderPreparationEmployee employee;

    /**
     * Initializes a pure domain instance before each test.
     */
    @Before
    public void setUp() {
        employee = new OrderPreparationEmployee(
                "GP", "Giorgos", "abcd123", "Papadopoulos",
                "3029761482", new EmailAddress("GP@gmail.com"), "OPE_1",
                100, 1000, 8, EmployeeState.ACTIVE, new Date(3, 5, 2025)
        );
    }

    /**
     * Verifies that newly created employees start with zero performance counters.
     */
    @Test
    public void testInitialStatisticsValues() {
        Assert.assertEquals(0, employee.getTotalOrdersPreparations());
        Assert.assertEquals(0, employee.getTotalUpdateReserveRequests());
    }

    /**
     * Verifies that the prepared orders counter increments correctly.
     */
    @Test
    public void testIncrementOrdersPrepared() {
        Assert.assertEquals(0, employee.getTotalOrdersPreparations());

        employee.incrementOrdersPrepared();
        employee.incrementOrdersPrepared();

        Assert.assertEquals(2, employee.getTotalOrdersPreparations());
    }

    /**
     * Verifies that the update reserve requests counter increments correctly.
     */
    @Test
    public void testIncrementUpdateReserveRequests() {
        Assert.assertEquals(0, employee.getTotalUpdateReserveRequests());

        employee.incrementUpdateReserveRequests();

        Assert.assertEquals(1, employee.getTotalUpdateReserveRequests());
    }
}