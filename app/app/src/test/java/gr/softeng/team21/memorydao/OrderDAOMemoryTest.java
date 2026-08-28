package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
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
 * that the repository correctly handles storage, retrieval, Foreign Key queries, and
 * updates using CompletableFuture.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOMemoryTest {
    private OrderDAOMemory orderDAOMemory;
    private Order order1;

    @Before
    public void setUp(){
        orderDAOMemory = OrderDAOMemory.getInstance();
        orderDAOMemory.clear().join();

        order1 = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH,new Date () ,new ShoppingCart());
        order1.getShoppingCart().addItem(new CartItem(TestHelper.getLaptop(), 2));
    }

    @Test
    public void getInstanceReturnsSameReferences() {
        OrderDAOMemory orderDAOMemory2 = OrderDAOMemory.getInstance();
        assertSame(orderDAOMemory, orderDAOMemory2);
    }

    @Test
    public void testGetOrdersInitiallyEmpty() {
        assertTrue(orderDAOMemory.getOrders().join().isEmpty());
    }

    @Test(expected = CompletionException.class)
    public void getOrder_NullArgumentTest() {
        orderDAOMemory.getOrder(null).join();
    }

    @Test
    public void getOrder_NonExistingOrderTest() {
        assertNull(orderDAOMemory.getOrder("order1245").join());
    }

    @Test
    public void addOrderTestSuccess() {
        orderDAOMemory.addOrder(order1).join();
        assertEquals(order1, orderDAOMemory.getOrder("order1246").join());
    }

    @Test(expected = CompletionException.class)
    public void addOrder_AlreadyRegisteredOrderTest(){
        orderDAOMemory.addOrder(order1).join();
        orderDAOMemory.addOrder(order1).join(); // This will fail
    }

    @Test
    public void updateOrderTestSuccess() {
        orderDAOMemory.addOrder(order1).join();
        order1.setOrderstatus(OrderStatusType.SHIPPED);
        orderDAOMemory.updateOrder(order1).join(); // Overwrites without error

        Order fetched = orderDAOMemory.getOrder("order1246").join();
        assertEquals(OrderStatusType.SHIPPED, fetched.getOrderstatus());
    }

    @Test
    public void getOrdersByDelivererIdTest() {
        order1.setDelivererId("DEL-999");
        orderDAOMemory.addOrder(order1).join();

        ArrayList<Order> assigned = orderDAOMemory.getOrdersByDelivererId("DEL-999").join();
        assertEquals(1, assigned.size());
        assertEquals("DEL-999", assigned.get(0).getDelivererId());
    }

    @Test
    public void getOrdersByPreparationEmployeeIdTest() {
        order1.setPreparationEmployeeId("PREP-123");
        orderDAOMemory.addOrder(order1).join();

        ArrayList<Order> assigned = orderDAOMemory.getOrdersByPreparationEmployeeId("PREP-123").join();
        assertEquals(1, assigned.size());
        assertEquals("PREP-123", assigned.get(0).getPreparationEmployeeId());
    }

    @After
    public void tearDownTest(){
        orderDAOMemory.clear().join();
    }
}