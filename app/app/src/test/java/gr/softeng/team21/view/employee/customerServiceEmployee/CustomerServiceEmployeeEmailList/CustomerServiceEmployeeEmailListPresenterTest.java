package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListPresenter;

/**
 * Unit tests for {@link CustomerServiceEmployeeEmailListPresenter}.
 * This suite ensures that the inbox logic for customer service employees functions correctly,
 * covering asynchronous message retrieval, marking emails as read, and navigating to message details.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListPresenterTest {

    private CustomerServiceEmployeeEmailListPresenter presenter;
    private CustomerServiceEmployeeEmailListViewStub viewStub;
    private CustomerServiceEmployee csr1;
    private CustomerServiceEmployee csr2;

    /**
     * Initializes data and prepares the testing environment before each test.
     * Sets up memory DAOs, a view stub, and adds a test email to the target employee's inbox.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new CustomerServiceEmployeeEmailListViewStub();

        csr1 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee("CSR-101").join();
        csr2 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee("CSR-102").join();

        EmailMessage testMsg = new EmailMessage(csr2.getEmailAddress(), csr1.getEmailAddress(),
                "Test Subject", "Test Body", new Date()
        );
        csr1.getEmailProvider().saveInboxEmails(testMsg);

        // Inject the memory DAO into the presenter for testing
        presenter = new CustomerServiceEmployeeEmailListPresenter(viewStub, EmployeeDAOMemory.getInstance());
    }

    /**
     * Verifies that the presenter correctly retrieves the list of inbox emails
     * for a specific employee ID and updates the view asynchronously.
     */
    @Test
    public void loadInboxUpdatesViewWithCorrectEmails() {
        presenter.loadInbox("CSR-101");

        ArrayList<EmailMessage> result = viewStub.getLoadedEmails();
        Assert.assertNotNull(result);

        // 1 message added from the setup method
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test Subject", result.get(0).getSubject());
    }

    /**
     * Tests if selecting the "Create New Message" option triggers
     * navigation with the correct employee ID.
     */
    @Test
    public void onCreateNewMsgSelectedNavigatesWithCorrectId() {
        presenter.onCreateNewMsgSelected("CSR-101");

        Assert.assertEquals(1, viewStub.getNavigateToCreateNewMsgCount());
        Assert.assertEquals(csr1.getEmployeeId(), viewStub.getPassedEmployeeId());
    }

    /**
     * Verifies the full workflow when an email is selected:
     * 1. The email is marked as read.
     * 2. The view navigates to the email details screen with correct metadata.
     */
    @Test
    public void onEmailSelectedMarksAsReadAndNavigates() {
        EmailMessage email = csr1.getEmailProvider().getInboxEmails().get(0);

        presenter.onEmailSelected(email, csr1.getEmployeeId());

        Assert.assertTrue(email.isRead());
        Assert.assertEquals(1, viewStub.getNavigateToEmailDetailsCount());
        Assert.assertEquals("Test Subject", viewStub.getDetailsSubject());
        Assert.assertEquals("Test Body", viewStub.getDetailsBody());
        Assert.assertEquals("CSR-101", viewStub.getDetailsId());
        Assert.assertEquals(csr2.getEmailAddress().toString(), viewStub.getDetailsSender());
        Assert.assertEquals(csr1.getEmailAddress().toString(), viewStub.getDetailsReceiver());
    }

    /**
     * Clears the employee's inbox after each test to ensure state isolation.
     */
    @After
    public void tearDownTest(){
        csr1.getEmailProvider().getInboxEmails().clear();
    }
}