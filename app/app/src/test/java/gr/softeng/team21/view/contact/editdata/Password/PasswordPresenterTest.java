package gr.softeng.team21.view.contact.editdata.Password;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link PasswordPresenter} class.
 * These tests verify the logic for saving password details, validating input constraints (length, empty, same as old)
 * and handling user retrieval.
 * @author PAVLOS GRATSANIS
 */
public class PasswordPresenterTest {

    private PasswordViewStub view;
    private PasswordPresenter presenter;
    private Customer customer;

    /**
     * Sets up the test class before each test case.
     * Initializes in-memory data, retrieves a test customer, creates a view stub and initializes the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new PasswordViewStub();
        customer = MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");

        presenter = new PasswordPresenter(view, customer.getCustomer_id());
    }

    /**
     * Verifies that the password is successfully updated when the input is valid.
     * Checks if the success message is displayed and if the customer object is updated correctly.
     */
    @Test
    public void savePasswordClickedSuccess() {
        presenter.savePasswordClicked("NewPass2024");
        Assert.assertEquals("Ο κωδικός ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("NewPass2024", customer.getPassword());
    }

    /**
     * Verifies that a validation error is shown when the password is shorter than 8 characters.
     */
    @Test
    public void savePasswordClickedInvalidLength() {
        presenter.savePasswordClicked("12345");
        Assert.assertEquals("Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες", view.getMessage());
    }

    /**
     * Verifies that a validation error is shown when the password input is empty.
     */
    @Test
    public void savePasswordClickedEmpty() {
        presenter.savePasswordClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε κωδικό", view.getMessage());
    }

    /**
     * Verifies that a validation error is shown when the new password is the same as the current one.
     */
    @Test
    public void savePasswordClickedSamePassword() {
        presenter.savePasswordClicked("pass1234");
        Assert.assertEquals("Ο νέος κωδικός δεν μπορεί να είναι ίδιος με τον παλιό", view.getMessage());
    }

    /**
     * Verifies that the presenter handles a null user gracefully without crashing.
     *Nothing is tested simply to have 100% coverage.
     */
    @Test
    public void savePasswordClickedWithNullUser() {
        PasswordPresenter safePresenter = new PasswordPresenter(view, null);
        safePresenter.savePasswordClicked("validPass123");
    }

    /**
     * Tests the user retrieval logic in the presenter constructor.
     * Verifies cases where the user ID is invalid (user not found) and where a user exists but has a null password.
     */
    @Test
    public void testFindUser() {
        PasswordViewStub viewNotFound = new PasswordViewStub();
        PasswordPresenter presenter1 = new PasswordPresenter(viewNotFound, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewNotFound.getMessage());

        PasswordViewStub viewNullPass = new PasswordViewStub();

        Customer customerNullPass = new Customer("userNull", "Name", null, "Surname",
                "6900000000", new EmailAddress("null@test.gr"), "CUSTNULLPASS", new Date());
        CustomerDAOMemory.getInstance().addCustomer(customerNullPass);
        PasswordPresenter presenter2 = new PasswordPresenter(viewNullPass, customerNullPass.getCustomer_id());
        Assert.assertNull(viewNullPass.getPassword());
    }
}