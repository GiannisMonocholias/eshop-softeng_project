package gr.softeng.team21.view.admin.changeQuantity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 *
 * This test class verifies the behavior of the presenter responsible
 * for loading product types to be displayed in the Change Quantity UI.
 *
 */

public class ChangeQuantityProductsPresenterTest {

    ChangeQuantityProductsPresenter presenter;
    ChangeQuantityProductsViewStub view;
    ProductTypeDAOMemory productTypeDAOMemory;


    /**
     * Sets up the test environment before each test method.
     *
     * Initializes in-memory data, creates a stub view, and instantiates
     * the presenter with the DAO and view.
     */

    @Before
    public void setUp() {

        //Prepares initial test data
        MemoryInitializer.prepareData();

        productTypeDAOMemory = ProductTypeDAOMemory.getInstance();
        view = new ChangeQuantityProductsViewStub();
        presenter = new ChangeQuantityProductsPresenter(view, productTypeDAOMemory);
    }

    /**
     * Tests that the presenter correctly loads all available products.
     *
     * Verifies that the number of products returned by the presenter
     * matches the expected size of the in-memory DAO (20 in this case).
     */

    @Test
    public void loadProducts() {
        ArrayList<ProductType> products = presenter.loadProducts();
        assertEquals(20 , products.size());

    }
}