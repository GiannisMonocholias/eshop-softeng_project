package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public class OrderPreparationEmployeeTest {
    private String orderId;
    private OrderPreparationEmployee employee;
    private CustomerServiceEmployee customerServiceEmployee;
    private Deliverer deliverer;
    private OrderDAOMemory orderDAOMemory;

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
        orderDAOMemory.addOrder(new Order("order1245", new Date(20,5,2025),StatusType.NEW,false,PaymentType.CASH, new Date(),new ShoppingCart()));

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

    @Test(expected = NoSuchElementException.class)
    public void selectNonExistingOrder() {
        employee.selectOrder("order1246");
    }

    @Test
    public void selectOrderTest(){
        String orderCode = "order1245";
        employee.selectOrder(orderCode);

        Order selectedOrder = orderDAOMemory.getOrder(orderCode);

        assertTrue(employee.getAssignedOrders().contains(selectedOrder));
    }


    @Test(expected = NoSuchElementException.class)
    public void prepareOrder_NonAssignedExistingOrderTest() {
        // Existing but not assigned order
        Order nonAssignedOrder1 = OrderDAOMemory.getInstance().getOrder("order1245");
        employee.prepareOrder(nonAssignedOrder1);
    }

    @Test(expected = NoSuchElementException.class)
    public void prepareOrder_NonExistingOrderTest() {
        // Non-existing order
        Order nonAssignedOrder2 = new Order("order1246", new Date(20,5,2025),StatusType.NEW,false,PaymentType.CASH, new Date(), new ShoppingCart());
        employee.prepareOrder(nonAssignedOrder2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void prepareOrder_NullArgumentTest() {
        // Null argument
        employee.prepareOrder(null);
    }

    @Test(expected = IllegalStateException.class)
    public void selectRandomEmployeeNoAvailableEmployeeTest(){
        EmployeeDAOMemory.getInstance().clear();
        employee.selectRandomEmployee(Employee.class);
    }


    @Test
    public void prepareOrderTestSufficientStock() {

        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypeDAOMemory.getInstance().addProductType(dummyProductType1);

        ProductsWareHouseDAOMemory.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypeDAOMemory.getInstance().getProduct("product1246"), 2));
        orderDAOMemory.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(StatusType.SHIPPED, order.getOrderstatus());
        assertEquals(8, (int) ProductsWareHouseDAOMemory.getInstance().getProductStock(dummyProductType1));
        assertEquals(1, employee.getTotalOrdersPreparations());


        Deliverer delivererSelected = (Deliverer) EmployeeDAOMemory.getInstance().getEmployees().get("DEL_1");
        assertTrue(delivererSelected.getOrders().contains(order));
    }



    @Test
    public void prepareOrderTestInsufficientStock(){
        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypeDAOMemory.getInstance().addProductType(dummyProductType1);

        ProductsWareHouseDAOMemory.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypeDAOMemory.getInstance().getProduct("product1246"), 11));
        orderDAOMemory.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(StatusType.DELAYED, order.getOrderstatus());
        assertEquals(10, (int) ProductsWareHouseDAOMemory.getInstance().getProductStock(dummyProductType1)); // Το stock δεν πρέπει να αλλάξει
        assertEquals(1, employee.getTotalUpdateReserveRequests());

        // Admin mail reception
        assertEquals(1, Admin.getInstance().getEmailProvider().getInboxEmails().size());

        // Customer service employee mail reception
        CustomerServiceEmployee customerServiceEmployeeSelected = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployees().get("CSE_1");
        assertEquals(1, customerServiceEmployeeSelected.getEmailProvider().getInboxEmails().size());

    }

    @After
    public void tearDownTest(){
        OrderDAOMemory.getInstance().clear();
        ProductsWareHouseDAOMemory.getInstance().clear();
        ProductTypeDAOMemory.getInstance().clear();
        EmployeeDAOMemory.getInstance().clear();

        Admin.getInstance().getEmailProvider().getInboxEmails().clear();
    }
}