package gr.softeng.team21.view.customer.FindProduct;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * Unit tests for the {@link CustomerFindProductPresenter} class.
 * These tests verify the logic for loading product lists, filtering products, handling product selection,
 * and managing shopping cart interactions.
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductPresenterTest {
    private CustomerFindProductPresenter presenter;
    private CustomerFindProductViewStub view;
    private Customer customer;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data, a view stub and the presenter, retrieves a test customer.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerFindProductViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-500");
        presenter = new CustomerFindProductPresenter(view, customer);
    }

    /**
     * Test that the product list is successfully loaded into the view.
     */
    @Test
    public void loadList() {
        presenter.loadList();
        Assert.assertEquals(1, view.getShowProductsCount());
        Assert.assertNotNull(view.getShowedProducts());
    }

    /**
     * Test the filtering logic for the product list.
     * Tests exact matches, empty queries, null queries, and queries with no results.
     */
    @Test
    public void filter() {
        // Test filtering with a valid string
        presenter.filter("Dell");
        ArrayList<ProductType> results = view.getShowedProducts();
        Assert.assertTrue("Η λίστα δεν πρέπει να είναι άδεια", results.size() > 0);
        Assert.assertTrue("Το προϊόν πρέπει να περιέχει 'Dell'", results.get(0).getProductname().contains("Dell"));

        presenter.filter("");
        Assert.assertEquals(
                MemoryInitializer.getProductTypeDAO().getProducts().size(),
                view.getShowedProducts().size()
        );

        presenter.filter(null);
        Assert.assertEquals(
                MemoryInitializer.getProductTypeDAO().getProducts().size(),
                view.getShowedProducts().size()
        );

        presenter.filter("ΚάτιΠουΔενΥπάρχει");
        Assert.assertEquals(0, view.getShowedProducts().size());
    }

    /**
     * Verifies that clicking a product triggers the navigation to the product details screen
     * with the correct product code.
     */
    @Test
    public void productClicked() {
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-001");
        presenter.ProductClicked(product);
        Assert.assertEquals("TECH-001", view.getProductCode());
    }

    /**
     * Verifies that clicking with a null product does not trigger navigation.
     */
    @Test
    public void productClickedWithNullProduct() {
        presenter.ProductClicked(null);
        Assert.assertNull(view.getProductCode());
    }

    /**
     * Test that clicking the shopping cart icon navigates to the cart if it contains items.
     */
    @Test
    public void openShoppingCartClicked() {
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-010"), 1);
        presenter.openShoppingCartClicked();
        Assert.assertEquals(1, view.getShoppingCartCount());
    }

    /**
     * Test that clicking the shopping cart icon shows an error message if the cart is empty.
     */
    @Test
    public void openShoppingCartClickedWithNullShoppingCart() {
        presenter.openShoppingCartClicked();
        Assert.assertEquals(0, view.getShoppingCartCount());
        Assert.assertEquals("To καλάθι είναι άδειο!!", view.getEmptyShoppingCartMessage());
    }

    /**
     * Test that the shopping cart quantity indicator is updated correctly when items are added.
     */
    @Test
    public void updateShoppingCartStatus() {
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-010"), 1);
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-011"), 1);
        presenter.updateShoppingCartStatus();
        Assert.assertEquals(2, view.getUpdateShoppingCartCount());
    }

    /**
     * Test that the shopping cart quantity indicator shows 0 when the cart is empty.
     */
    @Test
    public void updateShoppingCartStatusWithNullShoppingCart() {
        presenter.updateShoppingCartStatus();
        Assert.assertEquals(0, view.getUpdateShoppingCartCount());
    }
}