package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

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

/**
 * Unit tests for {@link AvailableOrdersToAssignPresenter}.
 * This suite verifies the logic for filtering available orders asynchronously, displaying
 * confirmation prompts, and the atomic process of assigning an order to an employee
 * using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignPresenterTest {

    private AvailableOrdersToAssignPresenter presenter;
    private AvailableOrdersToAssignViewStub viewStub;
    private OrderPreparationEmployee prepEmployee;

    private static final String EMPLOYEE_ID = "PREP-201";

    /**
     * Prepares memory repositories asynchronously, injects dependencies,
     * and initializes the presenter before each test case.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AvailableOrdersToAssignViewStub();
        presenter = new AvailableOrdersToAssignPresenter(viewStub, EmployeeDAOMemory.getInstance(),
                OrderDAOMemory.getInstance());

        // Using .join() since the DAOs now return CompletableFuture
        prepEmployee = (OrderPreparationEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID).join();
    }

    /**
     * Verifies that only orders with the status "NEW" are retrieved for assignment,
     * ensuring employees don't see orders already in process or shipped.
     */
    @Test
    public void loadAvailableOrdersReturnsOnlyNewOrders() {
        presenter.loadAvailableOrders(EMPLOYEE_ID);
        ArrayList<Order> result = viewStub.getLoadedOrders();

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ORD-2024-002", result.get(0).getOrdercode());
        Assert.assertEquals(OrderStatusType.NEW, result.get(0).getOrderstatus());
    }

    /**
     * Verifies that clicking an available order triggers a confirmation dialog
     * with the correct order details and message.
     */
    @Test
    public void onOrderClickedShowsConfirmationDialog() {
        // Load orders first to initialize loggedInEmployee within the presenter
        presenter.loadAvailableOrders(EMPLOYEE_ID);

        Order order = OrderDAOMemory.getInstance().getOrder("ORD-2024-002").join();

        presenter.onOrderClicked(order);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals(order, viewStub.getLastInteractedOrder());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("Θέλετε να αναλάβετε αυτή την παραγγελία;"));
    }

    /**
     * Verifies the full asynchronous order assignment workflow:
     * 1. Order status changes from NEW to PROCESSING.
     * 2. The order is added to the employee's assigned list.
     * 3. The UI receives a success message and refreshes the list.
     */
    @Test
    public void onOrderConfirmedAssignsOrderAndUpdatesStatus() {
        presenter.loadAvailableOrders(EMPLOYEE_ID);

        Order orderToAssign = OrderDAOMemory.getInstance().getOrder("ORD-2024-002").join();
        Assert.assertEquals(OrderStatusType.NEW, orderToAssign.getOrderstatus());

        presenter.onOrderConfirmed(orderToAssign);

        // Domain state verification
        Assert.assertEquals(OrderStatusType.PROCESSING, orderToAssign.getOrderstatus());
        Assert.assertTrue(prepEmployee.getAssignedOrders().contains(orderToAssign));

        // View feedback verification via stub
        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertEquals(orderToAssign, viewStub.getRemovedOrder());
    }
}