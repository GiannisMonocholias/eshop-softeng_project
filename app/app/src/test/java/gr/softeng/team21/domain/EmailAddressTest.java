package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;

/**
 * Unit tests for the {@link EmailAddress} class.
 * This test suite validates the correct handling of email strings,
 * focusing on data integrity, equality logic, and hashing consistency.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailAddressTest {

    private EmailAddress emailAddress;

    /**
     * Sets up a fresh EmailAddress instance before each test execution.
     */
    @Before
    public void setUp() {
        emailAddress = new EmailAddress("user@example.com");
    }

    /**
     * Verifies that the getter method returns the correct email address string.
     */
    @Test
    public void emailAddressGetterTest() {
        assertEquals("user@example.com", emailAddress.getAddress());
    }

    /**
     * Verifies that the setter method correctly updates the email address.
     */
    @Test
    public void setterTest() {
        emailAddress.setAddress("new@example.com");
        assertEquals("new@example.com", emailAddress.getAddress());
    }

    /**
     * Confirms that the equals method identifies the same object instance as equal.
     */
    @Test
    public void equalsSameObjectTest() {
        assertTrue(emailAddress.equals(emailAddress));
    }

    /**
     * Confirms that two different objects with identical email strings are considered equal.
     */
    @Test
    public void equalsSameValuesTest() {
        EmailAddress other = new EmailAddress("user@example.com");
        assertEquals(emailAddress, other);
    }

    /**
     * Confirms that two objects with different email strings are not considered equal.
     */
    @Test
    public void equalsDifferentValuesTest() {
        EmailAddress other = new EmailAddress("different@example.com");
        assertNotEquals(emailAddress, other);
    }

    /**
     * Verifies that an EmailAddress instance is not equal to null.
     */
    @Test
    public void equalsNullTest() {
        assertNotEquals(emailAddress, null);
    }

    /**
     * Verifies that an EmailAddress instance is not equal to an object of a different class.
     */
    @Test
    public void equalsDifferentClassTest() {
        assertNotEquals(emailAddress, new Object());
    }

    /**
     * Verifies the hashCode contract: equal objects must produce the same hash code.
     */
    @Test
    public void hashCodeSameValuesTest() {
        EmailAddress email1 = new EmailAddress("user@example.com");
        EmailAddress email2 = new EmailAddress("user@example.com");

        assertEquals(email1.hashCode(), email2.hashCode());
    }

    /**
     * Verifies that different email addresses produce different hash codes.
     */
    @Test
    public void hashCodeDifferentValuesTest() {
        EmailAddress email1 = new EmailAddress("user@example.com");
        EmailAddress email2 = new EmailAddress("other@example.com");

        assertNotEquals(email1.hashCode(), email2.hashCode());
    }

    /**
     * Verifies that the toString representation correctly contains the email address string.
     */
    @Test
    public void toStringContainsEmailTest() {
        String str = emailAddress.toString();
        assertTrue(str.contains("user@example.com"));
    }
}