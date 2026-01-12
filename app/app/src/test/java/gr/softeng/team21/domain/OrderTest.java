package gr.softeng.team21.domain;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Order} class.
 * Verifies getters, setters, shopping cart association and total cost calculation.
 * @author PAVLOS GRATSANIS
 */
public class OrderTest {

    private Order order;
    private Date subDate;
    private Date delDate;
    private ShoppingCart cart;

    /**
     * Sets up the test environment before each test.
     * Initializes dates, a shopping cart and a default order .
     */
    @Before
    public void setUp() throws Exception {
        subDate = new Date();
        delDate = new Date();
        delDate.changeDays(20);
        cart = new ShoppingCart();

        order = new Order(
                "ORD-1001",
                subDate,
                OrderStatusType.NEW,
                false,
                PaymentType.CASH,
                delDate,
                cart
        );
    }

    /**
     * Tests the getOrdercode method.
     */
    @Test
    public void getOrdercode() {
        assertEquals("ORD-1001", order.getOrdercode());
    }

    /**
     * Tests the setOrdercode method.
     */
    @Test
    public void setOrdercode() {
        order.setOrdercode("ORD-7");
        assertEquals("ORD-7", order.getOrdercode());
    }

    /**
     * Tests the getSubmissiondate method.
     */
    @Test
    public void getSubmissiondate() {
        assertEquals(subDate, order.getSubmissiondate());
    }

    /**
     * Tests the setSubmissiondate method.
     */
    @Test
    public void setSubmissiondate() {
        Date newDate = new Date();
        order.setSubmissiondate(newDate);
        assertEquals(newDate, order.getSubmissiondate());
    }

    /**
     * Tests the getDeliverydate method.
     */
    @Test
    public void getDeliverydate() {
        assertEquals(delDate, order.getDeliverydate());
    }

    /**
     * Tests the setDeliverydate method.
     */
    @Test
    public void setDeliverydate() {
        Date newDate = new Date();
        order.setDeliverydate(newDate);
        assertEquals(newDate, order.getDeliverydate());
    }

    /**
     * Tests the getOrderstatus method.
     */
    @Test
    public void getOrderstatus() {
        assertEquals(OrderStatusType.NEW, order.getOrderstatus());
    }

    /**
     * Tests the setOrderstatus method.
     */
    @Test
    public void setOrderstatus() {
        order.setOrderstatus(OrderStatusType.DELIVERED);
        assertEquals(OrderStatusType.DELIVERED, order.getOrderstatus());
    }

    /**
     * Tests the getPaid method.
     */
    @Test
    public void getPaid() {
        assertFalse(order.getPaid());
    }

    /**
     * Tests the setPaid method.
     */
    @Test
    public void setPaid() {
        order.setPaid(true);
        assertTrue(order.getPaid());
    }

    /**
     * Tests the getPaymentmethod method.
     */
    @Test
    public void getPaymentmethod() {
        assertEquals(PaymentType.CASH, order.getPaymentmethod());
    }

    /**
     * Tests the setPaymentmethod method.
     */
    @Test
    public void setPaymentmethod() {
        order.setPaymentmethod(PaymentType.CARD);
        assertEquals(PaymentType.CARD, order.getPaymentmethod());
    }

    /**
     * Tests the getShoppingCart method.
     * Verifies that the order contains a copy of the original cart, not the same reference.
     */
    @Test
    public void getShoppingCart() {
        assertNotSame(cart, order.getShoppingCart());
    }

    /**
     * Tests the setShoppingCart method.
     */
    @Test
    public void setShoppingCart() {
        ShoppingCart newCart = new ShoppingCart();
        order.setShoppingCart(newCart);
        assertEquals(newCart, order.getShoppingCart());
    }

    /**
     * Tests the getTotal_amount method.
     * Verifies that the order's total amount matches the shopping cart's total cost after checkout.
     */
    @Test
    public void getTotal_amount() {
        Customer c = TestHelper.getCustomer();
        ShoppingCart cart1 = new ShoppingCart();
        c.setShoppingCart(cart1);
        c.addItemToCart(TestHelper.getLaptop(), 5);
        c.addItemToCart(TestHelper.getMonitor(), 6);

        Order order1 = c.Checkout();
        assertEquals(cart1.getTotalCost(), order1.getTotal_amount());
    }

    /**
     * Cleans up after all tests in the class have run.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}