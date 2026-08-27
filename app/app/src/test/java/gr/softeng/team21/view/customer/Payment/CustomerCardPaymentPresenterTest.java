package gr.softeng.team21.view.customer.Payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.OrderDAOMemory;

/**
 * Unit tests for the {@link CustomerCardPaymentPresenter} class.
 * Verifies card payment processing logic, order confirmation, cancellation,
 * and input validation using In-Memory DAOs and CompletableFuture resolution.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentPresenterTest {

    private CustomerCardPaymentViewStub view;
    private CustomerCardPaymentPresenter presenter;
    private Customer customer;
    private String cardNumber;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerCardPaymentViewStub();

        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        OrderDAO orderDAO = OrderDAOMemory.getInstance();

        // Asynchronous resolution logic wrapped for tests via .join()
        customer = customerDAO.getCustomer("CUST-500").join();
        ProductType product = MemoryInitializer.getProductTypeDAO().getProduct("TECH-020").join();

        ShoppingCart cart = new ShoppingCart(customer);
        customer.addItemToCart(product, 1);

        cardNumber = "1234-5678-9012-3456";

        presenter = new CustomerCardPaymentPresenter(view, customerDAO, orderDAO);
        presenter.loadInitialData("CUST-500");
    }

    @Test
    public void cardPaymentClickedProceedsWithValidCard() {
        presenter.CardPaymentClicked(cardNumber);
        Assert.assertNotNull(view.getConfirmationAmount());
        Assert.assertEquals(new BigDecimal("679.0"), view.getConfirmationAmount().getAmount());
    }

    @Test
    public void confirmClickedFinalizesOrderAndNavigates() {
        presenter.CardPaymentClicked(cardNumber);
        presenter.ConfirmClicked();

        Assert.assertEquals(1, view.getHomePageCount());
        Assert.assertEquals("Η παραγγελία σας καταχωρήθυκε.", view.getMessage());
    }

    @Test
    public void cancelClickedCancelsOrderAndNavigates() {
        presenter.CardPaymentClicked(cardNumber);
        presenter.CancelClicked();

        Assert.assertEquals(1, view.getHomePageCount());
        Assert.assertEquals("Η παραγγελία σας ακυρώθηκε.", view.getMessage());
    }

    @Test
    public void testWithNullArgumentsAndEmptyCart() {
        presenter.CardPaymentClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε τον αριθμό κάρτας!", view.getMessage());

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