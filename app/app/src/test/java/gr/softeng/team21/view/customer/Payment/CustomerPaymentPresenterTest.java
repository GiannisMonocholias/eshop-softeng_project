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

/**
 * Unit tests for the {@link CustomerPaymentPresenter} class.
 * These tests verify the logic for selecting payment methods (Cash/Card), confirming/cancelling orders,
 * displaying totals and shipping details, and handling edge cases.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentPresenterTest {
    private CustomerPaymentPresenter presenter;
    private CustomerPaymentViewStub view;
    private Customer customer;
    private boolean payWithCash;

    /**
     * Sets up the test environment before each test case.
     *Initializes in-memory data, a view stub and the presenter, retrieves a test customer
     *  and sets up a cart with one product.
     */
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

    /**
     * Test the payment method selection logic.
     * Checks if selecting Cash shows the confirmation dialog with the correct amount,
     * and if selecting Card navigates to the card payment screen.
     */
    @Test
    public void paymentClicked() {
        presenter.paymentClicked(payWithCash);
        Assert.assertEquals(0, view.getCardPaymentCount());
        Assert.assertEquals(new BigDecimal("679.0"), view.getConfirmationAmount().getAmount());

        payWithCash = false;
        presenter.paymentClicked(payWithCash);
        Assert.assertEquals(1, view.getCardPaymentCount());
    }

    /**
     * Test that confirming the order finalizes it and navigates to the home page.
     */
    @Test
    public void confirmClicked() {
        presenter.paymentClicked(payWithCash);
        presenter.ConfirmClicked();
        Assert.assertEquals("Η παραγγελία σας καταχωρήθυκε.", view.getMessage());
        Assert.assertEquals(1, view.getHomePageCount());
    }

    /**
     * Test that cancelling the order stops the process and navigates to the home page.
     */
    @Test
    public void cancelClicked() {
        presenter.paymentClicked(payWithCash);
        presenter.CancelClicked();
        Assert.assertEquals("Η παραγγελία σας ακυρώθηκε.", view.getMessage());
        Assert.assertEquals(1, view.getHomePageCount());

    }

    /**
     * Test that the total amount is correctly passed to the view for display.
     */
    @Test
    public void setpaymentClicked() {
        String amount = "1000 €";
        presenter.setpaymentClicked(amount);
        Assert.assertEquals(amount, view.getTotalAmount());
    }

    /**
     * Test that shipping details (name, phone) are correctly loaded and displayed in the view.
     */
    @Test
    public void setShippingDetails() {
        Address addr2 = new Address("Τσιμισκή", "42", "Θεσσαλονίκη", "Ελλάδα", "54623");
        presenter.loadShippingDetails();
        Assert.assertEquals("Γιώργος Παπαδόπουλος", view.getShippingName());
        Assert.assertEquals("6987659483", view.getShippingPhone());
        //  Assert.assertEquals(addr2, view.getShippingAddress());
    }

    /**
     * Test the presenter's behavior with null arguments or confirming/cancelling without an active order,
     * attempting payment with a null shopping cart.
     */
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