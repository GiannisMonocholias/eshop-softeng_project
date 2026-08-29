package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListPresenter;

/**
 * Unit tests for {@link CustomerServiceEmployeeEmailListPresenter}.
 * Ensures that the inbox logic functions correctly, covering asynchronous
 * message retrieval strictly via the EmailDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListPresenterTest {

    private CustomerServiceEmployeeEmailListPresenter presenter;
    private CustomerServiceEmployeeEmailListViewStub viewStub;
    private EmailDAO emailDAO;
    private CustomerServiceEmployee csr1;
    private CustomerServiceEmployee csr2;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new CustomerServiceEmployeeEmailListViewStub();
        emailDAO = new EmailDAOMemory();
        EmployeeDAO employeeDAO = EmployeeDAOMemory.getInstance();

        csr1 = (CustomerServiceEmployee) employeeDAO.getEmployee("CSR-101").join();
        csr2 = (CustomerServiceEmployee) employeeDAO.getEmployee("CSR-102").join();

        EmailMessage testMsg = new EmailMessage(csr2.getEmailAddress(), csr1.getEmailAddress(), "Test Subject", "Test Body", new Date());

        // Save the test email directly to the DAO
        emailDAO.saveInboxEmails(testMsg).join();

        presenter = new CustomerServiceEmployeeEmailListPresenter(viewStub, employeeDAO, emailDAO);
    }

    @Test
    public void loadInboxUpdatesViewWithCorrectEmails() {
        presenter.loadInbox("CSR-101");

        ArrayList<EmailMessage> result = viewStub.getLoadedEmails();
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test Subject", result.get(0).getSubject());
    }

    @Test
    public void onCreateNewMsgSelectedNavigatesWithCorrectId() {
        presenter.onCreateNewMsgSelected("CSR-101");
        Assert.assertEquals(1, viewStub.getNavigateToCreateNewMsgCount());
        Assert.assertEquals(csr1.getEmployeeId(), viewStub.getPassedEmployeeId());
    }

    @Test
    public void onEmailSelectedMarksAsReadAndNavigates() {
        EmailMessage email = emailDAO.getInboxEmails().join().get(0);
        presenter.onEmailSelected(email, csr1.getEmployeeId());

        Assert.assertTrue(email.isRead());
        Assert.assertEquals(1, viewStub.getNavigateToEmailDetailsCount());
        Assert.assertEquals("Test Subject", viewStub.getDetailsSubject());
    }
}