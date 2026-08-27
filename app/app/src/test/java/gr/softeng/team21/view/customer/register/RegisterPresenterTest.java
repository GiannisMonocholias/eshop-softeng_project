package gr.softeng.team21.view.customer.register;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit tests for {@link RegisterPresenter}.
 * This suite ensures that the customer registration logic correctly handles
 * successful data entry, persistence in DAOs, and validation error reporting.
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterPresenterTest {

    private RegisterPresenter presenter;
    private RegisterViewStub viewStub;
    private CustomerDAO customerDAO;
    private UserCredentialsDAO credentialsDAO;

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data and instantiates the presenter with its dependencies.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new RegisterViewStub();

        // Inject In-Memory DAOs for testing
        customerDAO = CustomerDAOMemory.getInstance();
        credentialsDAO = UserCredentialsDAOMemory.getInstance();

        presenter = new RegisterPresenter(viewStub, customerDAO, credentialsDAO);
    }

    /**
     * Verifies that providing valid registration data results in:
     * 1. A success message in the view.
     * 2. Cleared input fields.
     * 3. A new customer record in the CustomerDAO.
     * 4. Validated credentials in the UserCredentialsDAO.
     */
    @Test
    public void registerValidDataSuccess() {
        String username = "newUser";
        String password = "newPass";
        String firstname = "Giorgos";
        String lastname = "Georgiou";
        String phone = "6999999999";
        String email = "georg@example.com";

        // Use .join() to safely get the current size from the CompletableFuture
        int initialCount = customerDAO.getCustomers().join().size();

        presenter.register(username, firstname, password, lastname, phone, email);

        Assert.assertTrue(viewStub.getSuccessMessage().contains("Επιτυχής εγγραφή"));
        Assert.assertTrue(viewStub.areInputsCleared());
        Assert.assertEquals("", viewStub.getErrorMessage());

        Assert.assertEquals(initialCount + 1, customerDAO.getCustomers().join().size());

        boolean customerFound = false;
        Map<String, Customer> allCustomers = customerDAO.getCustomers().join();

        for (Customer c : allCustomers.values()) {
            if (c.getUsername().equals(username)) {
                customerFound = true;
                Assert.assertEquals(email, c.getEmailAddress().toString());
                break;
            }
        }
        Assert.assertTrue("Ο νέος πελάτης δεν βρέθηκε στο CustomerDAO", customerFound);

        // Validate credentials exist in the Auth DAO
        Assert.assertNotNull(credentialsDAO.validateAndGetUser(username, password).join());
    }

    /**
     * Verifies that attempting to register with empty fields (e.g., missing password)
     * triggers the appropriate error message and prevents registration.
     */
    @Test
    public void registerEmptyFieldsShowsErrorMessage() {
        presenter.register("user", "First", "", "Last", "123", "mail@test.com");

        Assert.assertEquals("Παρακαλώ συμπληρώστε τα απαραίτητα πεδία.", viewStub.getErrorMessage());
        Assert.assertEquals("", viewStub.getSuccessMessage());
        Assert.assertFalse(viewStub.areInputsCleared());
    }
}