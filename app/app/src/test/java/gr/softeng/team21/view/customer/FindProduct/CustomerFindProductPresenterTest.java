package gr.softeng.team21.view.customer.FindProduct;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * Unit tests for the {@link CustomerFindProductPresenter} class.
 * Adapted to test asynchronous data loading through CompletableFuture logic.
 *
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductPresenterTest {
    private CustomerFindProductPresenter presenter;
    private CustomerFindProductViewStub view;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerFindProductViewStub();

        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        ProductTypeDAO productDAO = ProductTypeDAOMemory.getInstance();

        // Φορτώνουμε τον πελάτη (μόνο για να χρησιμοποιούμε το αντικείμενο στα tests)
        customer = customerDAO.getCustomer("CUST-500").join();

        presenter = new CustomerFindProductPresenter(view, customerDAO, productDAO);
    }

    @Test
    public void loadInitialDataSuccessfullyPopulatesProducts() {
        presenter.loadInitialData("CUST-500");

        Assert.assertEquals(1, view.getShowProductsCount());
        Assert.assertNotNull(view.getShowedProducts());
        Assert.assertNull(view.getErrorMessage());
    }

    @Test
    public void loadInitialDataShowsErrorForInvalidCustomer() {
        presenter.loadInitialData("INVALID");

        Assert.assertNull(view.getShowedProducts());
        Assert.assertNotNull(view.getErrorMessage());
        Assert.assertEquals("Ο πελάτης δεν βρέθηκε.", view.getErrorMessage());
    }

    @Test
    public void filterAppliesSearchTextCorrectly() {
        presenter.loadInitialData("CUST-500"); // Must load first

        presenter.filter("Dell");
        ArrayList<ProductType> results = view.getShowedProducts();
        Assert.assertTrue("Η λίστα δεν πρέπει να είναι άδεια", results.size() > 0);
        Assert.assertTrue("Το προϊόν πρέπει να περιέχει 'Dell'", results.get(0).getProductname().toLowerCase().contains("dell"));

        presenter.filter("");
        Assert.assertEquals(MemoryInitializer.getProductTypeDAO().getProducts().join().size(), view.getShowedProducts().size());

        presenter.filter(null);
        Assert.assertEquals(MemoryInitializer.getProductTypeDAO().getProducts().join().size(), view.getShowedProducts().size());

        presenter.filter("ΚάτιΠουΔενΥπάρχει");
        Assert.assertEquals(0, view.getShowedProducts().size());
    }

    @Test
    public void productClickedNavigatesToDetails() {
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-001").join();
        presenter.ProductClicked(product);
        Assert.assertEquals("TECH-001", view.getProductCode());
    }

    @Test
    public void productClickedWithNullProductDoesNothing() {
        presenter.ProductClicked(null);
        Assert.assertNull(view.getProductCode());
    }

    @Test
    public void openShoppingCartNavigatesIfNotEmpty() {
        presenter.loadInitialData("CUST-500");
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-010").join();
        customer.addItemToCart(product, 1);

        presenter.openShoppingCartClicked();
        Assert.assertEquals(1, view.getShoppingCartCount());
    }

    @Test
    public void openShoppingCartShowsErrorIfEmpty() {
        presenter.loadInitialData("CUST-500");

        presenter.openShoppingCartClicked();
        Assert.assertEquals(0, view.getShoppingCartCount());
        Assert.assertEquals("To καλάθι είναι άδειο!!", view.getEmptyShoppingCartMessage());
    }

    @Test
    public void updateShoppingCartStatusReflectsCorrectQuantity() {
        presenter.loadInitialData("CUST-500");
        ProductType product1 = MemoryInitializer.getProductTypeDAO().getProduct("TECH-010").join();
        ProductType product2 = MemoryInitializer.getProductTypeDAO().getProduct("TECH-011").join();

        customer.addItemToCart(product1, 1);
        customer.addItemToCart(product2, 1);

        presenter.updateShoppingCartStatus();
        Assert.assertEquals(2, view.getUpdateShoppingCartCount());
    }
}