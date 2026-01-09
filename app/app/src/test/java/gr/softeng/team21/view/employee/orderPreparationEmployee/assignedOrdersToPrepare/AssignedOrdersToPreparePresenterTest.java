package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link AssignedOrdersToPreparePresenter}.
 * This suite ensures that the list of orders already assigned to a preparation
 * employee is retrieved correctly and that selecting an order leads to the proper
 * navigation flow.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPreparePresenterTest {

    private AssignedOrdersToPreparePresenter presenter;
    private AssignedOrdersToPrepareViewStub viewStub;
    private OrderPreparationEmployee prepEmployee;

    private static final String EMPLOYEE_ID = "PREP-201";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data, sets up the presenter, and ensures at least one order
     * is assigned to the test employee.
     */
    @Before
    public void setUp(){
        MemoryInitializer.prepareData();

        viewStub = new AssignedOrdersToPrepareViewStub();
        presenter = new AssignedOrdersToPreparePresenter(viewStub, EmployeeDAOMemory.getInstance());

        prepEmployee = (OrderPreparationEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        Order orderToAssign = OrderDAOMemory.getInstance().getOrders().get("ORD-2023-001");

        prepEmployee.addOrder(orderToAssign);
    }

    /**
     * Verifies that the presenter correctly retrieves the list of assigned orders
     * for the given employee and matches the expected domain data.
     */
    @Test
    public void loadAssignedOrdersReturnsCorrectList() {
        ArrayList<Order> result = presenter.loadAssignedOrders(EMPLOYEE_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ORD-2023-001", result.get(0).getOrdercode());

        Assert.assertEquals(prepEmployee.getAssignedOrders(), result);
    }

    /**
     * Verifies that clicking on an assigned order correctly triggers the navigation
     * to the details view with the required employee and order identifiers.
     */
    @Test
    public void onClickOrderNavigatesToDetails() {
        presenter.loadAssignedOrders(EMPLOYEE_ID);

        Order order = prepEmployee.getAssignedOrders().get(0);

        presenter.onClickOrder(order);

        Assert.assertTrue(viewStub.isNavigationCalled());
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedEmployeeId());
        Assert.assertEquals("ORD-2023-001", viewStub.getNavigatedOrderCode());
    }
}