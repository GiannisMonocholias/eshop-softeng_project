package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationSystemTest {
    private AuthenticationSystem authSystem;
    private UserCredentialsRepository repo;

    @Before
    public void setUp() {
        repo = UserCredentialsRepository.getInstance();
        authSystem = AuthenticationSystem.getInstance();
    }

    @Test
    public void initiallyEmptyRepositoryTest(){
        Assertions.assertTrue(repo.getUsersCredentials().isEmpty());
    }

    @Test
    public void getInstanceReturnsSameReferencesTest() {
        AuthenticationSystem sys1 = AuthenticationSystem.getInstance();
        AuthenticationSystem sys2 = AuthenticationSystem.getInstance();
        Assertions.assertSame(sys1, sys2);
    }

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

    @Test
    public void loginFailureThrowsExceptionTest() {
        Assertions.assertThrows(SecurityException.class, () -> authSystem.login("unknown", "wrongpass"));
    }

    @Test(expected = SecurityException.class)
    public void removeUserTest() {
        Date now = new Date();
        EmailAddress email = new EmailAddress("giannis@mail.com");
        authSystem.registerCustomer("giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", email, "Customer1", now);

        authSystem.removeUser("giannispap");

        authSystem.login("giannispap", "pass789");
    }

    @Test
    public void removeNonExistingUserTest(){
        Assertions.assertThrows(NoSuchElementException.class,()->{authSystem.removeUser("giannispap");});
    }




    @After
    public void tearDown(){
        repo.clear();
        CustomerRepository.getInstance().getCustomers().clear();
    }
}