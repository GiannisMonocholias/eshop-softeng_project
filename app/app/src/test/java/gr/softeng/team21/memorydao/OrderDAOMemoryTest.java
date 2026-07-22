package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CompletionException;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.TestHelper;

/**
 * Unit tests for the {@link OrderDAOMemory} class.
 * This suite verifies the asynchronous in-memory management of customer orders, ensuring
 * that the repository correctly handles the storage, retrieval, and
 * uniqueness of order entities using CompletableFuture.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOMemoryTest {
    private OrderDAOMemory orderDAOMemory;
    private Order order1;

    /**
     * Initializes the testing environment before each test.
     * Clears the singleton repository asynchronously and prepares a sample order with
     * items in its shopping cart to be used in assertions.
     */
    @Before
    public void setUp(){
        orderDAOMemory = OrderDAOMemory.getInstance();
        orderDAOMemory.clear().join();

        order1 = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
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
        assertTrue(orderDAOMemory.getOrders().join().isEmpty());
    }

    /**
     * Verifies that providing a null argument to {@code getOrder}
     * throws an IllegalArgumentException wrapped in a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void getOrder_NullArgumentTest() {
        orderDAOMemory.getOrder(null).join();
    }

    /**
     * Verifies that requesting an order ID that does not exist
     * correctly returns null asynchronously.
     */
    @Test
    public void getOrder_NonExistingOrderTest() {
        assertNull(orderDAOMemory.getOrder("order1245").join());
    }

    /**
     * Tests the successful retrieval of an existing order by its unique order code.
     */
    @Test
    public void getOrderTestSuccess(){
        orderDAOMemory.addOrder(order1).join();
        Order returnedOrder = orderDAOMemory.getOrder("order1246").join();
        assertTrue(orderDAOMemory.getOrders().join().containsKey("order1246"));
        assertSame(returnedOrder, orderDAOMemory.getOrder("order1246").join());
    }

    /**
     * Tests that a newly added order can be successfully recovered from the repository.
     */
    @Test
    public void addOrderTestSuccess() {
        orderDAOMemory.addOrder(order1).join();
        assertEquals(order1, orderDAOMemory.getOrder("order1246").join());
    }

    /**
     * Verifies that attempting to add a null order to the repository
     * throws an exception wrapped in a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void addOrder_NullArgumentTest(){
        orderDAOMemory.addOrder(null).join();
    }

    /**
     * Verifies that adding an order with an ID that is already registered
     * throws an exception wrapped in a {@link CompletionException} to prevent data duplication.
     */
    @Test(expected = CompletionException.class)
    public void addOrder_AlreadyRegisteredOrderTest(){
        orderDAOMemory.addOrder(order1).join();
        orderDAOMemory.addOrder(order1).join(); // This will fail
    }

    /**
     * Verifies the growth and sizing of the orders repository as
     * new orders are added.
     */
    @Test
    public void getOrders() {
        assertEquals(0, orderDAOMemory.getOrders().join().size());

        orderDAOMemory.addOrder(order1).join();
        assertEquals(1, orderDAOMemory.getOrders().join().size());

        Order order2 = new Order("order1245", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        order2.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop (),2));

        orderDAOMemory.addOrder(order2).join();
        assertEquals(2, orderDAOMemory.getOrders().join().size());
    }

    /**
     * Verifies that the {@code clear} method successfully removes all
     * orders from the memory storage asynchronously.
     */
    @Test
    public void clear() {
        orderDAOMemory.addOrder(order1).join();
        orderDAOMemory.clear().join();
        assertEquals(0, orderDAOMemory.getOrders().join().size());
    }

    /**
     * Ensures that the repository is reset after each test case execution
     * to maintain isolation between tests.
     */
    @After
    public void tearDownTest(){
        orderDAOMemory.clear().join();
    }
}