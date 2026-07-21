package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link OrderStatusPresenter}.
 * Verifies the logic for handling order notifications, including asynchronous loading of orders,
 * triggering role-specific confirmation dialogs, and processing email notifications
 * for delayed or ready orders using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenterTest {

    private OrderStatusPresenter presenter;
    private OrderStatusViewStub viewStub;
    private CustomerServiceEmployee csr1;
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String WRONG_TYPE_EMPLOYEE_ID = "PREP-201";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data asynchronously, sets up dependencies, and instantiates the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new OrderStatusViewStub();
        presenter = new OrderStatusPresenter(viewStub, EmployeeDAOMemory.getInstance());
        csr1 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID).join();
    }

    /**
     * Verifies that the presenter correctly retrieves the list of orders
     * assigned to a specific Customer Service Employee asynchronously.
     */
    @Test
    public void loadOrdersReturnsCorrectList() {
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");
        csr1.addOrder(delayedOrder);

        presenter.loadOrders(EMPLOYEE_ID);
        ArrayList<Order> orders = viewStub.getLoadedOrders();

        Assert.assertNotNull(orders);
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals("ORD-2024-004", orders.get(0).getOrdercode());
    }

    /**
     * Verifies that an error is shown if the employee ID provided does not exist.
     */
    @Test
    public void loadOrdersNonExistingEmployeeId() {
        presenter.loadOrders("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Δεν βρέθηκε ID υπαλλήλου.", viewStub.getErrorMsg());
    }

    /**
     * Verifies that an error is shown if an employee of a different role
     * attempts to access this view.
     */
    @Test
    public void loadOrdersWrongEmployeeType() {
        presenter.loadOrders(WRONG_TYPE_EMPLOYEE_ID);
        Assert.assertTrue(viewStub.getErrorMsg().contains("δεν ανήκει στην εξυπηρέτηση πελατών"));
    }

    /**
     * Verifies that clicking on a "DELAYED" order triggers a specific
     * confirmation dialog for delay notification.
     */
    @Test
    public void onOrderClickedDelayedOrderShowsConfirmation() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");

        presenter.onOrderClicked(delayedOrder);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("ΚΑΘΥΣΤΕΡΗΣΗΣ"));
    }

    /**
     * Verifies that clicking on a "SHIPPED" order triggers a specific
     * confirmation dialog for readiness notification.
     */
    @Test
    public void onOrderClickedShippedOrderShowsConfirmation() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order shippedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-003");

        presenter.onOrderClicked(shippedOrder);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("ΕΤΟΙΜΟΤΗΤΑΣ"));
    }

    /**
     * Verifies that orders with other statuses do not show a confirmation dialog
     * but simply record the selection.
     */
    @Test
    public void onOrderClickedOtherStatusShowsSimpleToast() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order newOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-002");

        presenter.onOrderClicked(newOrder);

        Assert.assertFalse(viewStub.isConfirmationDialogShown());
        Assert.assertEquals("ORD-2024-002", viewStub.getSelectedOrderCode());
    }

    /**
     * Verifies that interaction fails if no valid employee session is established
     * (e.g., if loadOrders was never called successfully).
     */
    @Test
    public void onOrderClickedNoLoggedInEmployee() {
        Order anyOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-002");

        presenter.onOrderClicked(anyOrder);

        Assert.assertEquals("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος", viewStub.getErrorMsg());
    }

    /**
     * Verifies the full confirmation workflow for a delayed order:
     * 1. A notification message is shown.
     * 2. The UI list is refreshed.
     * 3. The order is removed from the employee's pending list.
     */
    @Test
    public void onOrderConfirmedDelayedSendsEmailAndUpdatesList() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");
        csr1.addOrder(delayedOrder);

        presenter.onOrderConfirmed(delayedOrder);

        Assert.assertTrue(viewStub.getMessageMsg().contains("καθυστέρησης"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertFalse(csr1.getOrders().contains(delayedOrder));
    }

    /**
     * Verifies the full confirmation workflow for a ready (shipped) order.
     */
    @Test
    public void onOrderConfirmedShippedSendsEmailAndUpdatesList() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order shippedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-003");
        csr1.addOrder(shippedOrder);

        presenter.onOrderConfirmed(shippedOrder);

        Assert.assertTrue(viewStub.getMessageMsg().contains("ετοιμότητας"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertFalse(csr1.getOrders().contains(shippedOrder));
    }
}