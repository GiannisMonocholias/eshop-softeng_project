package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link AssignedOrdersToPreparePresenter}.
 * Ensures that orders assigned via Foreign Keys to a preparation employee are
 * retrieved correctly via the DAO and that selection triggers the proper navigation flow.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPreparePresenterTest {

    private AssignedOrdersToPreparePresenter presenter;
    private AssignedOrdersToPrepareViewStub viewStub;
    private OrderDAO orderDAO;

    private static final String EMPLOYEE_ID = "PREP-201";

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        viewStub = new AssignedOrdersToPrepareViewStub();
        orderDAO = OrderDAOMemory.getInstance();
        presenter = new AssignedOrdersToPreparePresenter(viewStub, EmployeeDAOMemory.getInstance(), orderDAO);

        // Fetch a test order and assign it to the employee using the new Foreign Key logic
        Order orderToAssign = orderDAO.getOrder("ORD-2024-001").join(); // Ensure this ID exists in MemoryInitializer
        if (orderToAssign != null) {
            orderToAssign.setPreparationEmployeeId(EMPLOYEE_ID);
            orderToAssign.setOrderstatus(OrderStatusType.NEW);
            orderDAO.updateOrder(orderToAssign).join();
        }
    }

    @Test
    public void loadAssignedOrdersReturnsCorrectList() {
        presenter.loadAssignedOrders(EMPLOYEE_ID);
        ArrayList<Order> result = viewStub.getLoadedOrders();

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(EMPLOYEE_ID, result.get(0).getPreparationEmployeeId());
        Assert.assertEquals(OrderStatusType.NEW, result.get(0).getOrderstatus());
    }

    @Test
    public void loadAssignedOrdersInvalidEmployeeShowsError() {
        presenter.loadAssignedOrders("INVALID_ID");
        Assert.assertTrue(viewStub.getErrorMessage().contains("δεν βρέθηκε"));
    }

    @Test
    public void onClickOrderNavigatesToDetails() {
        presenter.loadAssignedOrders(EMPLOYEE_ID);

        Order order = viewStub.getLoadedOrders().get(0);
        presenter.onClickOrder(order);

        Assert.assertTrue(viewStub.isNavigationCalled());
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedEmployeeId());
        Assert.assertEquals(order.getOrdercode(), viewStub.getNavigatedOrderCode());
    }
}