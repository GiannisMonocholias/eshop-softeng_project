package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class UserCredentialsDAOMemoryTest {
    private UserCredentialsDAOMemory repository;

    @Before
    public void setUp() throws Exception {
        this.repository = UserCredentialsDAOMemory.getInstance();
        repository.clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }

    @Test
    public void GetUsersCredentialsInitiallyEmptyTest() {
        assertTrue(repository.getUsersCredentials().isEmpty());
    }

    @Test
    public void getInstanceReturnsSameReferencesTest() {
        UserCredentialsDAOMemory repository1 = UserCredentialsDAOMemory.getInstance();
        assertSame(repository1, repository);
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkNullArguments_BothNullTest(){
        repository.checkNullArguments(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNullArguments_PasswordNullTest(){
        repository.checkNullArguments("Username", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNullArguments_UsernameNullTest(){
        repository.checkNullArguments(null, "Password");
    }


    @Test(expected = IllegalArgumentException.class)
    public void addUserAlreadyExistingUsernameTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1);

        // Addition of an already existing username
        Customer user2 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "Customer1", new Date());

        repository.addUser(user2);
    }

    @Test(expected = NoSuchElementException.class)
    public void removeUserNonExistingUsernameTest() {
        repository.removeUser("UnknownUser");
    }



    @Test
    public void validateAndGetUser_SuccessTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1);

        // Correct credentials
        User loggedIn = repository.validateAndGetUser("giannispap", "pass1234");
        assertEquals(user1, loggedIn);
    }

    @Test(expected = SecurityException.class)
    public void validateAndGetUser_IncorrectPasswordTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1);

        // Incorrect password
        repository.validateAndGetUser("giannispap", "WrongPass");
    }

    @Test(expected = SecurityException.class)
    public void validateAndGetUser_UnknownUserTest() {
        Customer user1 = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        repository.addUser(user1);

        repository.validateAndGetUser("UnknownUser", "Password1");
    }


    @After
    public void tearDown() throws Exception {
        repository.clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}