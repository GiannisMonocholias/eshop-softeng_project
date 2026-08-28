package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Deliverer} domain class.
 * Verifies the management of the deliverer's workload capacity via internal counters,
 * ensuring dynamic availability functions correctly without storing full Order objects.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererTest {

    private Deliverer delivery;

    @Before
    public void setUp() throws Exception {
        Date hireDate = new Date(3,5,2025);
        delivery = new Deliverer("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"DEL_1",100,1000,8,
                EmployeeState.ACTIVE, hireDate, 100, true);

        EmployeeDAOMemory.getInstance().clear();
    }

    @Test
    public void testAssignOrderIncrementsCount() {
        assertEquals(0, delivery.getAssignedOrdersCount());
        delivery.assignOrder();
        assertEquals(1, delivery.getAssignedOrdersCount());
    }

    @Test
    public void testCompleteOrderDecrementsCount() {
        delivery.assignOrder();
        delivery.assignOrder();
        assertEquals(2, delivery.getAssignedOrdersCount());

        delivery.completeOrder();
        assertEquals(1, delivery.getAssignedOrdersCount());
    }

    @Test
    public void testSetAndGetQuantity() {
        delivery.setQuantity(50);
        assertEquals(50, delivery.getQuantity());
    }

    @Test
    public void testGetAvailability() {
        delivery.setQuantity(2);
        assertTrue(delivery.getAvailability());

        delivery.assignOrder();
        assertTrue(delivery.getAvailability());

        delivery.assignOrder();
        assertFalse(delivery.getAvailability()); // Capacity reached
    }

    @After
    public void tearDown(){
        EmployeeDAOMemory.getInstance().clear();
    }
}