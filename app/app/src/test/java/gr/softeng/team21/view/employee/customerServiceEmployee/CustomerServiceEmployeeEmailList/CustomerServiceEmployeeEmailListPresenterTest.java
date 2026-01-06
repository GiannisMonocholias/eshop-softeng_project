package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListViewStub;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListPresenter;

public class CustomerServiceEmployeeEmailListPresenterTest {

    private CustomerServiceEmployeeEmailListPresenter presenter;
    private CustomerServiceEmployeeEmailListViewStub viewStub;
    private CustomerServiceEmployee csr1;
    private CustomerServiceEmployee csr2;

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new CustomerServiceEmployeeEmailListViewStub();


        csr1 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee("CSR-101");
        csr2 = (CustomerServiceEmployee) EmployeeDAOMemory.getInstance().getEmployee("CSR-102");
        EmailMessage testMsg = new EmailMessage(csr2.getEmailAddress(),csr1.getEmailAddress(),
                "Test Subject", "Test Body", new Date()
        );
        csr1.getEmailProvider().saveInboxEmails(testMsg);

        presenter = new CustomerServiceEmployeeEmailListPresenter(viewStub, EmployeeDAOMemory.getInstance());
    }

    @Test
    public void getInboxReturnsCorrectEmails() {
        ArrayList<EmailMessage> result = presenter.getInbox("CSR-101");


        Assert.assertNotNull(result);

        // 1 message added from the setup method
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

    @After
    public void tearDownTest(){
        csr1.getEmailProvider().getInboxEmails().clear();
    }
}