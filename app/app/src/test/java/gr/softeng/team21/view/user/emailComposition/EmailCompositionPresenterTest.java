package gr.softeng.team21.view.user.emailComposition;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class EmailCompositionPresenterTest {

    private EmailCompositionPresenter presenter;
    private EmailCompositionViewStub viewStub;
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String CUSTOMER_ID = "CUST-500";

    private Employee employee;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new EmailCompositionViewStub();
        presenter = new EmailCompositionPresenter(
                viewStub,
                CustomerDAOMemory.getInstance(),
                EmployeeDAOMemory.getInstance()
        );

        employee = EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);
        customer = CustomerDAOMemory.getInstance().getCustomer(CUSTOMER_ID);
    }


    @Test
    public void onViewCreatedEmployeeSenderLoadsCorrectDetails() {
        presenter.onViewCreated(EMPLOYEE_ID);

        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getDisplayedSenderName());
        Assert.assertEquals(employee.getEmailAddress().toString(), viewStub.getDisplayedSenderEmail());
    }

    @Test
    public void onViewCreatedCustomerSenderLoadsCorrectDetails() {
        presenter.onViewCreated(CUSTOMER_ID);

        Assert.assertEquals("Νίκος Γεωργίου", viewStub.getDisplayedSenderName());
        Assert.assertEquals(customer.getEmailAddress().toString(), viewStub.getDisplayedSenderEmail());
    }

    @Test
    public void onViewCreatedInvalidUserShowsErrorAndFinishes() {
        presenter.onViewCreated("INVALID_ID");

        Assert.assertTrue(viewStub.getErrorMessage().contains("δεν βρέθηκε"));
        Assert.assertTrue(viewStub.isFinishActivityCalled());
    }

    @Test
    public void onSendClickedEmptyFieldsShowsError() {
        presenter.onViewCreated(EMPLOYEE_ID);

        viewStub.setRecipientEmailInput("");
        viewStub.setSubjectInput("Subject");
        viewStub.setBodyInput("Body");

        presenter.onSendClicked();

        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία.", viewStub.getErrorMessage());
    }

    @Test
    public void onSendClickedRecipientNotFoundShowsError() {
        presenter.onViewCreated(EMPLOYEE_ID);

        viewStub.setRecipientEmailInput("ghost@casper.com");
        viewStub.setSubjectInput("Hello");
        viewStub.setBodyInput("Body");

        presenter.onSendClicked();

        Assert.assertEquals("Δεν βρέθηκε χρήστης με αυτό το email.", viewStub.getErrorMessage());
    }

    @Test
    public void onSendClickedEmployeeToCustomerSuccess() {
        presenter.onViewCreated(EMPLOYEE_ID);
        int initialCustomerInboxSize = customer.getEmailProvider().getInboxEmails().size();

        viewStub.setRecipientEmailInput(customer.getEmailAddress().toString());
        viewStub.setSubjectInput("Order Update");
        viewStub.setBodyInput("Your order is ready.");

        presenter.onSendClicked();

        Assert.assertEquals("Το μήνυμα εστάλη!", viewStub.getSuccessMessage());
        Assert.assertTrue(viewStub.isFinishActivityCalled());


        Assert.assertEquals(initialCustomerInboxSize + 1, customer.getEmailProvider().getInboxEmails().size());
        Assert.assertEquals("Order Update", customer.getEmailProvider().getInboxEmails().get(initialCustomerInboxSize).getSubject());
    }

    @Test
    public void onSendClickedCustomerToEmployeeSuccess() {
        presenter.onViewCreated(CUSTOMER_ID);
        int initialEmployeeInboxSize = employee.getEmailProvider().getInboxEmails().size();

        viewStub.setRecipientEmailInput(employee.getEmailAddress().toString());
        viewStub.setSubjectInput("Help Needed");
        viewStub.setBodyInput("I have a question.");

        presenter.onSendClicked();


        Assert.assertEquals(initialEmployeeInboxSize + 1, employee.getEmailProvider().getInboxEmails().size());
        Assert.assertEquals("Help Needed", employee.getEmailProvider().getInboxEmails().get(initialEmployeeInboxSize).getSubject());
    }
}