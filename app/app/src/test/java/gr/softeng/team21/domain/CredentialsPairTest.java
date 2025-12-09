package gr.softeng.team21.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class CredentialsPairTest {

    @Test
    public void testConstructorValidValues() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertEquals("user1", cp.getUsername());
        assertEquals("pass1", cp.getPassword());
    }

    @Test(expected = IllegalArgumentException.class) // Χρήση @Test(expected=...)
    public void testConstructorNullUsernameThrowsException() {
        new CredentialsPair(null, "pass");
    }

    @Test(expected = IllegalArgumentException.class) // Χρήση @Test(expected=...)
    public void testConstructorNullPasswordThrowsException() {
        new CredentialsPair("user", null);
    }

    @Test
    public void testEqualsSameObject() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertTrue(cp.equals(cp));
    }

    @Test
    public void testEqualsDifferentObjectSameValues() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user1", "pass1");
        assertTrue(cp1.equals(cp2));
    }

    @Test
    public void testEqualsDifferentUsername() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user2", "pass1");
        assertFalse(cp1.equals(cp2));
    }

    @Test
    public void testEqualsDifferentPassword() {
        CredentialsPair cp1 = new CredentialsPair("user1", "pass1");
        CredentialsPair cp2 = new CredentialsPair("user1", "pass2");
        assertFalse(cp1.equals(cp2));
    }

    @Test
    public void testEqualsNull() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertFalse(cp.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        CredentialsPair cp = new CredentialsPair("user1", "pass1");
        assertFalse(cp.equals("some string"));
    }

}