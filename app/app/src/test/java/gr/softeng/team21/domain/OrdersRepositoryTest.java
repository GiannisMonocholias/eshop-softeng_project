package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OrdersRepositoryTest {
    private OrdersRepository ordersRepository;
    private Order order1;

    @Before // Αντικατάσταση του @BeforeEach
    public void setUp(){
        ordersRepository = OrdersRepository.getInstance();
        ordersRepository.clear(); // Καθαρισμός για απομόνωση των tests

        order1 = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        // Υποθέτουμε ότι η TestHelper.getLaptop() επιστρέφει ProductType/WholesaleProduct
        order1.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop(), 2));
    }

    @Test
    public void getInstanceReturnsSameReferences() {
        OrdersRepository ordersRepository2 = OrdersRepository.getInstance();
        assertSame(ordersRepository, ordersRepository2);
    }

    @Test
    public void testGetOrdersInitiallyEmpty() {
        assertTrue(ordersRepository.getOrders().isEmpty());
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ getOrderNonValidArgumentsTest
    // -------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void getOrder_NullArgumentTest() {
        // pass null argument in getOrder()
        ordersRepository.getOrder(null);
    }

    @Test
    public void getOrder_NonExistingOrderTest() {
        // Request a non existing order
        assertNull(ordersRepository.getOrder("order1245"));
    }

    @Test
    public void getOrderTestSuccess(){
        ordersRepository.addOrder(order1);
        Order returnedOrder = ordersRepository.getOrder("order1246");
        assertTrue(ordersRepository.getOrders().containsKey("order1246"));
        assertSame(returnedOrder, ordersRepository.getOrder("order1246"));
    }

    @Test
    public void addOrderTestSuccess() {
        ordersRepository.addOrder(order1);

        assertEquals(order1, ordersRepository.getOrder("order1246"));
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ addOrderNonValidArgumentTest
    // -------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void addOrder_NullArgumentTest(){
        // null argument passed
        ordersRepository.addOrder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOrder_AlreadyRegisteredOrderTest(){
        // Already registered Order
        ordersRepository.addOrder(order1);
        ordersRepository.addOrder(order1);
    }

    @Test
    public void getOrders() {
        //Initially Empty ordersRepository
        assertEquals(0, ordersRepository.getOrders().size());

        //order1 Order is constructed in the setUp method
        ordersRepository.addOrder(order1);
        assertEquals(1, ordersRepository.getOrders().size());


        Order order2 = new Order("order1245", new Date(), StatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        order2.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop (),2));
        ordersRepository.addOrder(order2);
        assertEquals(2, ordersRepository.getOrders().size());
    }

    @Test
    public void clear() {
        ordersRepository.clear();
        assertEquals(0, ordersRepository.getOrders().size());
    }

    @After
    public void tearDownTest(){
        ordersRepository.clear();
    }
}