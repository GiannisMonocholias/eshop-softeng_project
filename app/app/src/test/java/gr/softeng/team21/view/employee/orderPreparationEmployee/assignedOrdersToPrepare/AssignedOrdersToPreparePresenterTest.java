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

public class AssignedOrdersToPreparePresenterTest {

    private AssignedOrdersToPreparePresenter presenter;
    private AssignedOrdersToPrepareViewStub viewStub;
    private OrderPreparationEmployee prepEmployee;

    private static final String EMPLOYEE_ID = "PREP-201";

    @Before
    public void setUp(){
        MemoryInitializer.prepareData();

        viewStub = new AssignedOrdersToPrepareViewStub();
        presenter = new AssignedOrdersToPreparePresenter(viewStub, EmployeeDAOMemory.getInstance());

        prepEmployee = (OrderPreparationEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        Order orderToAssign = OrderDAOMemory.getInstance().getOrders().get("ORD-2023-001");

        prepEmployee.addOrder(orderToAssign);
    }


    @Test
    public void loadAssignedOrdersReturnsCorrectList() {
        ArrayList<Order> result = presenter.loadAssignedOrders(EMPLOYEE_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ORD-2023-001", result.get(0).getOrdercode());

        Assert.assertEquals(prepEmployee.getAssignedOrders(), result);
    }

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