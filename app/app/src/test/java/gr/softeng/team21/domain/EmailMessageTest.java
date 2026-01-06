package gr.softeng.team21.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.util.Date;

public class EmailMessageTest {
    private final EmailAddress from = new EmailAddress("sender@example.com");
    private final EmailAddress to = new EmailAddress("recipient@example.com");

    @Test
    public void isRepliedDefaultFalse() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplied());
    }

    @Test
    public void setRepliedTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        assertFalse(msg.isReplied());
        msg.setReplied(true);
        assertTrue(msg.isReplied());
    }

    @Test
    public void isReadDefaultFalseTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isRead());
    }

    @Test
    public void setReadTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        assertFalse(msg.isRead());
        msg.setRead(true);
        assertTrue(msg.isRead());
    }

    @Test
    public void isReplyMessageDefaultFalseTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplyMessage());
    }

    @Test
    public void setReplyMessageTrueTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        assertFalse(msg.isReplyMessage());
        msg.setReplyMessage(true);
        assertTrue(msg.isReplyMessage());
    }

    @Test
    public void getFromAndSetFromTest() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        EmailAddress newFrom = new EmailAddress("new@example.com");
        msg.setFrom(newFrom);
        assertEquals(newFrom, msg.getFrom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFromNullArgumentTest(){
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setFrom(null);
    }


    @Test
    public void testGetBodyAndSetBody() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setBody("New Body");
        assertEquals("New Body", msg.getBody());
    }

    @Test
    public void testGetSubjectAndSetSubject() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setSubject("New Subject");
        assertEquals("New Subject", msg.getSubject());
    }



    @Test
    public void testGetToAndSetTo() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        EmailAddress newTo = new EmailAddress("newrecipient@example.com");
        msg.setTo(newTo);
        assertEquals(newTo, msg.getTo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setToNullArgumentTest(){
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.setTo(null);
    }

    @Test
    public void appendToBody() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());
        msg.appendToBody(" + Extra");
        assertEquals("Body + Extra", msg.getBody());
    }
}