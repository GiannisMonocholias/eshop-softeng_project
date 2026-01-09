package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.TestHelper;

/**
 * Unit tests for the {@link OrderDAOMemory} class.
 * This suite verifies the in-memory management of customer orders, ensuring
 * that the repository correctly handles the storage, retrieval, and
 * uniqueness of order entities.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOMemoryTest {
    private OrderDAOMemory orderDAOMemory;
    private Order order1;

    /**
     * Initializes the testing environment before each test.
     * Clears the singleton repository and prepares a sample order with
     * items in its shopping cart to be used in assertions.
     */
    @Before // Αντικατάσταση του @BeforeEach
    public void setUp(){
        orderDAOMemory = OrderDAOMemory.getInstance();
        orderDAOMemory.clear(); // Καθαρισμός για απομόνωση των tests

        order1 = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        // Υποθέτουμε ότι η TestHelper.getLaptop() επιστρέφει ProductType/WholesaleProduct
        order1.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop(), 2));
    }

    /**
     * Verifies that {@link OrderDAOMemory} implements the Singleton pattern
     * correctly by providing the same instance reference across multiple calls.
     */
    @Test
    public void getInstanceReturnsSameReferences() {
        OrderDAOMemory orderDAOMemory2 = OrderDAOMemory.getInstance();
        assertSame(orderDAOMemory, orderDAOMemory2);
    }

    /**
     * Verifies that the order repository starts empty after a clear operation.
     */
    @Test
    public void testGetOrdersInitiallyEmpty() {
        assertTrue(orderDAOMemory.getOrders().isEmpty());
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ getOrderNonValidArgumentsTest
    // -------------------------------------------------------------------

    /**
     * Verifies that providing a null argument to {@code getOrder}
     * throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getOrder_NullArgumentTest() {
        // pass null argument in getOrder()
        orderDAOMemory.getOrder(null);
    }

    /**
     * Verifies that requesting an order ID that does not exist
     * correctly returns null.
     */
    @Test
    public void getOrder_NonExistingOrderTest() {
        // Request a non existing order
        assertNull(orderDAOMemory.getOrder("order1245"));
    }

    /**
     * Tests the successful retrieval of an existing order by its unique order code.
     */
    @Test
    public void getOrderTestSuccess(){
        orderDAOMemory.addOrder(order1);
        Order returnedOrder = orderDAOMemory.getOrder("order1246");
        assertTrue(orderDAOMemory.getOrders().containsKey("order1246"));
        assertSame(returnedOrder, orderDAOMemory.getOrder("order1246"));
    }

    /**
     * Tests that a newly added order can be successfully recovered from the repository.
     */
    @Test
    public void addOrderTestSuccess() {
        orderDAOMemory.addOrder(order1);

        assertEquals(order1, orderDAOMemory.getOrder("order1246"));
    }

    // -------------------------------------------------------------------
    // ΔΙΑΧΩΡΙΣΜΟΣ addOrderNonValidArgumentTest
    // -------------------------------------------------------------------

    /**
     * Verifies that attempting to add a null order to the repository
     * throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addOrder_NullArgumentTest(){
        // null argument passed
        orderDAOMemory.addOrder(null);
    }

    /**
     * Verifies that adding an order with an ID that is already registered
     * throws an {@link IllegalArgumentException} to prevent data duplication.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addOrder_AlreadyRegisteredOrderTest(){
        // Already registered Order
        orderDAOMemory.addOrder(order1);
        orderDAOMemory.addOrder(order1);
    }

    /**
     * Verifies the growth and sizing of the orders repository as
     * new orders are added.
     */
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

    /**
     * Verifies that the {@code clear} method successfully removes all
     * orders from the memory storage.
     */
    @Test
    public void clear() {
        orderDAOMemory.clear();
        assertEquals(0, orderDAOMemory.getOrders().size());
    }

    /**
     * Ensures that the repository is reset after each test case execution
     * to maintain isolation between tests.
     */
    @After
    public void tearDownTest(){
        orderDAOMemory.clear();
    }
}