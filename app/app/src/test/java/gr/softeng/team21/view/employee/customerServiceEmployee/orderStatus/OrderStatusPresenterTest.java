package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

public class OrderStatusPresenterTest {

    private OrderStatusPresenter presenter;
    private OrderStatusViewStub viewStub;
    private CustomerServiceEmployee csr1;
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String WRONG_TYPE_EMPLOYEE_ID = "PREP-201";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new OrderStatusViewStub();
        presenter = new OrderStatusPresenter(viewStub, EmployeeDAOMemory.getInstance());
        csr1 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);
    }


    @Test
    public void loadOrdersReturnsCorrectList() {
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");
        csr1.addOrder(delayedOrder);

        ArrayList<Order> orders = presenter.loadOrders(EMPLOYEE_ID);

        Assert.assertEquals(1, orders.size());
        Assert.assertEquals("ORD-2024-004", orders.get(0).getOrdercode());
    }

    @Test
    public void loadOrdersNonExistingEmployeeId() {
        presenter.loadOrders("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Δεν βρέθηκε ID υπαλλήλου.", viewStub.getErrorMsg());
    }

    @Test
    public void loadOrdersWrongEmployeeType() {
        presenter.loadOrders(WRONG_TYPE_EMPLOYEE_ID);
        Assert.assertTrue(viewStub.getErrorMsg().contains("δεν ανήκει στην εξυπηρέτηση πελατών"));
    }


    @Test
    public void onOrderClickedDelayedOrderShowsConfirmation() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order delayedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-004");

        presenter.onOrderClicked(delayedOrder);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("ΚΑΘΥΣΤΕΡΗΣΗΣ"));
    }

    @Test
    public void onOrderClickedShippedOrderShowsConfirmation() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order shippedOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-003");

        presenter.onOrderClicked(shippedOrder);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("ΕΤΟΙΜΟΤΗΤΑΣ"));
    }

    @Test
    public void onOrderClickedOtherStatusShowsSimpleToast() {
        presenter.loadOrders(EMPLOYEE_ID);
        Order newOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-002");

        presenter.onOrderClicked(newOrder);

        Assert.assertFalse(viewStub.isConfirmationDialogShown());
        Assert.assertEquals("ORD-2024-002", viewStub.getSelectedOrderCode());
    }

    @Test
    public void onOrderClickedNoLoggedInEmployee() {
        Order anyOrder = OrderDAOMemory.getInstance().getOrder("ORD-2024-002");

        presenter.onOrderClicked(anyOrder);

        Assert.assertEquals("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος", viewStub.getErrorMsg());
    }


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