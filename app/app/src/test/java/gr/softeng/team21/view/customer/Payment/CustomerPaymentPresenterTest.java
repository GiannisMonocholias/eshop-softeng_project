package gr.softeng.team21.view.customer.Payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class CustomerPaymentPresenterTest {
    private CustomerPaymentPresenter presenter;
    private CustomerPaymentViewStub view;
    private Customer customer;
    private boolean payWithCash;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerPaymentViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-501");
        presenter = new CustomerPaymentPresenter(view, customer);
        ProductType product=MemoryInitializer.getProductTypeDAO().getProduct("TECH-020");
        ShoppingCart cart = new ShoppingCart(customer);
        customer.addItemToCart(product,1);
        payWithCash = true;
    }

    @Test
    public void paymentClicked() {
        presenter.paymentClicked(payWithCash);
        Assert.assertEquals(0, view.getCardPaymentCount());
        Assert.assertEquals(new BigDecimal("679.0"), view.getConfirmationAmount().getAmount());
        payWithCash = false;
        presenter.paymentClicked(payWithCash);
        Assert.assertEquals(1, view.getCardPaymentCount());
    }

    @Test
    public void confirmClicked() {
        presenter.paymentClicked(payWithCash);
        presenter.ConfirmClicked();
        Assert.assertEquals("Η παραγγελία σας καταχωρήθυκε.", view.getMessage());
        Assert.assertEquals(1, view.getHomePageCount());
    }

    @Test
    public void cancelClicked() {
        presenter.paymentClicked(payWithCash);
        presenter.CancelClicked();
        Assert.assertEquals("Η παραγγελία σας ακυρώθηκε.", view.getMessage());
        Assert.assertEquals(1, view.getHomePageCount());

    }

    @Test
    public void setpaymentClicked() {
        String amount = "1000 €";
        presenter.setpaymentClicked(amount);
        Assert.assertEquals(amount, view.getTotalAmount());
    }

    @Test
    public void setShippingDetails() {
        Address addr2 = new Address("Τσιμισκή", "42", "Θεσσαλονίκη", "Ελλάδα", "54623");
        presenter.loadShippingDetails();
        Assert.assertEquals("Γιώργος Παπαδόπουλος", view.getShippingName());
        Assert.assertEquals("6987659483", view.getShippingPhone());
        //  Assert.assertEquals(addr2, view.getShippingAddress());
    }

    @Test
    public void testWithNullArgumnets() {
        presenter.ConfirmClicked();
        Assert.assertEquals(0, view.getHomePageCount());
        presenter.CancelClicked();
        Assert.assertEquals(0, view.getHomePageCount());
        Assert.assertEquals("Σφάλμα: Order cannot be null!!!", view.getMessage());

        customer.setShoppingCart(null);
        presenter.paymentClicked(true);
        Assert.assertEquals("Το καλάθι είναι άδειο!", view.getMessage());
    }


}