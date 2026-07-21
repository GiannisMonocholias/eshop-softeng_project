package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

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
 * asynchronously and confirming successful delivery, which involves state changes and task list updates.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListPresenterTest {

    private DelivererOrdersListPresenter presenter;
    private DelivererOrdersListViewStub viewStub;
    private Deliverer deliverer;
    private Order shippedOrder;

    private static final String DELIVERER_ID = "DEL-401";
    private static final String ORDER_CODE = "ORD-2023-001";

    /**
     * Sets up the testing environment before each test.
     * Populates memory repositories, injects dependencies, and adds a sample
     * shipped order to the deliverer's list asynchronously.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new DelivererOrdersListViewStub();
        presenter = new DelivererOrdersListPresenter(
                viewStub,
                OrderDAOMemory.getInstance(),
                EmployeeDAOMemory.getInstance()
        );

        deliverer = (Deliverer) EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID).join();

        shippedOrder = OrderDAOMemory.getInstance().getOrder(ORDER_CODE);

        deliverer.addOrder(shippedOrder);
    }

    /**
     * Verifies that the presenter correctly retrieves the list of assigned orders
     * and updates the view when a valid deliverer ID is provided.
     */
    @Test
    public void loadShippedOrdersValidDelivererReturnsOrders() {
        presenter.loadShippedOrders(DELIVERER_ID);
        ArrayList<Order> orders = viewStub.getLoadedOrders();

        Assert.assertNotNull(orders);
        Assert.assertEquals(1, orders.size());
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
     * The order is removed from the deliverer's internal assigned list.
     * The UI is requested to remove the order and display a success message.
     */
    @Test
    public void onOrderConfirmedSetsStatusDeliveredAndRemovesFromList() {
        presenter.loadShippedOrders(DELIVERER_ID);

        Assert.assertEquals(OrderStatusType.SHIPPED, shippedOrder.getOrderstatus());

        presenter.onOrderConfirmed(shippedOrder);

        // State update verification
        Assert.assertEquals(OrderStatusType.DELIVERED, shippedOrder.getOrderstatus());

        // Domain list update verification
        Assert.assertFalse(deliverer.getOrders().contains(shippedOrder));

        // UI update verification via stub
        Assert.assertTrue(viewStub.getMessageShown().contains("ολοκληρώθηκε"));
        Assert.assertEquals(shippedOrder, viewStub.getRemovedOrder());
    }
}