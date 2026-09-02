package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link AuthenticationSystem} class.
 * This test suite verifies user registration logic, successful and failed login attempts,
 * and account removal processes securely using Async DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class AuthenticationSystemTest {
    private AuthenticationSystem authSystem;
    private UserCredentialsDAO repo;

    @Before
    public void setUp() {
        repo = MemoryInitializer.getUserCredentialsDAO();
        repo.clear().join();

        // Initialization with Dependency Injection
        authSystem = new AuthenticationSystem(repo);
    }

    @Test
    public void initiallyEmptyRepositoryTest(){
        Assertions.assertTrue(repo.getUsersCredentials().join().isEmpty());
    }

    @Test
    public void registerCustomerStoresUserTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "CUST-001", now).join();

        User storedUser = repo.validateAndGetUser("giannispap", "pass1234").join();
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

    @Test
    public void registerCustomerDuplicateUsernameThrowsExceptionTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");

        // First register completed successfully
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now).join();

        // Catch the CompletionException which is caused by .join()
        CompletionException thrown = Assertions.assertThrows(
                CompletionException.class,
                () -> {
                    authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                            "697123456", email, "Customer2", now).join();
                }
        );

        // Check with instanceof that the actual cause is the IllegalArgumentException
        Assertions.assertTrue(thrown.getCause() instanceof IllegalArgumentException,
                "Η αιτία του σφάλματος πρέπει να είναι IllegalArgumentException");
        Assertions.assertEquals("Username already exists", thrown.getCause().getMessage());
    }

    @Test
    public void loginSuccessTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now).join();

        User user = authSystem.login("giannispap", "pass1234").join();
        Assertions.assertNotNull(user);
        Assertions.assertEquals("giannispap", user.getUsername());
    }

    @Test
    public void loginFailureThrowsExceptionTest() {
        CompletionException thrown = Assertions.assertThrows(
                CompletionException.class,
                () -> authSystem.login("unknown", "wrongpass").join()
        );

        Assertions.assertTrue(thrown.getCause() instanceof SecurityException,
                "Η αιτία του σφάλματος πρέπει να είναι SecurityException");
    }

    @Test
    public void removeUserTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now).join();

        // Successful deletion
        authSystem.removeUser("giannispap").join();

        // login attempt after the deletion
        CompletionException thrown = Assertions.assertThrows(
                CompletionException.class,
                () -> authSystem.login("giannispap", "pass789").join()
        );

        Assertions.assertTrue(thrown.getCause() instanceof SecurityException,
                "Η αιτία του σφάλματος πρέπει να είναι SecurityException");
    }

    @Test
    public void removeNonExistingUserTest(){
        CompletionException thrown = Assertions.assertThrows(
                CompletionException.class,
                () -> authSystem.removeUser("giannispap").join()
        );

        Assertions.assertTrue(thrown.getCause() instanceof NoSuchElementException,
                "Η αιτία του σφάλματος πρέπει να είναι NoSuchElementException");
    }

    @After
    public void tearDown(){
        repo.clear().join();
        MemoryInitializer.getCustomerDAO().clear().join();
    }
}