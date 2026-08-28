package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

/**
 * Unit tests for {@link OrderPreparationDetailsPresenter}.
 * Verifies fully asynchronous logic for checking stock concurrently, assigning
 * Foreign Keys (Deliverer or Customer Service), updating Order Status, and dispatching emails safely.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsPresenterTest {

    private OrderPreparationDetailsPresenter presenter;
    private OrderPreparationDetailsViewStub viewStub;
    private OrderDAO orderDAO;
    private EmailDAO emailDAO;

    private static final String PREP_EMP_ID = "PREP-201";
    private static final String ORDER_CODE_OK = "ORD-2024-001"; // Has stock in MemoryInitializer
    private static final String ORDER_CODE_MISSING = "ORD-2024-002"; // Missing stock in MemoryInitializer

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new OrderPreparationDetailsViewStub();

        EmployeeDAO employeeDAO = EmployeeDAOMemory.getInstance();
        orderDAO = OrderDAOMemory.getInstance();
        ProductsWareHouseDAO wareHouseDAO = ProductsWareHouseDAOMemory.getInstance();
        emailDAO = new EmailDAOMemory();

        presenter = new OrderPreparationDetailsPresenter(viewStub, employeeDAO, orderDAO, wareHouseDAO, emailDAO);
    }

    @Test
    public void checkStock_WithSufficientStock_ShipsOrderAndAssignsDeliverer() {
        presenter.loadOrder(PREP_EMP_ID, ORDER_CODE_OK);

        // Execute the async check
        presenter.checkStockOrder();

        // Fetch the updated order from DAO synchronously using join()
        Order processedOrder = orderDAO.getOrder(ORDER_CODE_OK).join();

        Assert.assertEquals(OrderStatusType.SHIPPED, processedOrder.getOrderstatus());
        Assert.assertNotNull(processedOrder.getDelivererId());
        Assert.assertTrue(viewStub.getSuccessMessage().contains("έτοιμη προς παράδοση"));
    }

    @Test
    public void checkStock_WithInsufficientStock_DelaysOrderAndAssignsCS() {
        presenter.loadOrder(PREP_EMP_ID, ORDER_CODE_MISSING);

        // Execute the async check
        presenter.checkStockOrder();

        // Fetch the updated order from DAO synchronously using join()
        Order processedOrder = orderDAO.getOrder(ORDER_CODE_MISSING).join();

        Assert.assertEquals(OrderStatusType.DELAYED, processedOrder.getOrderstatus());
        Assert.assertNotNull(processedOrder.getCustomerServiceId());
        Assert.assertTrue(viewStub.getErrorMessage().contains("Ανεπαρκές απόθεμα"));

        // Verifies the delay email was generated and saved to DAOs
        Assert.assertEquals(1, emailDAO.getSentEmails().join().size());
    }
}