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

public class CustomerEmailListPresenterTest {

    private  CustomerEmailListPresenter presenter;
    private CustomerEmailListViewStub view;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new CustomerEmailListViewStub();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-500");

        EmailMessage testMsg = new EmailMessage(
                new EmailAddress("sender@test.com"), // Sender
                customer.getEmailAddress(),          // Receiver (o πελάτης μας)
                "Test Subject",                      // Subject
                "Test Body",                         // Body
                new Date()
        );

        customer.getEmailProvider().saveInboxEmails(testMsg);
        presenter = new CustomerEmailListPresenter(view, CustomerDAOMemory.getInstance());
    }
        @Test
        public void getInboxReturnsCorrectEmails() {
            ArrayList<EmailMessage> result = presenter.getInbox("CUST-500");
            Assert.assertNotNull(result);
            Assert.assertEquals(1, result.size());
            Assert.assertEquals("Test Subject", result.get(0).getSubject());
        }
    @Test
    public void onCreateNewMsgSelectedNavigatesWithCorrectId() {
        presenter.onCreateNewMsgSelectedClicked("CUST-500");

        Assert.assertEquals(1, view.getCreateNewMsgCount());
        Assert.assertEquals(customer.getCustomer_id(), view.getPassedCustomerId());
    }

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
    @Test
    public void getInboxReturnsEmptyListWhenCustomerNotFound() {
        ArrayList<EmailMessage> result = presenter.getInbox("INVALID-ID-999");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }
}