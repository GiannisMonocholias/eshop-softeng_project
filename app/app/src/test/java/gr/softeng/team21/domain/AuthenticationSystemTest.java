package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link AuthenticationSystem} class.
 * This test suite verifies the Singleton integrity, user registration logic,
 * successful and failed login attempts, and account removal processes.
 * @author Γιάννης Μονοχολιάς
 */
public class AuthenticationSystemTest {
    private AuthenticationSystem authSystem;
    private UserCredentialsDAOMemory repo;

    /**
     * Sets up the testing environment before each test.
     * Initializes the credentials repository and the authentication system instance.
     */
    @Before
    public void setUp() {
        repo = UserCredentialsDAOMemory.getInstance();
        authSystem = AuthenticationSystem.getInstance();
    }

    /**
     * Verifies that the repository is empty at the start of testing.
     */
    @Test
    public void initiallyEmptyRepositoryTest(){
        Assertions.assertTrue(repo.getUsersCredentials().isEmpty());
    }

    /**
     * Verifies that the {@link AuthenticationSystem} correctly implements the Singleton pattern.
     */
    @Test
    public void getInstanceReturnsSameReferencesTest() {
        AuthenticationSystem sys1 = AuthenticationSystem.getInstance();
        AuthenticationSystem sys2 = AuthenticationSystem.getInstance();
        Assertions.assertSame(sys1, sys2);
    }

    /**
     * Tests the registration of a new customer.
     * Validates that all user attributes are correctly persisted in the repository.
     */
    @Test
    public void registerCustomerStoresUserTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "CUST-001", now);

        User storedUser = repo.validateAndGetUser("giannispap", "pass1234");
        Assertions.assertNotNull(storedUser);
        Assertions.assertTrue(storedUser instanceof Customer);

        Customer cust = (Customer) storedUser;
        Assertions.assertEquals("giannispap", cust.getUsername());
        Assertions.assertEquals("Giannis", cust.getFirstname());
        Assertions.assertEquals("Papadopoulos", cust.getLastname());
        Assertions.assertEquals("697123456", cust.getPhonenumber());
        Assertions.assertEquals(email, cust.getEmailAddress());
        Assertions.assertEquals("CUST-001", cust.getCustomer_id());
        Assertions.assertEquals(now, cust.getRegistdateDate());
    }

    /**
     * Verifies that the system prevents duplicate registrations with the same username.
     * Expected behavior is to throw an {@link IllegalArgumentException}.
     */
    @Test
    public void registerCustomerDuplicateUsernameThrowsExceptionTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                    "697123456", email, "Customer2", now);
        });
    }

    /**
     * Tests a successful login operation using valid credentials.
     */
    @Test
    public void loginSuccessTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now);

        User user = authSystem.login("giannispap", "pass1234");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("giannispap", user.getUsername());
    }

    /**
     * Tests login failure behavior when provided with non-existent credentials.
     * Expected behavior is to throw a {@link SecurityException}.
     */
    @Test
    public void loginFailureThrowsExceptionTest() {
        Assertions.assertThrows(SecurityException.class, () -> authSystem.login("unknown", "wrongpass"));
    }

    /**
     * Tests the removal of a user account.
     * Validates that after removal, the user can no longer log in.
     */
    @Test(expected = SecurityException.class)
    public void removeUserTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now);

        authSystem.removeUser("giannispap");

        authSystem.login("giannispap", "pass789");
    }

    /**
     * Verifies that attempting to remove a user that does not exist throws an exception.
     */
    @Test
    public void removeNonExistingUserTest(){
        Assertions.assertThrows(NoSuchElementException.class,()->{authSystem.removeUser("giannispap");});
    }

    /**
     * Cleans up the repositories after each test to ensure test isolation and a clean state.
     */
    @After
    public void tearDown(){
        repo.clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}