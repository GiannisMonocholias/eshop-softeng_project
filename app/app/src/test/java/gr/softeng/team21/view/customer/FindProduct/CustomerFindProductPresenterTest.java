package gr.softeng.team21.view.customer.FindProduct;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

public class CustomerFindProductPresenterTest {
    private CustomerFindProductPresenter presenter;
    private CustomerFindProductViewStub view;
    private Customer customer;
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view=new CustomerFindProductViewStub();
        customer= CustomerDAOMemory.getInstance().getCustomer("CUST-500");
        presenter=new CustomerFindProductPresenter(view,customer);
    }

    @Test
    public void loadList() {
        presenter.loadList();
        Assert.assertEquals(1,view.getShowProductsCount());
        Assert.assertNotNull(view.getShowedProducts());
    }

    @Test
    public void filter() {
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
    @Test
    public void openShoppingCartClicked(){
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-010"),1 );
        presenter.openShoppingCartClicked();
        Assert.assertEquals(1, view.getShoppingCartCount());
    }
    @Test
    public void openShoppingCartClickedWithNullShoppingCart(){
        presenter.openShoppingCartClicked();
        Assert.assertEquals(0, view.getShoppingCartCount());
        Assert.assertEquals("To καλάθι είναι άδειο!!",view.getEmptyShoppingCartMessage());
    }

    @Test
    public void updateShoppingCartStatus(){
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-010"),1 );
        customer.addItemToCart(ProductTypeDAOMemory.getInstance().getProduct("TECH-011"),1 );
        presenter.updateShoppingCartStatus();
        Assert.assertEquals(2,view.getUpdateShoppingCartCount());

    }
    @Test public void updateShoppingCartStatusWithNullShoppingCart(){
        presenter.updateShoppingCartStatus();
        Assert.assertEquals(0,view.getUpdateShoppingCartCount());
    }
}