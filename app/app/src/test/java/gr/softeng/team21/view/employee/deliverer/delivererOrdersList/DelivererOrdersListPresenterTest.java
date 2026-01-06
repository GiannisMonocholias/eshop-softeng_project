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

public class DelivererOrdersListPresenterTest {

    private DelivererOrdersListPresenter presenter;
    private DelivererOrdersListViewStub viewStub;
    private Deliverer deliverer;
    private Order shippedOrder;

    private static final String DELIVERER_ID = "DEL-401";
    private static final String ORDER_CODE = "ORD-2023-001";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new DelivererOrdersListViewStub();
        presenter = new DelivererOrdersListPresenter(
                viewStub,
                OrderDAOMemory.getInstance(),
                EmployeeDAOMemory.getInstance()
        );

        deliverer = (Deliverer) EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID);
        shippedOrder = OrderDAOMemory.getInstance().getOrder(ORDER_CODE);

        deliverer.addOrder(shippedOrder);
    }


    @Test
    public void loadShippedOrdersValidDelivererReturnsOrders() {
        ArrayList<Order> orders = presenter.loadShippedOrders(DELIVERER_ID);

        Assert.assertNotNull(orders);
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals(ORDER_CODE, orders.get(0).getOrdercode());
    }

    @Test
    public void loadShippedOrdersInvalidDeliverer_ShowsError() {
        ArrayList<Order> orders = presenter.loadShippedOrders("INVALID_ID");

        Assert.assertNull(orders);
        Assert.assertEquals("Σφάλμα: Ο διανομέας δεν βρέθηκε.", viewStub.getErrorShown());
    }

    @Test
    public void onOrderConfirmedSetsStatusDeliveredAndRemovesFromList() {
        presenter.loadShippedOrders(DELIVERER_ID);

        Assert.assertEquals(OrderStatusType.SHIPPED, shippedOrder.getOrderstatus());

        presenter.onOrderConfirmed(shippedOrder);


        Assert.assertEquals(OrderStatusType.DELIVERED, shippedOrder.getOrderstatus());


        Assert.assertFalse(deliverer.getOrders().contains(shippedOrder));


        Assert.assertTrue(viewStub.getMessageShown().contains("ολοκληρώθηκε"));

        Assert.assertEquals(shippedOrder, viewStub.getRemovedOrder());
    }
}