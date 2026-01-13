package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Customer} class.
 * Checks functionality for product search, shopping cart management,
 * checkout process, payment selection and customer account management.
 *
 * @author PAVLOS GRATSANIS
 */
public class CustomerTest {
    private Customer customer;
    private EmailAddress email;
    private Address address;
    private ShoppingCart shoppingCart;
    private Order order;

    /**
     * Sets up the test environment before each test.
     * Initializes a customer and his details, adds them to the DAO, and creates a  order.
     */
    @Before
    public void setUp() throws Exception {
        email = TestHelper.getEmail();
        address = TestHelper.getAddress();
        CustomerDAOMemory.getInstance().getCustomers().clear();

        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "CUST-001", new Date());

        CustomerDAOMemory.getInstance().addCustomer(customer);
        customer.setAddress(address);

        order = new Order("order001", new Date(), OrderStatusType.NEW, false,
                PaymentType.CASH, new Date(), new ShoppingCart());
    }

    /**
     * Tests finding a product by its code.
     */
    @Test
    public void findProduct() {
        TestHelper.addProductsManually();
        ProductType p = customer.findProduct(TestHelper.getProducts(), "l101");
        assertEquals(p, TestHelper.getLaptop());
    }

    /**
     * Tests finding a product with empty list or invalid code.
     */
    @Test
    public void findProductwithNullArguments() {
        ProductType p, k;
        p = customer.findProduct(TestHelper.getProducts(), "l101");
        assertNull(p); // empty products map

        k = customer.findProduct(TestHelper.getProducts(), "k101");
        assertNull(k); // error productcode
    }

    /**
     * Tests adding items to the shopping cart and updating quantity.
     */
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

    /**
     * Tests that adding an item with negative quantity throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addItemToCartWithNegativeQuantity() {
        ProductType p = TestHelper.getMonitor();
        customer.addItemToCart(p, -1);
    }

    /**
     * Tests that adding a null product throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addItemToCartWithNullProduct() {
        customer.addItemToCart(null, 2);
    }

    /**
     * Tests removing items from the shopping cart.
     */
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

    /**
     * Tests removing item from an empty cart throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeItemFromCartwithEmptyShoppingCart() {
        customer.removeItemFromCart(TestHelper.getKeyboard(), 5);
    }

    /**
     * Tests removing item with negative quantity throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNegativeQuantity() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getKeyboard(), -2);
    }

    /**
     * Tests removing null product throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNullProduct() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(null, 5);
    }

    /**
     * Tests removing more quantity than available throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithQuantityTooHigh() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getKeyboard(), 10);
    }

    /**
     * Tests removing an item that is not in the cart throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeItemWhereNotInCart() {
        customer.addItemToCart(TestHelper.getKeyboard(), 5);
        customer.removeItemFromCart(TestHelper.getLaptop(), 2);
    }

    /**
     * Tests the checkout process returns a valid Order.
     */
    @Test
    public void checkout() {
        customer.addItemToCart(TestHelper.getLaptop(), 1);
        Order order1 = customer.Checkout();
        assertEquals(OrderStatusType.NEW, order1.getOrderstatus());
        assertEquals(PaymentType.CASH, order1.getPaymentmethod());
        assertEquals(false, order1.getPaid());
    }

    /**
     * Tests checkout with empty/null conditions returns null.
     */
    @Test
    public void checkoutwithNullArguments() {
        assertNull(customer.Checkout());
    }

    /**
     * Tests that checkout creates a deep copy of the shopping cart for the order.
     * Modifications to the customer's cart after checkout should not affect the order's cart.
     */
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

    /**
     * Tests selecting a payment type.
     */
    @Test
    public void selectPaymentType() {
        assertFalse(order.getPaid());
        assertEquals(PaymentType.CASH, order.getPaymentmethod());

        customer.selectPaymentType(PaymentType.CARD, "1234-5678-9123-4567", order);

        assertTrue(order.getPaid());
        assertEquals(PaymentType.CARD, order.getPaymentmethod());
    }

    /**
     * Tests selecting payment type with null order throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullOrder() {
        customer.selectPaymentType(PaymentType.CASH, null, null);
    }

    /**
     * Tests selecting null payment type throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullPaymentType() {
        customer.selectPaymentType(null, null, order);
    }

    /**
     * Tests selecting card payment with invalid format throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithInvalidCardFormat() {
        customer.selectPaymentType(PaymentType.CARD, "1234-5678", order);
    }

    /**
     * Tests confirming an order.
     */
    @Test
    public void confirm() {
        customer.addItemToCart(TestHelper.getMouse(), 5);
        Order order1 = customer.Checkout();
        customer.Confirm("CONFIRM", order1);

        assertTrue(OrderDAOMemory.getInstance().getOrders().containsKey(order1.getOrdercode()));
        assertNull(customer.getShoppingCart());
    }

    /**
     * Tests confirming with null order throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullOrder() {
        customer.Confirm("CONFIRM", null);
    }

    /**
     * Tests confirming with null choice string throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullConfirmChoice() {
        customer.Confirm(null, order);
    }

    /**
     * Tests confirming with empty choice string throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void confirmWithEmptyConfirmChoice() {
        customer.Confirm("", order);
    }

    /**
     * Tests removing the customer from the repository.
     */
    @Test
    public void remove() {
        CustomerDAOMemory.getInstance().removeCustomer(customer);
        assertFalse(CustomerDAOMemory.getInstance().getCustomers().containsKey(customer.getCustomer_id()));
    }

    /**
     * Tests removing a customer that doesn't exist throws exception.
     */
    @Test(expected = IllegalStateException.class)
    public void removeCustomerThatDoesNotExist() {
        customer.remove();
        customer.remove();
    }

    /**
     * Cleans up after each test execution.
     */
    @After
    public void tearDown() throws Exception {
        TestHelper.clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
        OrderDAOMemory.getInstance().getOrders().clear();
    }

    /**
     * Cleans up after all tests in the class have run.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        TestHelper.clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
        OrderDAOMemory.getInstance().getOrders().clear();
    }
}