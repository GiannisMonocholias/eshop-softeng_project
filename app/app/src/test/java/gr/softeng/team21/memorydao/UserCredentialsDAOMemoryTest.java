package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CompletionException;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.User;

/**
 * Unit tests for the {@link UserCredentialsDAOMemory} class.
 * This suite validates the security-critical operations of the system, including
 * asynchronous user registration, credential validation (login logic), and the enforcement
 * of unique identifiers and secure access.
 * @author Γιάννης Μονοχολιάς
 */
public class UserCredentialsDAOMemoryTest {
    private UserCredentialsDAOMemory repository;

    /**
     * Initializes the testing environment before each test.
     * Obtains the singleton instance, clears the credentials repository asynchronously,
     * and ensures the customer memory DAO is reset to maintain test isolation.
     * @throws Exception if setup fails.
     */
    @Before
    public void setUp() throws Exception {
        this.repository = UserCredentialsDAOMemory.getInstance();
        repository.clear().join();
        CustomerDAOMemory.getInstance().clear().join();
    }

    /**
     * Verifies that the credentials repository is empty upon initialization.
     */
    @Test
    public void GetUsersCredentialsInitiallyEmptyTest() {
        assertTrue(repository.getUsersCredentials().join().isEmpty());
    }

    /**
     * Verifies that {@link UserCredentialsDAOMemory} correctly implements the
     * Singleton pattern by returning identical references across multiple calls.
     */
    @Test
    public void getInstanceReturnsSameReferencesTest() {
        UserCredentialsDAOMemory repository1 = UserCredentialsDAOMemory.getInstance();
        assertSame(repository1, repository);
    }

    /**
     * Verifies that the system prevents the registration of two different users
     * with the same username. Expects an exception wrapped in a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void addUserAlreadyExistingUsernameTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1).join();

        // Addition of an already existing username
        Customer user2 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "Customer1", new Date());

        repository.addUser(user2).join();
    }

    /**
     * Verifies that attempting to remove a user that does not exist in the
     * repository results in an exception wrapped in a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void removeUserNonExistingUsernameTest() {
        repository.removeUser("UnknownUser").join();
    }

    /**
     * Tests the successful asynchronous authentication of a user when provided with
     * correct username and password combinations.
     */
    @Test
    public void validateAndGetUser_SuccessTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1).join();

        // Correct credentials
        User loggedIn = repository.validateAndGetUser("giannispap", "pass1234").join();
        assertEquals(user1, loggedIn);
    }

    /**
     * Verifies that providing an incorrect password for an existing user
     * results in a SecurityException wrapped inside a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void validateAndGetUser_IncorrectPasswordTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1).join();

        // Incorrect password
        repository.validateAndGetUser("giannispap", "WrongPass").join();
    }

    /**
     * Verifies that attempting to authenticate a username that is not present
     * in the system results in a SecurityException wrapped inside a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void validateAndGetUser_UnknownUserTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1).join();

        repository.validateAndGetUser("UnknownUser", "Password1").join();
    }

    /**
     * Cleans up the repositories asynchronously after each test case to prevent data pollution
     * and ensure a clean state for subsequent tests.
     * @throws Exception if teardown fails.
     */
    @After
    public void tearDown() throws Exception {
        repository.clear().join();
        CustomerDAOMemory.getInstance().clear().join();
    }
}