package gr.softeng.team21.view.admin.changeQuantity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

public class ChangeQuantityProductsPresenterTest {

    ChangeQuantityProductsPresenter presenter;
    ChangeQuantityProductsViewStub view;
    ProductTypeDAOMemory productTypeDAOMemory;

    @Before
    public void setUp() {

        MemoryInitializer.prepareData();

        productTypeDAOMemory = ProductTypeDAOMemory.getInstance();
        view = new ChangeQuantityProductsViewStub();
        presenter = new ChangeQuantityProductsPresenter(view, productTypeDAOMemory);
    }

    @Test
    public void loadProducts() {
        ArrayList<ProductType> products = presenter.loadProducts();
        assertEquals(20 , products.size());

    }
}