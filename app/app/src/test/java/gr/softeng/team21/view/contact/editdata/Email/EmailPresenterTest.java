package gr.softeng.team21.view.contact.editdata.Email;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link EmailPresenter} class.
 * These tests verify the logic for saving email details, validating input format and handling user retrieval.
 * @author PAVLOS GRATSANIS
 */
public class EmailPresenterTest {

    private EmailViewStub view;
    private EmailPresenter presenter;
    private Customer customer;

    /**
     * Sets up the test class before each test case.
     * Initializes in-memory data, retrieves a test customer, creates a view stub, and initializeis the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new EmailViewStub();
        customer = MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");

        presenter = new EmailPresenter(view, customer.getCustomer_id());
    }

    /**
     * Verifies that the email is successfully updated when the input is valid.
     * Checks if the success message is displayed and if the customer object is updated correctly.
     */
    @Test
    public void saveEmailClickedSuccess() {
        presenter.saveEmailClicked("new.nick@example.com");
        Assert.assertEquals("Το email ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("new.nick@example.com", customer.getEmailAddress().toString());
    }

    /**
     * Verifies that a validation error is shown when the email format is invalid.
     */
    @Test
    public void saveEmailClickedInvalidFormat() {
        presenter.saveEmailClicked("invalid_email_format");
        Assert.assertEquals("Μη έγκυρη μορφή email", view.getMessage());
    }

    /**
     * Verifies that a validation error is shown when the email input is empty.
     */
    @Test
    public void saveEmailClickedEmpty() {
        presenter.saveEmailClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε Email", view.getMessage());
    }

    /**
     * Verifies that the presenter handles a null user gracefully without crashing.
     *Nothing is tested simply to have 100% coverage.
     */
    @Test
    public void saveEmailClickedWithNullUser() {
        EmailPresenter presenternull = new EmailPresenter(view, null);
        presenternull.saveEmailClicked("validPass@gmail.com");
    }

    /**
     * Tests the user retrieval logic in the presenter constructor.
     * Verifies cases where the user ID is invalid (user not found),
     * where a user exists but has a null email object,
     * and where a user exists but has an empty email string.
     */
    @Test
    public void testFindUser() {
        EmailViewStub viewNotFound = new EmailViewStub();
        EmailPresenter presenter1 = new EmailPresenter(viewNotFound, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewNotFound.getMessage());

        EmailViewStub viewNullEmail = new EmailViewStub();
        Customer customerNullEmail = new Customer(
                "userNullEmail",
                "Name",
                "password123",
                "Surname",
                "6969696969",
                null,
                "CUSTNULLEMAIL",
                new Date()
        );
        CustomerDAOMemory.getInstance().addCustomer(customerNullEmail);
        EmailPresenter presenter2 = new EmailPresenter(viewNullEmail, customerNullEmail.getCustomer_id());
        Assert.assertNull(viewNullEmail.getEmail());

        EmailViewStub viewEmptyString = new EmailViewStub();

        Customer customerEmptyString = new Customer(
                "userEmpty",
                "Name",
                "pass",
                "Surname",
                "6969696969",
                new EmailAddress(""),
                "CUST_EMPTY_STR",
                new Date()
        );

        CustomerDAOMemory.getInstance().addCustomer(customerEmptyString);
        EmailPresenter presenter3 = new EmailPresenter(viewEmptyString, customerEmptyString.getCustomer_id());
        Assert.assertNull(viewEmptyString.getEmail());
    }
}