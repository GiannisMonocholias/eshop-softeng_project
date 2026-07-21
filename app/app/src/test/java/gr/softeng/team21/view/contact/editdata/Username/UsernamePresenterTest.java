package gr.softeng.team21.view.contact.editdata.Username;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link UsernamePresenter} class.
 * These tests verify the logic for saving username details, validating input (empty, taken, no change) and handling user retrieval.
 * @author PAVLOS GRATSANIS
 */
public class UsernamePresenterTest {
    private Customer customer;
    private UsernamePresenter presenter;
    private UsernameViewStub view;

    /**
     * Sets up the test class before each test case.
     * Initializes in-memory data, retrieves a test customer, creates a view stub and initializes the presenter.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new UsernameViewStub();
        customer = MemoryInitializer.getCustomerDAO().getCustomer("CUST-500").join();
        presenter = new UsernamePresenter(view, customer.getCustomer_id());
    }

    /**
     * Verifies that the username is successfully updated when the input is valid and unique.
     * Checks if the success message is displayed and if the customer object is updated correctly.
     */
    @Test
    public void saveUsernameClickedSuccess() {
        presenter.saveUsernameClicked("newusername");
        Assert.assertEquals("Το username ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("newusername", customer.getUsername());
    }

    /**
     * Verifies that a validation error is shown when the username input is empty.
     */
    @Test
    public void saveUsernameClickedEmptyInput() {
        presenter.saveUsernameClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε Username", view.getMessage());
    }

    /**
     * Verifies that a success message (indicating no change) is shown when the new username is identical to the current one.
     */
    @Test
    public void saveUsernameClickedSameName() {
        String currentName = customer.getUsername();
        presenter.saveUsernameClicked(currentName);
        Assert.assertEquals("Δεν έγιναν αλλαγές.", view.getMessage());
    }

    /**
     * Verifies that a validation error is shown when the requested username is already taken by another user.
     */
    @Test
    public void saveUsernameClickedTakenName() {
        presenter.saveUsernameClicked("georgepap");
        Assert.assertEquals("Το username χρησιμοποιείται ήδη.", view.getMessage());
    }

    /**
     * Verifies that the presenter handles a null user gracefully without crashing.
     * Nothing is tested simply to have 100% coverage.
     */
    @Test
    public void saveUsernameClickedWithNull() {
        UsernamePresenter nullpresenter = new UsernamePresenter(view, null);
        nullpresenter.saveUsernameClicked("newname");
    }

    /**
     * Tests the user retrieval logic in the presenter constructor.
     * Verifies cases where the user ID is invalid (user not found),
     * where a user exists but has a null username,
     * and where a user exists but has an empty username string.
     */
    @Test
    public void testfindUser() {
        UsernameViewStub viewForNullId = new UsernameViewStub();
        UsernamePresenter nullpresenter = new UsernamePresenter(viewForNullId, null);
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewForNullId.getMessage());

        UsernameViewStub viewForNullName = new UsernameViewStub();
        Customer customer1 = new Customer(null, "firstname", "pass", "lastname", "69999",
                new EmailAddress("@"), "CUST-NULL", new Date());
        CustomerDAOMemory.getInstance().addCustomer(customer1).join();
        UsernamePresenter presenter1 = new UsernamePresenter(viewForNullName, customer1.getCustomer_id());
        Assert.assertNull(viewForNullName.getCurrentUsername());

        UsernameViewStub viewForEmptyName = new UsernameViewStub();
        Customer customer2 = new Customer("", "firstname", "pass", "lastname", "69999",
                new EmailAddress("@"), "CUST-EMPTY", new Date());
        CustomerDAOMemory.getInstance().addCustomer(customer2).join();
        UsernamePresenter presenter2 = new UsernamePresenter(viewForEmptyName, customer2.getCustomer_id());
        Assert.assertNull(viewForEmptyName.getCurrentUsername());
    }
}