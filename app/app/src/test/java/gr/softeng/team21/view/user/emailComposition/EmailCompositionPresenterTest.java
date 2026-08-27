package gr.softeng.team21.view.user.emailComposition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for {@link EmailCompositionPresenter}.
 * This suite verifies the logic for preparing the email composition screen and
 * the process of sending emails between different types of users (Employees and Customers).
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionPresenterTest {

    private EmailCompositionPresenter presenter;
    private EmailCompositionViewStub viewStub;
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String CUSTOMER_ID = "CUST-500";

    private Employee employee;
    private Customer customer;

    /**
     * Prepares data and initializes the presenter and dependencies before each test.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new EmailCompositionViewStub();
        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        EmployeeDAO employeeDAO = EmployeeDAOMemory.getInstance();

        presenter = new EmailCompositionPresenter(viewStub, customerDAO, employeeDAO);

        // Fetch asynchronously using .join() for testing environment
        employee = employeeDAO.getEmployee(EMPLOYEE_ID).join();
        customer = customerDAO.getCustomer(CUSTOMER_ID).join();
    }

    /**
     * Verifies that when an employee opens the composition screen, their details
     * (name and email) are loaded correctly into the view.
     */
    @Test
    public void onViewCreatedEmployeeSenderLoadsCorrectDetails() {
        presenter.onViewCreated(EMPLOYEE_ID);

        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getDisplayedSenderName());
        Assert.assertEquals(employee.getEmailAddress().toString(), viewStub.getDisplayedSenderEmail());
    }

    /**
     * Verifies that when a customer opens the composition screen, their details
     * are loaded correctly into the view.
     */
    @Test
    public void onViewCreatedCustomerSenderLoadsCorrectDetails() {
        presenter.onViewCreated(CUSTOMER_ID);

        Assert.assertEquals("Νίκος Γεωργίου", viewStub.getDisplayedSenderName());
        Assert.assertEquals(customer.getEmailAddress().toString(), viewStub.getDisplayedSenderEmail());
    }

    /**
     * Verifies that providing an invalid user ID triggers an error message
     * and closes the activity.
     */
    @Test
    public void onViewCreatedInvalidUserShowsErrorAndFinishes() {
        presenter.onViewCreated("INVALID_ID");

        Assert.assertTrue(viewStub.getErrorMessage().contains("δεν βρέθηκε"));
        Assert.assertTrue(viewStub.isFinishActivityCalled());
    }

    /**
     * Verifies that attempting to send an email with empty required fields
     * triggers a validation error message.
     */
    @Test
    public void onSendClickedEmptyFieldsShowsError() {
        presenter.onViewCreated(EMPLOYEE_ID);

        viewStub.setRecipientEmailInput("");
        viewStub.setSubjectInput("Subject");
        viewStub.setBodyInput("Body");

        presenter.onSendClicked();

        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία.", viewStub.getErrorMessage());
    }

    /**
     * Verifies that an error is shown if the recipient's email address
     * does not exist in the system.
     */
    @Test
    public void onSendClickedRecipientNotFoundShowsError() {
        presenter.onViewCreated(EMPLOYEE_ID);

        viewStub.setRecipientEmailInput("ghost@casper.com");
        viewStub.setSubjectInput("Hello");
        viewStub.setBodyInput("Body");

        presenter.onSendClicked();

        Assert.assertEquals("Δεν βρέθηκε χρήστης με αυτό το email.", viewStub.getErrorMessage());
    }

    /**
     * Verifies successful email delivery from an Employee to a Customer.
     * Checks for the success message, activity termination, and inbox update.
     */
    @Test
    public void onSendClickedEmployeeToCustomerSuccess() {
        presenter.onViewCreated(EMPLOYEE_ID);
        int initialCustomerInboxSize = customer.getEmailProvider().getInboxEmails().join().size();

        viewStub.setRecipientEmailInput(customer.getEmailAddress().toString());
        viewStub.setSubjectInput("Order Update");
        viewStub.setBodyInput("Your order is ready.");

        presenter.onSendClicked();

        Assert.assertEquals("Το μήνυμα εστάλη!", viewStub.getSuccessMessage());
        Assert.assertTrue(viewStub.isFinishActivityCalled());

        Assert.assertEquals(initialCustomerInboxSize + 1, customer.getEmailProvider().getInboxEmails().join().size());
        Assert.assertEquals("Order Update", customer.getEmailProvider().getInboxEmails().join().get(initialCustomerInboxSize).getSubject());
    }

    /**
     * Verifies successful email delivery from a Customer to an Employee.
     */
    @Test
    public void onSendClickedCustomerToEmployeeSuccess() {
        presenter.onViewCreated(CUSTOMER_ID);
        int initialEmployeeInboxSize = employee.getEmailProvider().getInboxEmails().join().size();

        viewStub.setRecipientEmailInput(employee.getEmailAddress().toString());
        viewStub.setSubjectInput("Help Needed");
        viewStub.setBodyInput("I have a question.");

        presenter.onSendClicked();

        Assert.assertEquals(initialEmployeeInboxSize + 1, employee.getEmailProvider().getInboxEmails().join().size());
        Assert.assertEquals("Help Needed", employee.getEmailProvider().getInboxEmails().join().get(initialEmployeeInboxSize).getSubject());
    }
}