package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link OrderStatusPresenter}.
 * Verifies the logic for handling order notifications, including asynchronous loading of orders,
 * triggering role-specific confirmation dialogs, and verifying email persistence using DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenterTest {

    private OrderStatusPresenter presenter;
    private OrderStatusViewStub viewStub;
    private EmailDAO emailDAO;
    private CustomerServiceEmployee csr1;

    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String WRONG_TYPE_EMPLOYEE_ID = "PREP-201";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data asynchronously, injects dependencies including EmailDAO,
     * and instantiates the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new OrderStatusViewStub();
        emailDAO = new EmailDAOMemory();
        presenter = new OrderStatusPresenter(viewStub, EmployeeDAOMemory.getInstance(), emailDAO);

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
     * Verifies that interaction fails if no valid employee session is established.
     */
    @Test
    public void onOrderClickedNoLoggedInEmployee() {
        Order anyOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-002");
        presenter.onOrderClicked(anyOrder);
        Assert.assertEquals("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος", viewStub.getErrorMsg());
    }

    /**
     * Verifies the full confirmation workflow for a delayed order:
     * 1. The async DAO actually saves the email in Sent & Inbox.
     * 2. A success message is shown and the UI list is updated.
     * 3. The order is removed from the pending list.
     */
    @Test
    public void onOrderConfirmedDelayedSendsEmailAndUpdatesList() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");
        csr1.addOrder(delayedOrder);

        presenter.onOrderConfirmed(delayedOrder);

        // Verify async DB writes
        Assert.assertEquals(1, emailDAO.getSentEmails().join().size());
        Assert.assertEquals(1, emailDAO.getInboxEmails().join().size());

        // Verify UI updates
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

        // Verify async DB writes
        Assert.assertEquals(1, emailDAO.getSentEmails().join().size());
        Assert.assertEquals(1, emailDAO.getInboxEmails().join().size());

        // Verify UI updates
        Assert.assertTrue(viewStub.getMessageMsg().contains("ετοιμότητας"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertFalse(csr1.getOrders().contains(shippedOrder));
    }

    /**
     * Clears shared memory states after tests to ensure isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
    }
}