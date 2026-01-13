package gr.softeng.team21.domain;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;

/**
 * Unit tests for the {@link ShoppingCart} class.
 * Verifies the calculation of the total cost when adding or removing items.
 * @author PAVLOS GRATSANIS
 */
public class ShoppingCartTest {
    private ShoppingCart shoppingCart;
    private Customer customer;
    private ProductType p1, p2, p3;

    /**
     * Sets up the test environment before each test.
     * Initializes a customer and a shopping cart with two default items.
     */
    @Before
    public void setUp() throws Exception {
        customer = TestHelper.getCustomer();
        shoppingCart = new ShoppingCart(customer);
        p1 = TestHelper.getLaptop();
        shoppingCart.addItem(new CartItem(p1, 2));
        p2 = TestHelper.getMouse();
        shoppingCart.addItem(new CartItem(p2, 3));
    }

    /**
     * Tests the calculation of total cost when a new product is added to the cart.
     */
    @Test
    public void getTotalCostWhenAddProduct() {
        p3 = TestHelper.getKeyboard();
        shoppingCart.addItem(new CartItem(p3, 3));

        assertEquals(1390, shoppingCart.getTotalCost().getAmount().intValue());
        assertEquals("€", shoppingCart.getTotalCost().getCurrency());
    }

    /**
     * Tests the calculation of total cost when a product is removed from the cart.
     */
    @Test
    public void getTotalCostWhenRemoveProduct() {
        shoppingCart.removeItem(shoppingCart.getItems().get(0));

        assertEquals(150, shoppingCart.getTotalCost().getAmount().intValue());
        assertEquals("€", shoppingCart.getTotalCost().getCurrency());
    }

    /**
     * Cleans up after all tests in the class have run.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}