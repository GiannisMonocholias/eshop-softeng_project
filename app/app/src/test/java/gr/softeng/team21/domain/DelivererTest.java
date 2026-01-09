package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*; // Χρησιμοποιούμε μόνο JUnit 4 Assertions

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Deliverer} domain class.
 * This class verifies the management of orders assigned to a deliverer,
 * as well as the correct functioning of availability and quantity properties.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererTest {

    Deliverer delivery;
    Order order;

    /**
     * Initializes the testing environment before each test case.
     * Sets up a Deliverer instance and a sample Order, while clearing the
     * Employee memory DAO to ensure test isolation.
     * @throws Exception if initialization fails.
     */
    @Before
    public void setUp() throws Exception {
        Date hireDate = new Date(3,5,2025);
        delivery = new Deliverer("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"DEL_1",100,1000,8,
                EmployeeState.ACTIVE, hireDate,100 , true );
        order = new Order("001" , new Date() , OrderStatusType.NEW , false ,PaymentType.CASH, new Date() , new ShoppingCart());

        EmployeeDAOMemory.getInstance().clear();

    }

    /**
     * Verifies that an order is successfully added to the deliverer's task list.
     */
    @Test
    public void addOrder() {

        int before = delivery.getOrders().size();

        delivery.addOrder(order);

        int after = delivery.getOrders().size();


        assertEquals(before + 1 , after);
    }

    /**
     * Tests the ability to search for a specific order within the deliverer's current assignments.
     * Validates both positive (order exists) and negative (order does not exist) scenarios.
     */
    @Test
    public void checkfor() {

        delivery.addOrder(order);

        boolean found = delivery.checkfor(order);

        assertTrue(found);

        Order order1 = new Order("008" , new Date() , OrderStatusType.NEW , false ,PaymentType.CASH, new Date() , new ShoppingCart());

        assertFalse(delivery.checkfor(order1));
    }

    /**
     * Verifies the getter and setter for the quantity property, representing
     * the deliverer's capacity or current load.
     */
    @Test
    public void testSetAndGetQuantity() {
        int expectedQuantity = 100;
        delivery.setQuantity(expectedQuantity);
        assertEquals(expectedQuantity, delivery.getQuantity());
    }

    /**
     * Validates the deliverer's initial availability status.
     */
    @Test
    public void testGetAvailability() {
        assertTrue(delivery.getAvailability());
    }

    /**
     * Verifies that the order list is properly initialized and accurately
     * reflects the orders assigned to the deliverer.
     */
    @Test
    public void testGetOrders() {
        assertNotNull(delivery.getOrders());
        delivery.addOrder(order);
        assertEquals(1, delivery.getOrders().size());
        assertEquals(order, delivery.getOrders().get(0));
    }

    /**
     * Cleans up the memory DAO after each test execution to maintain a clean state.
     */
    @After
    public void tearDown(){
        EmployeeDAOMemory.getInstance().clear();
    }
}