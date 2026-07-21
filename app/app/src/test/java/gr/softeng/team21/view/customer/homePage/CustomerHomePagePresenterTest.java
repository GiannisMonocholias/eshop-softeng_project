package gr.softeng.team21.view.customer.homePage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit tests for the {@link CustomerHomePagePresenter} class.
 * These tests verify the navigation logic for menu's options (edit data, logout, find product, inbox)
 * and the asynchronous account deletion process using Dependency Injection.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePagePresenterTest {
    private CustomerHomePagePresenter presenter;
    private CustomerHomePageViewStub view;
    private CustomerDAO customerDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data, DAOs, a view stub and the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerHomePageViewStub();
        customerDAO = CustomerDAOMemory.getInstance();
        userCredentialsDAO = UserCredentialsDAOMemory.getInstance();

        // Presenter loads customer asynchronously during its instantiation
        presenter = new CustomerHomePagePresenter(view, "CUST-500", customerDAO, userCredentialsDAO);
    }

    /**
     * Test that clicking on "Edit Data" triggers the correct navigation method with the right ID.
     */
    @Test
    public void editDataClicked() {
        presenter.EditDataClicked();
        Assert.assertEquals(1, view.getEditDataCount());
        Assert.assertEquals("CUST-500", view.getPassedCustomerId());
    }

    /**
     * Test that clicking "Logout" triggers the navigation back to the Login.
     */
    @Test
    public void logoutClicked() {
        presenter.LogoutClicked();
        Assert.assertEquals(1, view.getLoginCount());
    }

    /**
     * Test that clicking "Find Product" triggers the navigation to the product search screen with the right ID.
     */
    @Test
    public void findProductClicked() {
        presenter.FindProductClicked();
        Assert.assertEquals(1, view.getFindProductCount());
        Assert.assertEquals("CUST-500", view.getPassedCustomerId());
    }

    /**
     * Test that clicking "Delete Account" triggers the confirmation dialog.
     */
    @Test
    public void deleteClicked() {
        presenter.DeleteClicked();
        Assert.assertEquals(1, view.getDeleteCount());
    }

    /**
     * Test the asynchronous account deletion confirmation process.
     * Checks if the customer is removed from the DAO, a success message is shown,
     * and the user is redirected to the login screen.
     */
    @Test
    public void deleteConfirm() {
        // Βεβαιωνόμαστε ότι ο πελάτης υπάρχει πριν τη διαγραφή (χρήση .join() για τη μνήμη)
        Assert.assertNotNull(customerDAO.getCustomer("CUST-500").join());

        presenter.DeleteConfirm();

        Assert.assertEquals("Ο λογαριασμός σας διαγράφηκε.", view.getMessage());
        Assert.assertEquals(1, view.getLoginCount());

        // Βεβαιωνόμαστε ότι ο πελάτης δεν υπάρχει πια
        Assert.assertNull(customerDAO.getCustomer("CUST-500").join());
    }

    /**
     * Test that clicking "Inbox" triggers the navigation to the email list screen with the right ID.
     */
    @Test
    public void inboxClicked() {
        presenter.InboxClicked();
        Assert.assertEquals(1, view.getInboxCount());
        Assert.assertEquals("CUST-500", view.getPassedCustomerId());
    }

    /**
     * Test initialization and delete action with an invalid/non-existent customer ID.
     */
    @Test
    public void deleteConfirmWithNullCustomer() {
        CustomerHomePageViewStub viewForNull = new CustomerHomePageViewStub();

        // Initialize with non-existent customer ID
        // Presenter will return to login page (LoginCount becomes 1)
        CustomerHomePagePresenter nullPresenter = new CustomerHomePagePresenter(viewForNull, "NON-EXISTENT", customerDAO, userCredentialsDAO);

        Assert.assertEquals(1, viewForNull.getLoginCount());

        // Try to delete, whereas the customer has not been loaded
        nullPresenter.DeleteConfirm();

        // LoginCount must remain 1, proving that DeleteConfirm cannot delete a null customer
        Assert.assertEquals(1, viewForNull.getLoginCount());
    }
}