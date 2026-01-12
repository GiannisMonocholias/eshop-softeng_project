package gr.softeng.team21.view.contact.editdata.Phone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link PhonePresenter} class.
 * These tests verify the logic for saving phone details, validating input length and handling user retrieval.
 * @author PAVLOS GRATSANIS
 */
public class PhonePresenterTest {

    private PhonePresenter presenter;
    private PhoneViewStub view;
    private Customer customer;

    /**
     * Sets up the test class before each test case.
     * Initializes in-memory data, retrieves a test customer, creates a view stub and initializes the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-501");
        view = new PhoneViewStub();
        presenter = new PhonePresenter(view, customer.getCustomer_id());
    }

    /**
     * Verifies that the phone number is successfully updated when the input is valid.
     * Checks if the success message is displayed and if the customer object is updated correctly.
     */
    @Test
    public void savePhoneClickedSuccess() {
        presenter.savePhoneClicked("6966778899");
        Assert.assertEquals("Το τηλέφωνο ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("6966778899", customer.getPhonenumber());
    }

    /**
     * Verifies that a validation error is shown when the phone number does not have 10 digits.
     */
    @Test
    public void savePhoneClickedInvalidLength() {
        presenter.savePhoneClicked("12345");
        Assert.assertEquals("Το τηλέφωνο πρέπει να έχει 10 ψηφία", view.getMessage());
    }

    /**
     * Verifies that a validation error is shown when the phone input is empty.
     */
    @Test
    public void savePhoneClickedEmpty() {
        presenter.savePhoneClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε τηλέφωνο", view.getMessage());
    }

    /**
     * Verifies that the presenter handles a null user gracefully without crashing.
     * Nothing is tested simply to have 100% coverage.
     */
    @Test
    public void savePhoneClickedWithNullUser() {
        PhonePresenter nullPresenter = new PhonePresenter(view, null);
        nullPresenter.savePhoneClicked("6912345678");
    }

    /**
     * Tests the user retrieval logic in the presenter constructor.
     * Verifies cases where the user ID is invalid (user not found) and where a user exists but has a null phone number.
     */
    @Test
    public void testFindUser() {
        PhoneViewStub viewNotFound = new PhoneViewStub();
        PhonePresenter presenter1 = new PhonePresenter(viewNotFound, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewNotFound.getMessage());

        PhoneViewStub viewNullPhone = new PhoneViewStub();
        Customer customerNullPhone = new Customer("User", "Name", "pass", "Surname", null, new EmailAddress("test@mail.com"), "CUSTNULLPHONE", new Date());
        CustomerDAOMemory.getInstance().addCustomer(customerNullPhone);
        PhonePresenter presenter2 = new PhonePresenter(viewNullPhone, customerNullPhone.getCustomer_id());
        Assert.assertNull(viewNullPhone.getPhone());
    }
}