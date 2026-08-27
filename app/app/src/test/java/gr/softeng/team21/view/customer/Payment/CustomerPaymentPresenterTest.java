package gr.softeng.team21.view.customer.Payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for the {@link CustomerPaymentPresenter} class.
 * Tests payment method selection (Cash/Card), order confirmation/cancellation,
 * shipping detail presentation, and null handling using In-Memory DAO fakes.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentPresenterTest {
    private CustomerPaymentPresenter presenter;
    private CustomerPaymentViewStub view;
    private Customer customer;
    private boolean payWithCash;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerPaymentViewStub();

        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        OrderDAO orderDAO = OrderDAOMemory.getInstance();

        customer = customerDAO.getCustomer("CUST-501").join();
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-020").join();
        customer.addItemToCart(product, 1);

        presenter = new CustomerPaymentPresenter(view, customerDAO, orderDAO);
        presenter.loadInitialData("CUST-501");

        payWithCash = true;
    }

    @Test
    public void paymentClicked() {
        presenter.paymentClicked(payWithCash);
        Assert.assertEquals(0, view.getCardPaymentCount());
        Assert.assertNotNull(view.getConfirmationAmount());
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
        presenter.loadShippingDetails();
        Assert.assertEquals("Γιώργος Παπαδόπουλος", view.getShippingName());
        Assert.assertEquals("6987659483", view.getShippingPhone());
        Assert.assertEquals("Τσιμισκή 42, Θεσσαλονίκη, 54623, Ελλάδα", view.getShippingAddress());
    }

    @Test
    public void testWithNullArguments() {
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