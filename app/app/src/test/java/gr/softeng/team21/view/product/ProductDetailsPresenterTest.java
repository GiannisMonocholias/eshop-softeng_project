package gr.softeng.team21.view.product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class ProductDetailsPresenterTest {

    private ProductDetailsPresenter presenter;
    private ProductDetailsViewStub view;
    private Customer customer;
    private ShoppingCart cart;
    private ProductType product;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-503");
        cart=new ShoppingCart(customer);
        view = new ProductDetailsViewStub();
        presenter = new ProductDetailsPresenter(view, customer);
        product=MemoryInitializer.getProductTypeDAO().getProduct("TECH-020");
    }

    @Test
    public void plusClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.plusClicked();
        presenter.plusClicked();
        Assert.assertEquals(3, view.getQuantity());

    }

    @Test
    public void minusClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.minusClicked();
        Assert.assertEquals(1, view.getQuantity());
        presenter.plusClicked();
        presenter.minusClicked();
        Assert.assertEquals(1, view.getQuantity());

    }

    @Test
    public void addToCartClicked() {
        presenter.loadProduct(product.getProductCode());
        presenter.plusClicked();
        presenter.addToCartClicked();
        Assert.assertEquals(1,view.getAddToCartCount());
        Assert.assertEquals(1, customer.getShoppingCart().getItems().size());
    }

    @Test
    public void openShoppingCartClicked() {
        presenter.openShoppingCartClicked();
        Assert.assertEquals(1,view.getCartCount());
        Assert.assertEquals("Μετάβαση στο Καλάθι...", view.getMessage());
    }

    @Test
    public void loadProduct() {
        presenter.loadProduct(product.getProductCode());
        Assert.assertEquals("TECH-020", view.getCode());

    }
    @Test
    public void loadProductWithNullArguments() {
        presenter.loadProduct(null);
        Assert.assertEquals(1, view.getQuantity());

        presenter.loadProduct("TECH-150");
        Assert.assertEquals(1, view.getQuantity());


    }

    @Test
    public void addToCartClickedWithNullProduct() {
        presenter.addToCartClicked();
        Assert.assertNotNull(view.getMessage());
        Assert.assertEquals(0, view.getAddToCartCount());

    }
}