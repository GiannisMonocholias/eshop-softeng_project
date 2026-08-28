package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link CustomerServiceEmployee} pure domain class.
 * This suite verifies the employee's ability to manage their assigned orders
 * list and track their total response metrics.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeTest {

    private CustomerServiceEmployee employee;
    private Order order1;
    private Order order2;

    /**
     * Sets up the testing environment before each test.
     * Initializes a Customer Service Employee and sample orders.
     */
    @Before
    public void setUp() {
        employee = new CustomerServiceEmployee("GP","Giorgos","abcd123",
                "Papadopoulos","3029761482",new EmailAddress("GP@gmail.com"),
                "CS_1",100,1000,8,EmployeeState.ACTIVE, new Date(3,5,2025));

        order1 = new Order("order1246", new Date(), OrderStatusType.NEW,
                false, PaymentType.CASH, new Date(), new ShoppingCart());
        order2 = new Order("order9999", new Date(), OrderStatusType.SHIPPED,
                false, PaymentType.CARD, new Date(), new ShoppingCart());
    }

    /**
     * Verifies that adding and removing orders updates the list correctly.
     */
    @Test
    public void testAddAndRemoveOrders() {
        assertEquals(0, employee.getOrders().size());

        employee.addOrder(order1);
        assertEquals(1, employee.getOrders().size());
        assertEquals(order1, employee.getOrders().get(0));

        employee.removeOrder(order1);
        assertEquals(0, employee.getOrders().size());
    }

    /**
     * Verifies that setting a complete list of orders overwrites the existing one.
     */
    @Test
    public void testSetOrders() {
        ArrayList<Order> newOrders = new ArrayList<>();
        newOrders.add(order1);
        newOrders.add(order2);

        employee.setOrders(newOrders);
        assertEquals(2, employee.getOrders().size());
    }

    /**
     * Verifies that the total responses counter increments correctly.
     */
    @Test
    public void testIncrementTotalResponses() {
        assertEquals(0, employee.getTotalResponses());

        employee.incrementTotalResponses();
        assertEquals(1, employee.getTotalResponses());
    }
}