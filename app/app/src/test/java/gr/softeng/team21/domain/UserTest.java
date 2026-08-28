package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the abstract {@link User} class (tested via its concrete subclass {@link Customer}).
 * This suite verifies core user profile data management (getters and setters)
 * following the removal of domain-level DAOs and messaging logic.
 * @author Γιάννης Μονοχολιάς
 */
public class UserTest {

    private Customer customer;

    /**
     * Initializes the testing environment before each test.
     * Sets up a sample Customer to test the inherited User methods.
     */
    @Before
    public void setUp() {
        EmailAddress customerEmailAddress = new EmailAddress("giannis@mail.com");
        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", customerEmailAddress, "CUST-001", new Date());
    }

    /**
     * Tests the username property accessors.
     */
    @Test
    public void testUsernameGetterSetter() {
        customer.setUsername("newUser");
        assertEquals("newUser", customer.getUsername());
    }

    /**
     * Tests the password property accessors.
     */
    @Test
    public void testPasswordGetterSetter() {
        customer.setPassword("newPass");
        assertEquals("newPass", customer.getPassword());
    }

    /**
     * Tests the first name property accessors.
     */
    @Test
    public void testFirstnameGetterSetter() {
        customer.setFirstname("NewFirst");
        assertEquals("NewFirst", customer.getFirstname());
    }

    /**
     * Tests the last name property accessors.
     */
    @Test
    public void testLastnameGetterSetter() {
        customer.setLastname("NewLast");
        assertEquals("NewLast", customer.getLastname());
    }

    /**
     * Tests the phone number property accessors.
     */
    @Test
    public void testPhoneNumberGetterSetter() {
        customer.setPhonenumber("99999");
        assertEquals("99999", customer.getPhonenumber());
    }

    /**
     * Tests the email address property accessors.
     */
    @Test
    public void testEmailAddressGetterSetter() {
        EmailAddress newAddress = new EmailAddress("new@example.com");
        customer.setEmailaddress(newAddress);
        assertEquals(newAddress, customer.getEmailAddress());
    }

    /**
     * Tests the address property accessors.
     */
    @Test
    public void testAddressGetterSetter() {
        Address newaddr = new Address("Solonos", "25", "Athens", "Greece", "10672");
        customer.setAddress(newaddr);
        assertEquals(newaddr, customer.getAddress());
    }
}