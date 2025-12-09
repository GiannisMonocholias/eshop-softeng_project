package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class OrderPreparationEmployeeTest {
    private String orderId;
    private OrderPreparationEmployee employee;
    private CustomerServiceEmployee customerServiceEmployee;
    private Deliverer deliverer;
    private OrdersRepository ordersRepository;

    @Before
    public void setUp(){
        OrdersRepository.getInstance().clear();
        EmployeeRepository.getInstance().clear();
        ProductsWareHouse.getInstance().clear();
        ProductTypesRepository.getInstance().clear();

        Admin.getInstance();
        Admin.getInstance().setEmailProviderStub(new EmailProviderStub());


        employee = new OrderPreparationEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"OPE_1",100,1000,
                8,EmployeeState.ACTIVE, new Date(3,5,2025));
        employee.setEmailProviderStub(new EmailProviderStub());

        ordersRepository = OrdersRepository.getInstance();
        ordersRepository.addOrder(new Order("order1245", new Date(20,5,2025),StatusType.NEW,false,PaymentType.CASH, new Date(),new ShoppingCart()));

        deliverer =new Deliverer("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"DEL_1",100,1000,8,
                EmployeeState.ACTIVE, new Date(3,5,2025),100 , true , new ArrayList<>());

        customerServiceEmployee = new CustomerServiceEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"CSE_1",100,1000,
                8,EmployeeState.ACTIVE, new Date(3,5,2025));
        customerServiceEmployee.setEmailProviderStub(new EmailProviderStub());


    }

    @Test(expected = NoSuchElementException.class)
    public void selectNonExistingOrder() {
        employee.selectOrder("order1246");
    }

    @Test
    public void selectOrderTest(){
        String orderCode = "order1245";
        employee.selectOrder(orderCode);

        Order selectedOrder = ordersRepository.getOrder(orderCode);

        assertTrue(employee.getAssignedOrders().contains(selectedOrder));
    }


    @Test(expected = NoSuchElementException.class)
    public void prepareOrder_NonAssignedExistingOrderTest() {
        // Existing but not assigned order
        Order nonAssignedOrder1 = OrdersRepository.getInstance().getOrder("order1245");
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
        EmployeeRepository.getInstance().clear();
        employee.selectRandomEmployee(Employee.class);
    }


    @Test
    public void prepareOrderTestSufficientStock() {

        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypesRepository.getInstance().addProductType(dummyProductType1);

        ProductsWareHouse.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypesRepository.getInstance().getProduct("product1246"), 2));
        ordersRepository.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(StatusType.SHIPPED, order.getOrderstatus());
        assertEquals(8, (int)ProductsWareHouse.getInstance().getProductStock(dummyProductType1));
        assertEquals(1, employee.getTotalOrdersPreparations());


        Deliverer delivererSelected = (Deliverer) EmployeeRepository.getInstance().getEmployees().get("DEL_1");
        assertTrue(delivererSelected.getOrders().contains(order));
    }



    @Test
    public void prepareOrderTestInsufficientStock(){
        ProductType dummyProductType1 = new ProductType("LAPTOP","500",new Money(500,"€"),"product1246");
        ProductTypesRepository.getInstance().addProductType(dummyProductType1);

        ProductsWareHouse.getInstance().increaseProductStock(dummyProductType1,10);

        Order order = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        order.getShoppingCart().addItem(new CartItem(ProductTypesRepository.getInstance().getProduct("product1246"), 11));
        ordersRepository.addOrder(order);


        employee.selectOrder("order1246");
        employee.prepareOrder(employee.getAssignedOrders().get(0));


        assertEquals(StatusType.DELAYED, order.getOrderstatus());
        assertEquals(10, (int)ProductsWareHouse.getInstance().getProductStock(dummyProductType1)); // Το stock δεν πρέπει να αλλάξει
        assertEquals(1, employee.getTotalUpdateReserveRequests());

        // Admin mail reception
        assertEquals(1, Admin.getInstance().getEmailProviderStub().getInboxEmails().size());

        // Customer service employee mail reception
        CustomerServiceEmployee customerServiceEmployeeSelected = (CustomerServiceEmployee) EmployeeRepository.getInstance().getEmployees().get("CSE_1");
        assertEquals(1, customerServiceEmployeeSelected.getEmailProviderStub().getInboxEmails().size());

    }

    @After
    public void tearDownTest(){
        OrdersRepository.getInstance().clear();
        ProductsWareHouse.getInstance().clear();
        ProductTypesRepository.getInstance().clear();
        EmployeeRepository.getInstance().clear();

        Admin.getInstance().getEmailProviderStub().getInboxEmails().clear();
    }
}