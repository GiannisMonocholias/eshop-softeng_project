package gr.softeng.team21.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link CredentialsPair} class.
 * This suite ensures the integrity of user authentication tokens, covering
 * successful initialization, null-value protection, and equality logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CredentialsPairTest {

    /**
     * Verifies that the constructor correctly assigns valid username and password values.
     */
    @Test
    public void testConstructorValidValues() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertEquals("user1", cp.getUsername());
        assertEquals("pass1", cp.getPassword());
    }

    /**
     * Verifies that the constructor throws an {@link IllegalArgumentException}
     * when a null username is provided.
     */
    @Test(expected = IllegalArgumentException.class) // Χρήση @Test(expected=...)
    public void testConstructorNullUsernameThrowsException() {
        new CredentialsPair(null, "pass");
    }

    /**
     * Verifies that the constructor throws an {@link IllegalArgumentException}
     * when a null password is provided.
     */
    @Test(expected = IllegalArgumentException.class) // Χρήση @Test(expected=...)
    public void testConstructorNullPasswordThrowsException() {
        new CredentialsPair("user", null);
    }

    /**
     * Verifies that the equals method returns true when comparing an object
     * with itself (Reflexivity).
     */
    @Test
    public void testEqualsSameObject() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertTrue(cp.equals(cp));
    }

    /**
     * Verifies that two different CredentialsPair instances with identical
     * username and password values are considered equal.
     */
    @Test
    public void testEqualsDifferentObjectSameValues() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user1", "pass1");
        assertTrue(cp1.equals(cp2));
    }

    /**
     * Verifies that two objects with different usernames are not equal.
     */
    @Test
    public void testEqualsDifferentUsername() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user2", "pass1");
        assertFalse(cp1.equals(cp2));
    }

    /**
     * Verifies that two objects with different passwords are not equal.
     */
    @Test
    public void testEqualsDifferentPassword() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user1", "pass2");
        assertFalse(cp1.equals(cp2));
    }

    /**
     * Verifies that comparing a CredentialsPair instance with null returns false.
     */
    @Test
    public void testEqualsNull() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertFalse(cp.equals(null));
    }

    /**
     * Verifies that comparing a CredentialsPair instance with an object
     * of a different class returns false.
     */
    @Test
    public void testEqualsDifferentClass() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertFalse(cp.equals("some string"));
    }

}