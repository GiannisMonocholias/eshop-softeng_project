package gr.softeng.team21.view.customer.Payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class CustomerCardPaymentPresenterTest {
    private CustomerCardPaymentViewStub view;
    private CustomerCardPaymentPresenter presenter;
    private Customer customer;
    private String cardNumber;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerCardPaymentViewStub();
        customer = MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");
        presenter = new CustomerCardPaymentPresenter(view, customer);
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-020");
        ShoppingCart cart = new ShoppingCart(customer);
        customer.addItemToCart(product, 1);
        cardNumber="1234-5678-9012-3456";
    }

    @Test
    public void cardPaymentClicked() {
        presenter.CardPaymentClicked(cardNumber);
        Assert.assertEquals(new BigDecimal("679.0"), view.getConfirmationAmount().getAmount());

    }

    @Test
    public void confirmClicked() {
        presenter.CardPaymentClicked(cardNumber);
        presenter.ConfirmClicked();
        Assert.assertEquals(1, view.getHomePageCount());
        Assert.assertEquals("Η παραγγελία σας καταχωρήθυκε.", view.getMessage());

    }

    @Test
    public void cancelClicked() {
        presenter.CardPaymentClicked(cardNumber);
        presenter.CancelClicked();
        Assert.assertEquals(1, view.getHomePageCount());
        Assert.assertEquals("Η παραγγελία σας ακυρώθηκε.", view.getMessage());
    }
    @Test
    public void testWithNullArguments(){
        presenter.CardPaymentClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε τον αριθμό κάρτας!",view.getMessage());

        presenter.ConfirmClicked();
        Assert.assertEquals(0, view.getHomePageCount());
        presenter.CancelClicked();
        Assert.assertEquals(0, view.getHomePageCount());
        Assert.assertEquals("Σφάλμα: Order cannot be null!!!", view.getMessage());

        customer.setShoppingCart(null);
        presenter.CardPaymentClicked(cardNumber);
        Assert.assertEquals("Το καλάθι είναι άδειο!", view.getMessage());

    }
}