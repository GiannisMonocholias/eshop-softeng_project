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

public class EmailDetailsPresenterTest {

    private EmailDetailsPresenter presenter;
    private EmailDetailsViewStub viewStub;

    // IDs από MemoryInitializer
    private static final String EMPLOYEE_ID = "CSR-101";
    private static final String CUSTOMER_ID = "CUST-500";
    private static final String DELIVERER_ID = "DEL-401";

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

        // Ο Sender είναι υπάλληλος, άρα πρέπει να έχει το suffix
        Assert.assertEquals("Νίκος Στάμος(Υπάλληλος)", viewStub.getDisplayedSenderName());

        // Ο Receiver αναγνωρίστηκε σωστά
        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getDisplayedReceiverName());
    }

    @Test
    public void onViewCreatedCustomerToEmployeeDisplaysCustomerSuffix() {
        Customer sender = CustomerDAOMemory.getInstance().getCustomer(CUSTOMER_ID);

        String senderEmail = sender.getEmailAddress().toString();

        // EXECUTE
        presenter.onViewCreated("Sub", senderEmail, "receiver@test.com", "Body", EMPLOYEE_ID);

        // Ο Sender είναι πελάτης
        Assert.assertEquals("Νίκος Γεωργίου(Πελάτης)", viewStub.getDisplayedSenderName());
    }

    @Test
    public void onViewCreatedAdminToEmployeeDisplaysAdminSuffix() {
        Admin admin = Admin.getInstance();
        String senderEmail = admin.getEmailAddress().toString();

        // EXECUTE
        presenter.onViewCreated("Alert", senderEmail, "rec@test.com", "Body", EMPLOYEE_ID);

        // Ο Sender είναι διαχειριστής
        Assert.assertTrue(viewStub.getDisplayedSenderName().contains("(Διαχειριστής)"));
    }

    @Test
    public void onViewCreatedUnknownSenderDisplaysUnknownMessage() {
        // EXECUTE με άγνωστο email
        presenter.onViewCreated("Sub", "unknownemail1@team21.com", "unknownemail2@team21.com", "Body", EMPLOYEE_ID);

        // CHECK
        Assert.assertEquals("Άγνωστο όνομα αποστολέα", viewStub.getDisplayedSenderName());
    }

    @Test
    public void onViewCreatedNullValuesDisplaysEmptyStrings() {
        // EXECUTE με null subject και body
        presenter.onViewCreated(null, "sender@test.com", "rec@test.com", null, EMPLOYEE_ID);

        // CHECK
        Assert.assertEquals("", viewStub.getDisplayedSubject());
        Assert.assertEquals("", viewStub.getDisplayedBody());
    }

    @Test
    public void findReceiverNameReturnsCorrectNameForEmployee() {
        String name = presenter.findReceiverName(EMPLOYEE_ID);
        Assert.assertEquals("Μαρία Αλεξάνδρου", name);
    }

    @Test
    public void findReceiverNameReturnsCorrectNameForCustomer() {
        String name = presenter.findReceiverName(CUSTOMER_ID);
        Assert.assertEquals("Νίκος Γεωργίου", name);
    }

    @Test
    public void findReceiverNameInvalidIdReturnsEmptyString() {
        String name = presenter.findReceiverName("INVALID_ID");
        Assert.assertEquals("", name);
    }

    @Test
    public void findSenderNameChecksAllUserTypes() {
        // Έλεγχος Υπαλλήλου
        String empEmail = EmployeeDAOMemory.getInstance().getEmployee(DELIVERER_ID).getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(empEmail).contains("(Υπάλληλος)"));

        // Έλεγχος Πελάτη
        String custEmail = CustomerDAOMemory.getInstance().getCustomer(CUSTOMER_ID).getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(custEmail).contains("(Πελάτης)"));

        // Έλεγχος Admin
        String adminEmail = Admin.getInstance().getEmailAddress().toString();
        Assert.assertTrue(presenter.findSenderName(adminEmail).contains("(Διαχειριστής)"));

        Assert.assertNull(presenter.findSenderName("unknown@mail.com"));
    }
}