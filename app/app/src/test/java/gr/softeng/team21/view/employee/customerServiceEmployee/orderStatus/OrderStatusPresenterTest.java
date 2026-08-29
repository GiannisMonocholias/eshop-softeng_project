package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for {@link OrderStatusPresenter}.
 * Verifies the logic for handling order notifications, testing asynchronous loading
 * using Foreign Keys from the OrderDAO and verifying email persistence using EmailDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenterTest {

    private OrderStatusPresenter presenter;
    private OrderStatusViewStub viewStub;
    private EmailDAO emailDAO;
    private OrderDAO orderDAO;

    private static final String EMPLOYEE_ID = "CSR-101";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new OrderStatusViewStub();
        emailDAO = new EmailDAOMemory();
        orderDAO = OrderDAOMemory.getInstance();

        presenter = new OrderStatusPresenter(viewStub, EmployeeDAOMemory.getInstance(), orderDAO, emailDAO);
    }

    @Test
    public void loadOrdersReturnsCorrectList() {
        Order delayedOrder = orderDAO.getOrder("ORD-2024-004").join();
        delayedOrder.setCustomerServiceId(EMPLOYEE_ID);
        orderDAO.updateOrder(delayedOrder).join();

        presenter.loadOrders(EMPLOYEE_ID);
        ArrayList<Order> orders = viewStub.getLoadedOrders();

        Assert.assertNotNull(orders);
        Assert.assertFalse(orders.isEmpty());
        Assert.assertEquals("ORD-2024-004", orders.get(0).getOrdercode());
    }

    @Test
    public void onOrderConfirmedDelayedSendsEmailAndUpdatesList() {
        Order delayedOrder = orderDAO.getOrder("ORD-2024-004").join();
        delayedOrder.setCustomerServiceId(EMPLOYEE_ID);
        orderDAO.updateOrder(delayedOrder).join();
        presenter.loadOrders(EMPLOYEE_ID);

        presenter.onOrderConfirmed(delayedOrder);

        // Verify async DB writes
        Assert.assertEquals(1, emailDAO.getSentEmails().join().size());

        // Verify Order is unassigned from CSR
        Order updatedOrder = orderDAO.getOrder("ORD-2024-004").join();
        Assert.assertNull(updatedOrder.getCustomerServiceId());

        Assert.assertTrue(viewStub.getMessageMsg().contains("καθυστέρησης"));
    }

    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        OrderDAOMemory.getInstance().clear().join();
    }
}