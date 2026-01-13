package gr.softeng.team21.view.product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for the {@link ProductDetailsPresenter} class.
 * These tests verify the logic for loading product details, managing quantity, adding items to the cart and navigation.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsPresenterTest {

    private ProductDetailsPresenter presenter;
    private ProductDetailsViewStub view;
    private Customer customer;
    private ShoppingCart cart;
    private ProductType product;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data,a view stub , the presenter, a test customer and  a test product.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-503");
        cart=new ShoppingCart(customer);
        view = new ProductDetailsViewStub();
        presenter = new ProductDetailsPresenter(view, customer);
        product=MemoryInitializer.getProductTypeDAO().getProduct("TECH-020");
    }

    /**
     * Verifies that the quantity increases correctly when the plus button is clicked.
     */
    @Test
    public void plusClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.plusClicked();
        presenter.plusClicked();
        Assert.assertEquals(3, view.getQuantity());

    }

    /**
     * Verifies that the quantity decreases correctly when the minus button is clicked
     * and ensures it does not drop below 1.
     */
    @Test
    public void minusClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.minusClicked();
        Assert.assertEquals(1, view.getQuantity());
        presenter.plusClicked();
        presenter.minusClicked();
        Assert.assertEquals(1, view.getQuantity());

    }

    /**
     * Verifies that the product is successfully added to the ShoppingCart
     * and that the view updates the corresponding counter.
     */
    @Test
    public void addToCartClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.plusClicked();
        presenter.addToCartClicked();
        Assert.assertEquals(1,view.getAddToCartCount());
        Assert.assertEquals(1, customer.getShoppingCart().getItems().size());
    }

    /**
     * Test the navigation to the ShoppingCart works.
     */
    @Test
    public void openShoppingCartClicked() {
        presenter.openShoppingCartClicked();
        Assert.assertEquals(1,view.getCartCount());
        Assert.assertEquals("Μετάβαση στο Καλάθι...", view.getMessage());
    }

    /**
     * Test that product details are correctly loaded into the view.
     */
    @Test
    public void loadProduct() {
        presenter.loadProduct(product.getProductCode());
        Assert.assertEquals("TECH-020", view.getCode());

    }

    /**
     * Test the presenter's behavior when loading a product with null or invalid arguments.
     * Ensures quantity remains at default value.
     */
    @Test
    public void loadProductWithNullArguments() {
        presenter.loadProduct(null);
        Assert.assertEquals(1, view.getQuantity());

        presenter.loadProduct("TECH-150");
        Assert.assertEquals(1, view.getQuantity());

    }

    /**
     * Verifies that adding to cart fails gracefully when no product is currently loaded.
     */
    @Test
    public void addToCartClickedWithNullProduct() {
        presenter.addToCartClicked();
        Assert.assertNotNull(view.getMessage());
        Assert.assertEquals(0, view.getAddToCartCount());

    }
}