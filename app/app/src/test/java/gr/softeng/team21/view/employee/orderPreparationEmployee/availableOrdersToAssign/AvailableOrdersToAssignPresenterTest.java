package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

public class AvailableOrdersToAssignPresenterTest {

    private AvailableOrdersToAssignPresenter presenter;
    private AvailableOrdersToAssignViewStub viewStub;
    private OrderPreparationEmployee prepEmployee;

    private static final String EMPLOYEE_ID = "PREP-201";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AvailableOrdersToAssignViewStub();
        presenter = new AvailableOrdersToAssignPresenter(viewStub, EmployeeDAOMemory.getInstance(),
                OrderDAOMemory.getInstance());

        prepEmployee = (OrderPreparationEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);
    }


    @Test
    public void loadAvailableOrdersReturnsOnlyNewOrders() {

        ArrayList<Order> result = presenter.loadAvailableOrders(EMPLOYEE_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ORD-2024-002", result.get(0).getOrdercode());
        Assert.assertEquals(OrderStatusType.NEW, result.get(0).getOrderstatus());
    }

    @Test
    public void onOrderClickedShowsConfirmationDialog() {
        Order order = OrderDAOMemory.getInstance().getOrders().get("ORD-2024-002");

        presenter.onOrderClicked(order);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals(order, viewStub.getLastInteractedOrder());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("Θέλετε να αναλάβετε αυτή την παραγγελία;"));
    }

    @Test
    public void onOrderConfirmedAssignsOrderAndUpdatesStatus() {

        presenter.loadAvailableOrders(EMPLOYEE_ID);

        Order orderToAssign = OrderDAOMemory.getInstance().getOrders().get("ORD-2024-002");
        Assert.assertEquals(OrderStatusType.NEW, orderToAssign.getOrderstatus());

        presenter.onOrderConfirmed(orderToAssign);


        Assert.assertEquals(OrderStatusType.PROCESSING, orderToAssign.getOrderstatus());
        Assert.assertTrue(prepEmployee.getAssignedOrders().contains(orderToAssign));

        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isListUpdated());

        Assert.assertEquals(orderToAssign, viewStub.getRemovedOrder());
    }
}