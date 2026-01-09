package gr.softeng.team21.view.contact.emailDetails;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for {@link EmailDetailsPresenter}.
 * Verifies that the presenter correctly maps data from DAOs to the View
 * and applies proper formatting for different user roles (Employee, Customer, Admin).
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDetailsPresenterTest {

    private EmailDetailsPresenter presenter;
    private EmailDetailsViewStub viewStub;

    // IDs from MemoryInitializer
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String CUSTOMER_ID = "CUST-500";
    private static final String DELIVERER_ID = "DEL-401";

    /**
     * Prepares initial memory data and sets up the presenter with a view stub
     * before each test execution.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        // 2. Setup Components
        viewStub = new EmailDetailsViewStub();
        presenter = new EmailDetailsPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                CustomerDAOMemory.getInstance()
        );
    }

    /**
     * Verifies that when an email is sent between two employees,
     * the view displays the sender's name with the "(Υπάλληλος)" suffix.
     */
    @Test
    public void onViewCreatedEmployeeToEmployeeDisplaysCorrectDetails() {
        Employee sender = EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID);
        Employee receiver = EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        String subject = "Test Subject";
        String body = "Test Body";
        String senderEmail = sender.getEmailAddress().toString();
        String receiverEmail = receiver.getEmailAddress().toString();

        presenter.onViewCreated(subject, senderEmail, receiverEmail, body, EMPLOYEE_ID);

        // CHECK
        Assert.assertEquals("Test Subject", viewStub.getDisplayedSubject());
        Assert.assertEquals("Test Body", viewStub.getDisplayedBody());
        Assert.assertEquals(senderEmail, viewStub.getDisplayedSenderEmail());

        // Sender is an employee, should have the suffix
        Assert.assertEquals("Νίκος Στάμος (Υπάλληλος)", viewStub.getDisplayedSenderName());

        // Receiver recognized correctly
        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getDisplayedReceiverName());
    }

    /**
     * Verifies that when the sender is a Customer, the view correctly
     * appends the "(Πελάτης)" suffix to the name.
     */
    @Test
    public void onViewCreatedCustomerToEmployeeDisplaysCustomerSuffix() {
        Customer sender = CustomerDAOMemory.getInstance().getCustomer(CUSTOMER_ID);

        String senderEmail = sender.getEmailAddress().toString();

        // EXECUTE
        presenter.onViewCreated("Sub", senderEmail, "receiver@test.com", "Body", EMPLOYEE_ID);

        // Sender is a customer
        Assert.assertEquals("Νίκος Γεωργίου (Πελάτης)", viewStub.getDisplayedSenderName());
    }

    /**
     * Verifies that when the sender is the Administrator, the view correctly
     * appends the "(Διαχειριστής)" suffix.
     */
    @Test
    public void onViewCreatedAdminToEmployeeDisplaysAdminSuffix() {
        Admin admin = Admin.getInstance();
        String senderEmail = admin.getEmailAddress().toString();

        // EXECUTE
        presenter.onViewCreated("Alert", senderEmail, "rec@test.com", "Body", EMPLOYEE_ID);

        // Sender is admin
        Assert.assertTrue(viewStub.getDisplayedSenderName().contains("(Διαχειριστής)"));
    }

    /**
     * Verifies that if the sender's email cannot be found in the system,
     * a generic "Unknown sender" message is displayed.
     */
    @Test
    public void onViewCreatedUnknownSenderDisplaysUnknownMessage() {
        // EXECUTE with unknown email
        presenter.onViewCreated("Sub", "unknownemail1@team21.com", "unknownemail2@team21.com", "Body", EMPLOYEE_ID);

        // CHECK
        Assert.assertEquals("Άγνωστο όνομα αποστολέα", viewStub.getDisplayedSenderName());
    }

    /**
     * Verifies that the presenter handles null values gracefully by
     * converting them to empty strings in the view.
     */
    @Test
    public void onViewCreatedNullValuesDisplaysEmptyStrings() {
        // EXECUTE with null subject and body
        presenter.onViewCreated(null, "sender@test.com", "rec@test.com", null, EMPLOYEE_ID);

        // CHECK
        Assert.assertEquals("", viewStub.getDisplayedSubject());
        Assert.assertEquals("", viewStub.getDisplayedBody());
    }

    /**
     * Tests that finding a receiver name by ID works correctly for Employees.
     */
    @Test
    public void findReceiverNameReturnsCorrectNameForEmployee() {
        String name = presenter.findReceiverName(EMPLOYEE_ID);
        Assert.assertEquals("Μαρία Αλεξάνδρου", name);
    }

    /**
     * Tests that finding a receiver name by ID works correctly for Customers.
     */
    @Test
    public void findReceiverNameReturnsCorrectNameForCustomer() {
        String name = presenter.findReceiverName(CUSTOMER_ID);
        Assert.assertEquals("Νίκος Γεωργίου", name);
    }

    /**
     * Verifies that searching for an invalid ID returns an empty string.
     */
    @Test
    public void findReceiverNameInvalidIdReturnsEmptyString() {
        String name = presenter.findReceiverName("INVALID_ID");
        Assert.assertEquals("", name);
    }

    /**
     * Tests the internal logic that iterates through all user types (Employee,
     * Customer, Admin) to determine the correct sender name and role suffix.
     */
    @Test
    public void findSenderNameChecksAllUserTypes() {
        // Check Employee
        String empEmail = EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID).getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(empEmail).contains("(Υπάλληλος)"));

        // Check Customer
        String custEmail = CustomerDAOMemory.getInstance().getCustomer(CUSTOMER_ID).getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(custEmail).contains("(Πελάτης)"));

        // Check Admin
        String adminEmail = Admin.getInstance().getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(adminEmail).contains("(Διαχειριστής)"));

        Assert.assertNull(presenter.findSenderName("unknown@mail.com"));
    }
}