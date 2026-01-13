package gr.softeng.team21.view.customer.homePage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for the {@link CustomerHomePagePresenter} class.
 * These tests verify the navigation logic for  menu's options (edit data, logout, find product, inbox)
 * and the account deletion process.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePagePresenterTest {
    private CustomerHomePagePresenter presenter;
    private Customer customer;
    private CustomerHomePageViewStub view;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data, a view stub and the presenter, retrieves a test customer.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerHomePageViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-500");
        presenter = new CustomerHomePagePresenter(view, customer);
    }

    /**
     * Test that clicking on "Edit Data" triggers the correct navigation method in the view.
     */
    @Test
    public void editDataClicked() {
        presenter.EditDataClicked();
        Assert.assertEquals(1, view.getEditDataCount());
    }

    /**
     * Test that clicking "Logout" triggers the navigation back to the Login .
     */
    @Test
    public void logoutClicked() {
        presenter.LogoutClicked();
        Assert.assertEquals(1, view.getLoginCount());
    }

    /**
     * Test that clicking "Find Product" triggers the navigation to the product search screen.
     */
    @Test
    public void findProductClicked() {
        presenter.FindProductClicked();
        Assert.assertEquals(1, view.getFindProductCount());
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
     * Test the account deletion confirmation process.
     * Checks if the customer is removed from the DAO, a success message is shown,
     * and the user is redirected to the login screen.
     */
    @Test
    public void deleteConfirm() {
        Assert.assertNotNull(CustomerDAOMemory.getInstance().getCustomer("CUST-500"));
        presenter.DeleteConfirm();
        Assert.assertEquals("Ο λογαριασμός σας διαγράφηκε.", view.getMessage());
        Assert.assertEquals(1, view.getLoginCount());
        Assert.assertNull(CustomerDAOMemory.getInstance().getCustomer("CUST-500"));
    }

    /**
     * Test that clicking "Inbox" triggers the navigation to the email list screen.
     */
    @Test
    public void inboxClicked() {
        presenter.InboxClicked();
        Assert.assertEquals(1, view.getInboxCount());
    }

    /**
     * Test that you are trying to confirm the delete with a null customer object
     * andit does not navigate to Login
     */
    @Test
    public void deleteConfirmWithNullCustomer() {
        CustomerHomePagePresenter nullpresenter = new CustomerHomePagePresenter(view, null);
        nullpresenter.DeleteConfirm();
        Assert.assertEquals(0, view.getLoginCount());
    }
}