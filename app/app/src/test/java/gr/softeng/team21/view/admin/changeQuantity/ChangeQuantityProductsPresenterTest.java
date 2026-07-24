package gr.softeng.team21.view.admin.changeQuantity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * Unit testing class that verifies the asynchronous behavior of the
 * {@link ChangeQuantityProductsPresenter} responsible for loading product
 * types to be displayed in the Change Quantity UI.
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsPresenterTest {

    private ChangeQuantityProductsPresenter presenter;
    private ChangeQuantityProductsViewStub viewStub;
    private ProductTypeDAOMemory productTypeDAOMemory;

    /**
     * Sets up the test environment before each test method execution.
     * Initializes in-memory mock data, creates a view stub, and instantiates
     * the presenter with the required MemoryDAO Dependency Injection.
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
     * matches the expected predefined size of the in-memory DAO (e.g., 20 items).
     */
    @Test
    public void loadProductsSuccessfullyPopulatesView() {
        // Asynchronous call, executes instantly using MemoryDAO CompletableFutures
        presenter.loadProducts();

        ArrayList<ProductType> products = viewStub.getLoadedProducts();

        assertNotNull("Products list should not be null", products);
        assertEquals(20, products.size());
        assertNull("There should be no error message during successful fetch", viewStub.getErrorMessage());
    }
}