package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.TestHelper;

public class OrderDAOMemoryTest {
    private OrderDAOMemory orderDAOMemory;
    private Order order1;

    @Before // Αντικατάσταση του @BeforeEach
    public void setUp(){
        orderDAOMemory = OrderDAOMemory.getInstance();
        orderDAOMemory.clear(); // Καθαρισμός για απομόνωση των tests

        order1 = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        // Υποθέτουμε ότι η TestHelper.getLaptop() επιστρέφει ProductType/WholesaleProduct
        order1.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop(), 2));
    }

    @Test
    public void getInstanceReturnsSameReferences() {
        OrderDAOMemory orderDAOMemory2 = OrderDAOMemory.getInstance();
        assertSame(orderDAOMemory, orderDAOMemory2);
    }

    @Test
    public void testGetOrdersInitiallyEmpty() {
        assertTrue(orderDAOMemory.getOrders().isEmpty());
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ getOrderNonValidArgumentsTest
    // -------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void getOrder_NullArgumentTest() {
        // pass null argument in getOrder()
        orderDAOMemory.getOrder(null);
    }

    @Test
    public void getOrder_NonExistingOrderTest() {
        // Request a non existing order
        assertNull(orderDAOMemory.getOrder("order1245"));
    }

    @Test
    public void getOrderTestSuccess(){
        orderDAOMemory.addOrder(order1);
        Order returnedOrder = orderDAOMemory.getOrder("order1246");
        assertTrue(orderDAOMemory.getOrders().containsKey("order1246"));
        assertSame(returnedOrder, orderDAOMemory.getOrder("order1246"));
    }

    @Test
    public void addOrderTestSuccess() {
        orderDAOMemory.addOrder(order1);

        assertEquals(order1, orderDAOMemory.getOrder("order1246"));
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ addOrderNonValidArgumentTest
    // -------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void addOrder_NullArgumentTest(){
        // null argument passed
        orderDAOMemory.addOrder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOrder_AlreadyRegisteredOrderTest(){
        // Already registered Order
        orderDAOMemory.addOrder(order1);
        orderDAOMemory.addOrder(order1);
    }

    @Test
    public void getOrders() {
        //Initially Empty ordersRepository
        assertEquals(0, orderDAOMemory.getOrders().size());

        //order1 Order is constructed in the setUp method
        orderDAOMemory.addOrder(order1);
        assertEquals(1, orderDAOMemory.getOrders().size());


        Order order2 = new Order("order1245", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        order2.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop (),2));
        orderDAOMemory.addOrder(order2);
        assertEquals(2, orderDAOMemory.getOrders().size());
    }

    @Test
    public void clear() {
        orderDAOMemory.clear();
        assertEquals(0, orderDAOMemory.getOrders().size());
    }

    @After
    public void tearDownTest(){
        orderDAOMemory.clear();
    }
}