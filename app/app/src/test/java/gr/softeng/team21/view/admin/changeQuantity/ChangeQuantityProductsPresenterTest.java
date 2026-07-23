package gr.softeng.team21.view.admin.changeQuantity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * This test class verifies the asynchronous behavior of the presenter responsible
 * for loading product types to be displayed in the Change Quantity UI.
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsPresenterTest {

    private ChangeQuantityProductsPresenter presenter;
    private ChangeQuantityProductsViewStub viewStub;
    private ProductTypeDAOMemory productTypeDAOMemory;

    /**
     * Sets up the test environment before each test method.
     * Initializes in-memory data, creates a stub view, and instantiates
     * the presenter with the MemoryDAO and view stub via Dependency Injection.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        productTypeDAOMemory = ProductTypeDAOMemory.getInstance();
        viewStub = new ChangeQuantityProductsViewStub();

        presenter = new ChangeQuantityProductsPresenter(viewStub, productTypeDAOMemory);
    }

    /**
     * Tests that the presenter correctly loads all available products asynchronously.
     * Verifies that the number of products returned to the ViewStub
     * matches the expected size of the in-memory DAO (20 in this case).
     */
    @Test
    public void loadProductsSuccessfullyPopulatesView() {
        // Η κλήση ολοκληρώνεται ακαριαία χάρη στο completedFuture του MemoryDAO
        presenter.loadProducts();

        ArrayList<ProductType> products = viewStub.getLoadedProducts();

        assertNotNull("Products list should not be null", products);
        assertEquals(20, products.size());
        assertNull("There should be no error message", viewStub.getErrorMessage());
    }
}