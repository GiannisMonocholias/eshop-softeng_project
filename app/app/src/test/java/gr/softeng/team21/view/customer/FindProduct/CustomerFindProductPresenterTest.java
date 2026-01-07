package gr.softeng.team21.view.customer.FindProduct;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class CustomerFindProductPresenterTest {
    private CustomerFindProductPresenter presenter;
    private CustomerFindProductViewStub view;
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view=new CustomerFindProductViewStub();
        presenter=new CustomerFindProductPresenter(view);
    }

    @Test
    public void loadList() {
        presenter.loadList();
        Assert.assertEquals(1,view.getShowProductsCount());
        Assert.assertNotNull(view.getShowedProducts());
    }

    @Test
    public void filter() {
        // 1. Δοκιμή φίλτρου που επιστρέφει αποτελέσματα
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

    @Test
    public void productClicked() {
        ProductType product=MemoryInitializer.getProductTypeDAO().getProduct("TECH-001");
        presenter.ProductClicked(product);
        Assert.assertEquals("TECH-001",view.getProductCode());
    }
    @Test
    public void productClickedWithNullProduct() {
        presenter.ProductClicked(null);
        Assert.assertNull(view.getProductCode());
    }
}