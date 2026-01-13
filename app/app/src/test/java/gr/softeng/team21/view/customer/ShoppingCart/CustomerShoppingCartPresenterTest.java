package gr.softeng.team21.view.customer.ShoppingCart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for the {@link CustomerShoppingCartPresenter} class.
 * These tests verify the logic for managing cart items (add, remove, delete),
 * calculating total price, and handling payment navigation.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartPresenterTest {
    private CustomerShoppingCartPresenter presenter;
    private CustomerShoppingCartViewStub view;
    private Customer customer;
    private ProductType product;
    private CartItem item;
    private ShoppingCart cart;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data, a view stub and the presenter, retrieves a test customer
     * and adds a sample product to the cart.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerShoppingCartViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-500");
        presenter = new CustomerShoppingCartPresenter(view, customer);
        cart = new ShoppingCart(customer);
        product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-008");
        customer.addItemToCart(product, 2);
        item = cart.getItems().get(0);
    }

    /**
     * Test that clicking "Payment" triggers navigation to the payment screen when the cart is not empty.
     */
    @Test
    public void continuePaymentClicked() {
        presenter.ContinuePaymentClicked();
        Assert.assertEquals(1, view.getGoToPaymentCount());
    }

    /**
     * Test that clicking "Payment" shows an error message and does not navigate when the cart is empty.
     */
    @Test
    public void continuePaymentClickedWithEmptyCart() {
        ShoppingCart cart2 = new ShoppingCart(customer);
        presenter.ContinuePaymentClicked();
        Assert.assertEquals(0, view.getGoToPaymentCount());
        Assert.assertEquals("Το καλάθι είναι άδειο!", view.getMessage());
    }

    /**
     * Test that clicking "Plus" increases the quantity of the item in the cart.
     */
    @Test
    public void plusClicked() {
        presenter.plusClicked(item);
        Assert.assertEquals(3, customer.getShoppingCart().getItems().get(0).getQuantity());

    }

    /**
     * Test that operations with null arguments are handled gracefully without crashing.
     */
    @Test
    public void testWithNullArguments() {
        ShoppingCart cart3 = new ShoppingCart(customer);
        presenter.plusClicked(null);
        Assert.assertTrue(cart3.getItems().isEmpty());
        presenter.minusClicked(null);
        Assert.assertTrue(cart3.getItems().isEmpty());
        presenter.deleteClicked(null);
        Assert.assertTrue(cart3.getItems().isEmpty());
    }

    /**
     * Test that clicking "Minus" decreases the quantity of the item in the cart.
     */
    @Test
    public void minusClicked() {
        presenter.minusClicked(item);
        Assert.assertEquals(1, customer.getShoppingCart().getItems().get(0).getQuantity());
    }

    /**
     * Test that clicking "Delete" removes the item completely from the cart.
     */
    @Test
    public void deleteClicked() {
        presenter.deleteClicked(item);
        Assert.assertEquals(0, customer.getShoppingCart().getItems().size());
        Assert.assertEquals("Αφαιρέθηκε", view.getMessage());
    }

    /**
     * Test that the total price calculation is correct and updated in the view.
     */
    @Test
    public void setTotalprice() {
        presenter.setTotalprice();
        Assert.assertEquals(new BigDecimal("1500.0"), cart.getTotalCost().getAmount());
        Assert.assertEquals("€", cart.getTotalCost().getCurrency());

    }

    /**
     * Test that loading cart data handles a null shopping cart gracefully
     * by showing an empty list instead of crashing.
     */
    @Test
    public void loadCartDataWithNullArguments() {
        customer.setShoppingCart(null);
        presenter.loadCartData();
        Assert.assertNotNull(view.getCartItems());
        Assert.assertTrue(view.getCartItems().isEmpty());

    }
}