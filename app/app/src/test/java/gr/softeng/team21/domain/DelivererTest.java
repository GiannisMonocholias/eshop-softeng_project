package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*; // Χρησιμοποιούμε μόνο JUnit 4 Assertions

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

public class DelivererTest {

    Deliverer delivery;
    Order order;

    @Before
    public void setUp() throws Exception {
        Date hireDate = new Date(3,5,2025);
        delivery = new Deliverer("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"DEL_1",100,1000,8,
                EmployeeState.ACTIVE, hireDate,100 , true );
        order = new Order("001" , new Date() , OrderStatusType.NEW , false ,PaymentType.CASH, new Date() , new ShoppingCart());

        EmployeeDAOMemory.getInstance().clear();

    }

    @Test
    public void addOrder() {

        int before = delivery.getOrders().size();

        delivery.addOrder(order);

        int after = delivery.getOrders().size();


        assertEquals(before + 1 , after);
    }

    @Test
    public void checkfor() {

        delivery.addOrder(order);

        boolean found = delivery.checkfor(order);

        assertTrue(found);

        Order order1 = new Order("008" , new Date() , OrderStatusType.NEW , false ,PaymentType.CASH, new Date() , new ShoppingCart());

        assertFalse(delivery.checkfor(order1));
    }

    @Test
    public void testSetAndGetQuantity() {
        int expectedQuantity = 100;
        delivery.setQuantity(expectedQuantity);
        assertEquals(expectedQuantity, delivery.getQuantity());
    }

    @Test
    public void testGetAvailability() {
        assertTrue(delivery.getAvailability());
    }

    @Test
    public void testGetOrders() {
        assertNotNull(delivery.getOrders());
        delivery.addOrder(order);
        assertEquals(1, delivery.getOrders().size());
        assertEquals(order, delivery.getOrders().get(0));
    }

    @After
    public void tearDown(){
        EmployeeDAOMemory.getInstance().clear();
    }
}