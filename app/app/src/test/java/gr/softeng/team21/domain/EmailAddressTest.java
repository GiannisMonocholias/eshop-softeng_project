package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*; // Χρησιμοποιούμε μόνο JUnit 4 Assertions

public class EmailAddressTest {

    private EmailAddress emailAddress;

    @Before // Αντικατάσταση του @BeforeEach
    public void setUp() {
        emailAddress = new EmailAddress("user@example.com");
    }

    @Test
    public void emailAddressGetterTest() {
        assertEquals("user@example.com", emailAddress.getAddress());
    }

    @Test
    public void setterTest() {
        emailAddress.setAddress("new@example.com");
        assertEquals("new@example.com", emailAddress.getAddress());
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue(emailAddress.equals(emailAddress));
    }

    @Test
    public void equalsSameValuesTest() {
        EmailAddress other = new EmailAddress("user@example.com");
        assertEquals(emailAddress, other);
    }

    @Test
    public void equalsDifferentValuesTest() {
        EmailAddress other = new EmailAddress("different@example.com");
        assertNotEquals(emailAddress, other);
    }

    @Test
    public void equalsNullTest() {
        assertNotEquals(emailAddress, null);
    }

    @Test
    public void equalsDifferentClassTest() {
        assertNotEquals(emailAddress, new Object());
    }

    @Test
    public void hashCodeSameValuesTest() {
        EmailAddress email1 = new EmailAddress("user@example.com");
        EmailAddress email2 = new EmailAddress("user@example.com");

        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    public void hashCodeDifferentValuesTest() {
        EmailAddress email1 = new EmailAddress("user@example.com");
        EmailAddress email2 = new EmailAddress("other@example.com");

        assertNotEquals(email1.hashCode(), email2.hashCode());
    }


    @Test
    public void toStringContainsEmailTest() {
        String str = emailAddress.toString();
        assertTrue(str.contains("user@example.com"));
    }
}