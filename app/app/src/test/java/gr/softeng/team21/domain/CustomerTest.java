package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Customer} class.
 * Checks functionality for product search, shopping cart management,
 * checkout process, payment selection and customer account management.
 * @author PAVLOS GRATSANIS
 */
public class CustomerTest {
    private Customer customer;
    private EmailAddress email;
    private Address address;
    private Order order;

    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    @Before
    public void setUp() throws Exception {
        customerDAO = MemoryInitializer.getCustomerDAO();
        orderDAO = MemoryInitializer.getOrderDAO();
        customerDAO.clear().join();

        email = TestHelper.getEmail();
        address = TestHelper.getAddress();

        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "CUST-001", new Date());

        customerDAO.addCustomer(customer).join();
        customer.setAddress(address);

        order = new Order("order001", new Date(), OrderStatusType.NEW, false,
                PaymentType.CASH, new Date(), new ShoppingCart());
    }

    @Test
    public void addItemToCart() {
        ProductType p = TestHelper.getMonitor();
        ProductType l = TestHelper.getLaptop();

        customer.addItemToCart(p, 2);
        customer.addItemToCart(l, 3);
        customer.addItemToCart(l, 1);

        assertEquals(2, customer.getShoppingCart().getItems().size());
        assertEquals(4, customer.getShoppingCart().getItems().get(1).getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItemToCartWithNegativeQuantity() {
        ProductType p = TestHelper.getMonitor();
        customer.addItemToCart(p, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItemToCartWithNullProduct() {
        customer.addItemToCart(null, 2);
    }

    @Test
    public void removeItemFromCart() {
        ProductType p1 = TestHelper.getMonitor();
        ProductType p2 = TestHelper.getKeyboard();

        customer.addItemToCart(p1, 6);
        customer.addItemToCart(p2, 5);

        customer.removeItemFromCart(p1, 3);
        assertEquals(3, customer.getShoppingCart().getItems().get(0).getQuantity());

        customer.removeItemFromCart(p2, 5);
        assertFalse(customer.getShoppingCart().getItems().contains(p2.getProductCode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemFromCartwithEmptyShoppingCart() {
        customer.removeItemFromCart(TestHelper.getKeyboard(), 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNegativeQuantity() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getKeyboard(), -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNullProduct() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithQuantityTooHigh() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getKeyboard(), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWhereNotInCart() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getLaptop(), 2);
    }

    @Test
    public void checkout() {
        customer.addItemToCart(TestHelper.getLaptop(), 1);
        Order order1 = customer.Checkout();
        assertEquals(OrderStatusType.NEW, order1.getOrderstatus());
        assertEquals(PaymentType.CASH, order1.getPaymentmethod());
        assertEquals(false, order1.getPaid());
    }

    @Test
    public void checkoutwithNullArguments() {
        assertNull(customer.Checkout());
    }

    @Test
    public void CheckoutCopyShoppingCart() {
        ProductType laptop = TestHelper.getLaptop();
        customer.addItemToCart(laptop, 1);
        Order order2 = customer.Checkout();

        customer.setShoppingCart(new ShoppingCart(customer));
        customer.addItemToCart(laptop, 5);

        assertEquals(1, order2.getShoppingCart().getItems().size());
        assertEquals(1, order2.getShoppingCart().getItems().get(0).getQuantity());

        assertEquals(1, customer.getShoppingCart().getItems().size());
        assertEquals(5, customer.getShoppingCart().getItems().get(0).getQuantity());
    }

    @Test
    public void selectPaymentType() {
        assertFalse(order.getPaid());
        assertEquals(PaymentType.CASH, order.getPaymentmethod());

        customer.selectPaymentType(PaymentType.CARD, "1234-5678-9123-4567", order);

        assertTrue(order.getPaid());
        assertEquals(PaymentType.CARD, order.getPaymentmethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullOrder() {
        customer.selectPaymentType(PaymentType.CASH, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullPaymentType() {
        customer.selectPaymentType(null, null, order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithInvalidCardFormat() {
        customer.selectPaymentType(PaymentType.CARD, "1234-5678", order);
    }

    @Test
    public void confirm() {
        customer.addItemToCart(TestHelper.getMouse(), 5);
        Order order1 = customer.Checkout();
        customer.Confirm("CONFIRM", order1);

        assertNull(customer.getShoppingCart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullOrder() {
        customer.Confirm("CONFIRM", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullConfirmChoice() {
        customer.Confirm(null, order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithEmptyConfirmChoice() {
        customer.Confirm("", order);
    }

    @After
    public void tearDown() throws Exception {
        TestHelper.clear();
        customerDAO.clear().join();
        orderDAO.clear().join();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        TestHelper.clear();
        MemoryInitializer.getCustomerDAO().clear().join();
        MemoryInitializer.getOrderDAO().clear().join();
    }
}