package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link DelivererOrdersListPresenter}.
 * This suite verifies the core workflow of a deliverer: viewing assigned shipped orders
 * asynchronously via the database and confirming successful delivery, validating
 * both domain state (workload counters) and DAO interactions.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListPresenterTest {

    private DelivererOrdersListPresenter presenter;
    private DelivererOrdersListViewStub viewStub;
    private Deliverer deliverer;
    private Order shippedOrder;
    private OrderDAOMemory orderDAO;

    private static final String DELIVERER_ID = "DEL-401";
    private static final String ORDER_CODE = "ORD-2023-001";

    /**
     * Sets up the testing environment before each test.
     * Populates memory repositories, injects dependencies, fetches a sample order,
     * and assigns it to the deliverer using the new Foreign Key logic.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new DelivererOrdersListViewStub();
        orderDAO = OrderDAOMemory.getInstance();
        presenter = new DelivererOrdersListPresenter(
                viewStub,
                orderDAO,
                EmployeeDAOMemory.getInstance()
        );

        // Fetch employee asynchronously
        deliverer = (Deliverer) EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID).join();

        // Fetch order asynchronously
        shippedOrder = orderDAO.getOrder(ORDER_CODE).join();

        // Adapt to new architecture: Set Foreign Key and increment local workload counter
        if (shippedOrder != null) {
            shippedOrder.setDelivererId(DELIVERER_ID);
            shippedOrder.setOrderstatus(OrderStatusType.SHIPPED);
            orderDAO.updateOrder(shippedOrder).join(); // Save explicitly

            deliverer.assignOrder();
        }
    }

    /**
     * Verifies that the presenter correctly retrieves the list of assigned orders
     * from the DAO using the deliverer's ID and updates the view.
     */
    @Test
    public void loadShippedOrdersValidDelivererReturnsOrders() {
        presenter.loadShippedOrders(DELIVERER_ID);
        ArrayList<Order> orders = viewStub.getLoadedOrders();

        Assert.assertNotNull(orders);
        Assert.assertFalse(orders.isEmpty());
        Assert.assertEquals(ORDER_CODE, orders.get(0).getOrdercode());
    }

    /**
     * Verifies that attempting to load orders for an invalid deliverer ID
     * results in an appropriate UI error message.
     */
    @Test
    public void loadShippedOrdersInvalidDeliverer_ShowsError() {
        presenter.loadShippedOrders("INVALID_ID");

        Assert.assertNull(viewStub.getLoadedOrders());
        Assert.assertEquals("Σφάλμα: Ο διανομέας δεν βρέθηκε.", viewStub.getErrorShown());
    }

    /**
     * Verifies the delivery confirmation workflow:
     * The order status transitions from SHIPPED to DELIVERED.
     * The deliverer's internal workload counter is decremented.
     * The UI is requested to remove the order and display a success message.
     */
    @Test
    public void onOrderConfirmedSetsStatusDeliveredAndRemovesFromList() {
        presenter.loadShippedOrders(DELIVERER_ID);
        int initialWorkload = deliverer.getAssignedOrdersCount();

        Assert.assertEquals(OrderStatusType.SHIPPED, shippedOrder.getOrderstatus());

        presenter.onOrderConfirmed(shippedOrder);

        // State update verification
        Assert.assertEquals(OrderStatusType.DELIVERED, shippedOrder.getOrderstatus());

        // Domain counter update verification
        Assert.assertEquals(initialWorkload - 1, deliverer.getAssignedOrdersCount());

        // UI update verification via stub
        Assert.assertTrue(viewStub.getMessageShown().contains("ολοκληρώθηκε"));
        Assert.assertEquals(shippedOrder, viewStub.getRemovedOrder());
    }

    /**
     * Clears shared memory states after tests to ensure isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        OrderDAOMemory.getInstance().clear().join();
    }
}