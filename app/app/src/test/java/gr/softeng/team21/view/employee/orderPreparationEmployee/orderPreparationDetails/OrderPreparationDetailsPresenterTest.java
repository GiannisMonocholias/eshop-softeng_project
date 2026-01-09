package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

/**
 * Unit tests for {@link OrderPreparationDetailsPresenter}.
 * This suite validates the detailed order preparation process, focusing on stock checking
 * logic, status transitions (SHIPPED vs DELAYED), and error handling for unassigned
 * or missing orders.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsPresenterTest {

    private OrderPreparationDetailsPresenter presenter;
    private OrderPreparationDetailsViewStub viewStub;
    private OrderPreparationEmployee prepEmployee;
    private Order order;

    private static final String EMPLOYEE_ID = "PREP-201";
    private static final String ORDER_CODE = "ORD-2023-001";

    /**
     * Initializes data and sets up the presenter and target domain objects before each test.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new OrderPreparationDetailsViewStub();

        presenter = new OrderPreparationDetailsPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                OrderDAOMemory.getInstance()
        );

        prepEmployee = (OrderPreparationEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);
        order = OrderDAOMemory.getInstance().getOrder(ORDER_CODE);

        prepEmployee.addOrder(order);
    }

    /**
     * Verifies that order details are correctly loaded and mapped to the view.
     */
    @Test
    public void loadOrderDisplaysCorrectDetails() {
        ArrayList<CartItem> items = presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);

        Assert.assertEquals(ORDER_CODE, viewStub.getDisplayedOrderCode());
        Assert.assertNotNull(items);
        Assert.assertFalse(items.isEmpty());
    }

    /**
     * Verifies successful stock verification leading to the "SHIPPED" status.
     */
    @Test
    public void checkStockOrderSufficientStock_Success() {
        presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);
        presenter.checkStockOrder();

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));
        Assert.assertEquals(OrderStatusType.SHIPPED, order.getOrderstatus());
    }

    /**
     * Verifies that insufficient stock results in a "DELAYED" status and appropriate error message.
     */
    @Test
    public void checkStockOrderInsufficientStockShowsError() {
        presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);

        ProductsWareHouseDAOMemory warehouse = (ProductsWareHouseDAOMemory) ProductsWareHouseDAOMemory.getInstance();
        warehouse.getProductStocks().put(order.getShoppingCart().getItems().get(0).getProductType(), 0);

        presenter.checkStockOrder();

        Assert.assertTrue(viewStub.getErrorMessage().contains("Ανεπαρκές απόθεμα"));
        Assert.assertEquals(OrderStatusType.DELAYED, order.getOrderstatus());
    }

    /**
     * Verifies error handling when attempting to check stock for a null order.
     */
    @Test
    public void checkStockOrder_NullOrderShowsErrorMessage() {
        presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);

        presenter.setOrderToPrepare(null);

        presenter.checkStockOrder();

        Assert.assertEquals("Σφάλμα: Δεν δόθηκε παραγγελία (null Order pointer)", viewStub.getErrorMessage());
    }

    /**
     * Verifies that an employee cannot prepare an order that is not assigned to them.
     */
    @Test
    public void checkStockOrderOrderNotAssignedShowsErrorMessage() {
        presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);

        prepEmployee.removeOrder(order);

        presenter.checkStockOrder();

        Assert.assertEquals("Σφάλμα: Δεν σας έχει ανατεθεί η συγκεκριμένη παραγγελία", viewStub.getErrorMessage());
    }

    /**
     * Verifies that if no deliverers exist in the system, an {@link IllegalStateException} is thrown
     * during the order shipping phase.
     */
    @Test(expected = IllegalStateException.class)
    public void checkStockOrderNoDeliverersAvailableThrowsIllegalStateException() {
        presenter.loadOrder(EMPLOYEE_ID, ORDER_CODE);

        EmployeeDAOMemory memoryDAO = (EmployeeDAOMemory) EmployeeDAOMemory.getInstance();
        memoryDAO.getEmployees().remove("DEL-401");
        memoryDAO.getEmployees().remove("DEL-402");
        memoryDAO.getEmployees().remove("DEL-403");

        presenter.checkStockOrder();
    }
}