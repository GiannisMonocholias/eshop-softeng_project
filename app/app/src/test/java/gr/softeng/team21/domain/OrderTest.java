package gr.softeng.team21.domain;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.MemoryInitializer;
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

    @Test
    public void getOrdercode() {
        assertEquals("ORD-1001", order.getOrderCode());
    }

    @Test
    public void setOrdercode() {
        order.setOrderCode("ORD-7");
        assertEquals("ORD-7", order.getOrderCode());
    }

    @Test
    public void getSubmissiondate() {
        assertEquals(subDate, order.getSubmissiondate());
    }

    @Test
    public void setSubmissiondate() {
        Date newDate = new Date();
        order.setSubmissionDate(newDate);
        assertEquals(newDate, order.getSubmissiondate());
    }

    @Test
    public void getDeliverydate() {
        assertEquals(delDate, order.getDeliverydate());
    }

    @Test
    public void setDeliverydate() {
        Date newDate = new Date();
        order.setDeliveryDate(newDate);
        assertEquals(newDate, order.getDeliverydate());
    }

    @Test
    public void getOrderstatus() {
        assertEquals(OrderStatusType.NEW, order.getOrderStatus());
    }

    @Test
    public void setOrderstatus() {
        order.setOrderStatus(OrderStatusType.DELIVERED);
        assertEquals(OrderStatusType.DELIVERED, order.getOrderStatus());
    }

    @Test
    public void getPaid() {
        assertFalse(order.isPaid());
    }

    @Test
    public void setPaid() {
        order.setPaid(true);
        assertTrue(order.isPaid());
    }

    @Test
    public void getPaymentmethod() {
        assertEquals(PaymentType.CASH, order.getPaymentMethod());
    }

    @Test
    public void setPaymentmethod() {
        order.setPaymentMethod(PaymentType.CARD);
        assertEquals(PaymentType.CARD, order.getPaymentMethod());
    }

    @Test
    public void getShoppingCart() {
        assertNotSame(cart, order.getShoppingCart());
    }

    @Test
    public void setShoppingCart() {
        ShoppingCart newCart = new ShoppingCart();
        order.setShoppingCart(newCart);
        assertEquals(newCart, order.getShoppingCart());
    }

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

    @AfterClass
    public static void tearDownAfterClass() {
        MemoryInitializer.getCustomerDAO().clear().join();
    }
}