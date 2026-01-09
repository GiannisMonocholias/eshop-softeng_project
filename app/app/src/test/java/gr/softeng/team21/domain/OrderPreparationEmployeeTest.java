package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link OrderPreparationEmployee} class.
 * This suite validates the order fulfillment workflow, including order selection,
 * stock verification, communication with other roles (Deliverers, Admins, Customer Service),
 * and handling of insufficient stock scenarios.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployeeTest {
    private String orderId;
    private OrderPreparationEmployee employee;
    private CustomerServiceEmployee customerServiceEmployee;
    private Deliverer deliverer;
    private OrderDAOMemory orderDAOMemory;

    /**
     * Initializes the testing environment before each test case.
     * Clears all repositories (Orders, Employees, Warehouse) and sets up
     * the necessary personnel (Preparation Employee, Deliverer, Customer Service)
     * and sample orders.
     */
    @Before
    public void setUp(){
        OrderDAOMemory.getInstance().clear();
        EmployeeDAOMemory.getInstance().clear();
        ProductsWareHouseDAOMemory.getInstance().clear();
        ProductTypeDAOMemory.getInstance().clear();

        Admin.getInstance();
        Admin.getInstance().setEmailProvider(new EmailDAOMemory());


        employee = new OrderPreparationEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"OPE_1",100,1000,
                8,EmployeeState.ACTIVE, new Date(3,5,2025));
        EmployeeDAOMemory.getInstance().addEmployee(employee);
        employee.setEmailProvider(new EmailDAOMemory());

        orderDAOMemory = OrderDAOMemory.getInstance();
        orderDAOMemory.addOrder(new Order("order1245", new Date(20,5,2025), OrderStatusType.NEW,false,PaymentType.CASH, new Date(),new ShoppingCart()));

        deliverer =new Deliverer("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"DEL_1",100,1000,8,
                EmployeeState.ACTIVE, new Date(3,5,2025),100 , true);
        EmployeeDAOMemory.getInstance().addEmployee(deliverer);

        customerServiceEmployee = new CustomerServiceEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"CSE_1",100,1000,
                8,EmployeeState.ACTIVE, new Date(3,5,2025));
        customerServiceEmployee.setEmailProvider(new EmailDAOMemory());
        EmployeeDAOMemory.getInstance().addEmployee(customerServiceEmployee);


    }

    /**
     * Verifies that selecting an order that does not exist in the repository
     * results in a {@link NoSuchElementException}.
     */
    @Test(expected = NoSuchElementException.class)
    public void selectNonExistingOrder() {
        employee.selectOrder("order1246");
    }

    /**
     * Tests the successful selection of an existing order and its assignment
     * to the employee's pending task list.
     */
    @Test
    public void selectOrderTest(){
        String orderCode = "order1245";
        employee.selectOrder(orderCode);

        Order selectedOrder = orderDAOMemory.getOrder(orderCode);

        assertTrue(employee.getAssignedOrders().contains(selectedOrder));
    }


    /**
     * Verifies that attempting to prepare an order that exists but has not been
     * assigned to the specific employee throws a {@link NoSuchElementException}.
     */
    @Test(expected = NoSuchElementException.class)
    public void prepareOrder_NonAssignedExistingOrderTest() {
        // Existing but not assigned order
        Order nonAssignedOrder1 = OrderDAOMemory.getInstance().getOrder("order1245");
        employee.prepareOrder(nonAssignedOrder1);
    }

    /**
     * Verifies that attempting to prepare an order that does not exist
     * in the system throws a {@link NoSuchElementException}.
     */
    @Test(expected = NoSuchElementException.class)
    public void prepareOrder_NonExistingOrderTest() {
        // Non-existing order
        Order nonAssignedOrder2 = new Order("order1246", new Date(20,5,2025), OrderStatusType.NEW,false,PaymentType.CASH, new Date(), new ShoppingCart());
        employee.prepareOrder(nonAssignedOrder2);
    }

    /**
     * Verifies that the prepareOrder method throws {@link IllegalArgumentException}
     * when passed a null parameter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void prepareOrder_NullArgumentTest() {
        // Null argument
        employee.prepareOrder(null);
    }

    /**
     * Verifies that if no employees of a specific type are available in the system,
     * an {@link IllegalStateException} is thrown during the random selection process.
     */
    @Test(expected = IllegalStateException.class)
    public void selectRandomEmployeeNoAvailableEmployeeTest(){
        EmployeeDAOMemory.getInstance().clear();
        employee.selectRandomEmployee(Employee.class);
    }


    /**
     * Verifies the successful preparation of an order when stock is sufficient.
     * Checks if:
     * 1. The order status changes to SHIPPED.
     * 2. Warehouse stock is correctly deducted.
     * 3. The order is assigned to a Deliverer.
     * 4. Employee's preparation count increases.
     */
    @Test
    public void prepareOrderTestSufficientStock() {

        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypeDAOMemory.getInstance().addProductType(dummyProductType1);

        ProductsWareHouseDAOMemory.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypeDAOMemory.getInstance().getProduct("product1246"), 2));
        orderDAOMemory.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(OrderStatusType.SHIPPED, order.getOrderstatus());
        assertEquals(8, (int) ProductsWareHouseDAOMemory.getInstance().getProductStock(dummyProductType1));
        assertEquals(1, employee.getTotalOrdersPreparations());


        Deliverer delivererSelected = (Deliverer) EmployeeDAOMemory.getInstance().getEmployees().get("DEL_1");
        assertTrue(delivererSelected.getOrders().contains(order));
    }


    /**
     * Verifies the system behavior when an order requires more items than available in stock.
     * Checks if:
     * 1. The order status changes to DELAYED.
     * 2. Stock remains unchanged.
     * 3. A reserve update request is logged.
     * 4. Automatic emails are sent to the Admin and Customer Service.
     */
    @Test
    public void prepareOrderTestInsufficientStock(){
        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypeDAOMemory.getInstance().addProductType(dummyProductType1);

        ProductsWareHouseDAOMemory.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypeDAOMemory.getInstance().getProduct("product1246"), 11));
        orderDAOMemory.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(OrderStatusType.DELAYED, order.getOrderstatus());
        assertEquals(10, (int) ProductsWareHouseDAOMemory.getInstance().getProductStock(dummyProductType1)); // Το stock δεν πρέπει να αλλάξει
        assertEquals(1, employee.getTotalUpdateReserveRequests());

        // Admin mail reception
        assertEquals(1, Admin.getInstance().getEmailProvider().getInboxEmails().size());

        // Customer service employee mail reception
        CustomerServiceEmployee customerServiceEmployeeSelected = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployees().get("CSE_1");
        assertEquals(1, customerServiceEmployeeSelected.getEmailProvider().getInboxEmails().size());

    }

    /**
     * Cleans up all memory repositories after each test to ensure test isolation.
     */
    @After
    public void tearDownTest(){
        OrderDAOMemory.getInstance().clear();
        ProductsWareHouseDAOMemory.getInstance().clear();
        ProductTypeDAOMemory.getInstance().clear();
        EmployeeDAOMemory.getInstance().clear();

        Admin.getInstance().getEmailProvider().getInboxEmails().clear();
    }
}