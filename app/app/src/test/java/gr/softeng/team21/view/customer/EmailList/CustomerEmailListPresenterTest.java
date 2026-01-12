package gr.softeng.team21.view.customer.EmailList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link CustomerEmailListPresenter} class.
 * These tests verify the logic for retrieving the email inbox, composing new messages,
 * and handling email navigation.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListPresenterTest {

    private CustomerEmailListPresenter presenter;
    private CustomerEmailListViewStub view;
    private Customer customer;

    /**
     * Sets up the test environment before each test case.
     * Initializes in-memory data, a view stub and the presenter, retrieves a test customer,adds a test email to their inbox,

     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerEmailListViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-500");

        EmailMessage testMsg = new EmailMessage(
                new EmailAddress("sender@test.com"),
                customer.getEmailAddress(),
                "Test Subject",
                "Test Body",
                new Date()
        );

        customer.getEmailProvider().saveInboxEmails(testMsg);
        presenter = new CustomerEmailListPresenter(view, CustomerDAOMemory.getInstance());
    }

    /**
     * Test that the presenter correctly retrieves the inbox emails for a valid customer.
     */
    @Test
    public void getInboxReturnsCorrectEmails() {
        ArrayList<EmailMessage> result = presenter.getInbox("CUST-500");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test Subject", result.get(0).getSubject());
    }

    /**
     * Test that selecting to create a new message triggers the correct navigation method
     * in the view with the correct customer ID.
     */
    @Test
    public void onCreateNewMsgSelectedNavigatesWithCorrectId() {
        // Note: Corrected method name to match Presenter definition
        presenter.onCreateNewMsgSelectedClicked("CUST-500");

        Assert.assertEquals(1, view.getCreateNewMsgCount());
        Assert.assertEquals(customer.getCustomer_id(), view.getPassedCustomerId());
    }

    /**
     * Test that selecting an email marks it as read and triggers navigation to the
     * email details screen with the correct data.
     */
    @Test
    public void onEmailSelectedMarksAsReadAndNavigates() {
        EmailMessage email = customer.getEmailProvider().getInboxEmails().get(0);
        presenter.onEmailSelected(email, customer.getCustomer_id());
        Assert.assertTrue(email.isRead());

        Assert.assertEquals(1, view.getEmailDetailsCount());
        Assert.assertEquals("Test Subject", view.getDetailsSubject());
        Assert.assertEquals("Test Body", view.getDetailsBody());
        Assert.assertEquals("CUST-500", view.getDetailsId());
        Assert.assertEquals("sender@test.com", view.getDetailsSender());
        Assert.assertEquals(customer.getEmailAddress().toString(), view.getDetailsReceiver());
    }

    /**
     * Test that the presenter returns an empty list (and handles null safely)
     * when the customer ID provided does not exist.
     */
    @Test
    public void getInboxReturnsEmptyListWhenCustomerNotFound() {
        ArrayList<EmailMessage> result = presenter.getInbox("INVALID-ID-999");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }
}