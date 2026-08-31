package gr.softeng.team21.view.user.emailComposition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for {@link EmailCompositionPresenter}.
 * Verifies validation constraints and the single-dispatch logic
 * via the unified EmailDAO structure.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionPresenterTest {

    private EmailCompositionPresenter presenter;
    private EmailCompositionViewStub viewStub;
    private EmailDAO emailDAO;
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String CUSTOMER_ID = "CUST-500";

    private Employee employee;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new EmailCompositionViewStub();
        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        EmployeeDAO employeeDAO = EmployeeDAOMemory.getInstance();

        emailDAO = EmailDAOMemory.getInstance();
        emailDAO.clear().join();

        presenter = new EmailCompositionPresenter(viewStub, customerDAO, employeeDAO, emailDAO);

        employee = employeeDAO.getEmployee(EMPLOYEE_ID).join();
        customer = customerDAO.getCustomer(CUSTOMER_ID).join();
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
    public void onSendClickedEmptyFieldsShowsInputErrors() {
        presenter.onViewCreated(EMPLOYEE_ID);

        viewStub.setRecipientEmailInput("");
        viewStub.setSubjectInput("");
        viewStub.setBodyInput("");

        presenter.onSendClicked();

        // Ελέγχει την εμφάνιση των σφαλμάτων σε κάθε πεδίο ξεχωριστά
        Assert.assertEquals("Το email παραλήπτη είναι υποχρεωτικό.", viewStub.getInputError("recipient"));
        Assert.assertEquals("Το θέμα είναι υποχρεωτικό.", viewStub.getInputError("subject"));
        Assert.assertEquals("Το κείμενο μηνύματος δεν μπορεί να είναι κενό.", viewStub.getInputError("body"));
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

        String recipientAddress = customer.getEmailAddress().toString();
        int initialSize = emailDAO.getEmailsForUser(recipientAddress).join().size();

        viewStub.setRecipientEmailInput(recipientAddress);
        viewStub.setSubjectInput("Order Update");
        viewStub.setBodyInput("Your order is ready.");

        presenter.onSendClicked();

        Assert.assertEquals("Το μήνυμα εστάλη επιτυχώς!", viewStub.getSuccessMessage());

        // Validate state updates based on the exact receiver address
        Assert.assertEquals(initialSize + 1, emailDAO.getEmailsForUser(recipientAddress).join().size());
        Assert.assertEquals("Order Update", emailDAO.getEmailsForUser(recipientAddress).join().get(initialSize).getSubject());
    }
}