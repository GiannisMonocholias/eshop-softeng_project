package gr.softeng.team21.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link EmailMessage} domain class.
 * This suite verifies the state management of email messages, including read/reply
 * statuses, sender/recipient integrity, document IDs, and content manipulation.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailMessageTest {
    private final EmailAddress from = new EmailAddress("sender@example.com");
    private final EmailAddress to = new EmailAddress("recipient@example.com");

    /**
     * Verifies that the unique document identifier (emailId) can be correctly set and retrieved.
     */
    @Test
    public void getEmailIdAndSetEmailIdTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertNull("Default emailId should be null before saving", msg.getEmailId());

        msg.setEmailId("DOC-12345");
        assertEquals("DOC-12345", msg.getEmailId());
    }

    /**
     * Verifies that a newly created email message has its replied status
     * set to false by default.
     */
    @Test
    public void isRepliedDefaultFalse() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplied());
    }

    /**
     * Tests the setter and getter for the replied status.
     */
    @Test
    public void setRepliedTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        assertFalse(msg.isReplied());
        msg.setReplied(true);
        assertTrue(msg.isReplied());
    }

    /**
     * Verifies that a newly created email message has its read status
     * set to false by default.
     */
    @Test
    public void isReadDefaultFalseTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isRead());
    }

    /**
     * Tests the setter and getter for the read status.
     */
    @Test
    public void setReadTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        assertFalse(msg.isRead());
        msg.setRead(true);
        assertTrue(msg.isRead());
    }

    /**
     * Verifies that a newly created email message is not marked as a
     * reply message by default.
     */
    @Test
    public void isReplyMessageDefaultFalseTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplyMessage());
    }

    /**
     * Tests the setter and getter for the reply message flag.
     */
    @Test
    public void setReplyMessageTrueTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplyMessage());
        msg.setReplyMessage(true);
        assertTrue(msg.isReplyMessage());
    }

    /**
     * Tests the sender (from) property accessors.
     */
    @Test
    public void getFromAndSetFromTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        EmailAddress newFrom = new EmailAddress("new@example.com");
        msg.setFrom(newFrom);
        assertEquals(newFrom, msg.getFrom());
    }

    /**
     * Verifies that setting a null sender address throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setFromNullArgumentTest(){
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setFrom(null);
    }

    /**
     * Tests the email body property accessors.
     */
    @Test
    public void testGetBodyAndSetBody() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setBody("New Body");
        assertEquals("New Body", msg.getBody());
    }

    /**
     * Tests the email subject property accessors.
     */
    @Test
    public void testGetSubjectAndSetSubject() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setSubject("New Subject");
        assertEquals("New Subject", msg.getSubject());
    }

    /**
     * Tests the recipient (to) property accessors.
     */
    @Test
    public void testGetToAndSetTo() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        EmailAddress newTo = new EmailAddress("newrecipient@example.com");
        msg.setTo(newTo);
        assertEquals(newTo, msg.getTo());
    }

    /**
     * Verifies that setting a null recipient address throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setToNullArgumentTest(){
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setTo(null);
    }

    /**
     * Tests the functionality of appending text to the existing email body.
     */
    @Test
    public void appendToBody() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.appendToBody(" + Extra");
        assertEquals("Body + Extra", msg.getBody());
    }
}