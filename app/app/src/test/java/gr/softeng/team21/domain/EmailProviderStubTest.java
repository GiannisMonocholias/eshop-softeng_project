package gr.softeng.team21.domain;

import org.junit.Test;
import static org.junit.Assert.*; // Χρησιμοποιούμε μόνο JUnit 4 Assertions

import java.util.ArrayList;

public class EmailProviderStubTest {

    private final EmailAddress from = new EmailAddress("sender@example.com");
    private final EmailAddress to = new EmailAddress("recipient@example.com");


    @Test
    public void testSaveInboxEmailsAndGetInbox() {

        EmailProviderStub provider = new EmailProviderStub();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body");

        provider.saveInboxEmails(msg); // Save the msg into provider's inbox email list

        // Check if the new msg was saved into provider's inbox
        ArrayList<EmailMessage> inbox = provider.getInboxEmails();
        assertEquals(1, inbox.size());
        assertEquals(msg, inbox.get(0));

        // Check that the getInboxEmails method returns a copy of the inbox email list
        assertNotSame(inbox, provider.getInboxEmails());
    }

    @Test
    public void testSaveSentEmailsAndGetSent() {
        EmailProviderStub provider = new EmailProviderStub();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body");

        provider.saveSentEmails(msg); // Save the msg into provider's sent emails list

        // Check if the new msg was saved into provider's sent emails list
        ArrayList<EmailMessage> sent = provider.getSentEmails();
        assertEquals(1, sent.size());
        assertEquals(msg, sent.get(0));

        // Check that the getSentEmails method returns a copy of the sent email list
        assertNotSame(sent, provider.getSentEmails());
    }

    @Test
    public void testGetUnreadEmails() {
        EmailProviderStub provider = new EmailProviderStub();

        EmailMessage unreadMsg = new EmailMessage(from, to, "Unread", "Body");
        EmailMessage readMsg = new EmailMessage(from, to, "Read", "Body");
        readMsg.setRead(true);

        provider.saveInboxEmails(unreadMsg);
        provider.saveInboxEmails(readMsg);

        ArrayList<EmailMessage> unread = provider.getUnreadEmails();
        assertEquals(1, unread.size());
        assertEquals(unreadMsg, unread.get(0));
    }

    @Test
    public void testGetReadEmails() {
        EmailProviderStub provider = new EmailProviderStub();

        EmailMessage readMsg = new EmailMessage(from, to, "Read", "Body");
        readMsg.setRead(true);

        EmailMessage unreadMsg = new EmailMessage(from, to, "Unread", "Body");

        provider.saveInboxEmails(readMsg);
        provider.saveInboxEmails(unreadMsg);

        ArrayList<EmailMessage> readEmails = provider.getReadEmails();
        assertEquals(1, readEmails.size());
        assertEquals(readMsg, readEmails.get(0));
    }

    @Test
    public void testGetUnrepliedEmails() {
        EmailProviderStub provider = new EmailProviderStub();

        EmailMessage unrepliedMsg = new EmailMessage(from, to, "Unreplied", "Body");
        EmailMessage repliedMsg = new EmailMessage(from, to, "Replied", "Body");
        repliedMsg.setReplied(true);

        provider.saveInboxEmails(unrepliedMsg);
        provider.saveInboxEmails(repliedMsg);

        ArrayList<EmailMessage> unreplied = provider.getUnrepliedEmails();
        assertEquals(1, unreplied.size());
        assertEquals(unrepliedMsg, unreplied.get(0));
    }

    @Test
    public void testGetRepliedEmails() {
        EmailProviderStub provider = new EmailProviderStub();

        EmailMessage repliedMsg = new EmailMessage(from, to, "Replied", "Body");
        repliedMsg.setReplied(true);

        EmailMessage unrepliedMsg = new EmailMessage(from, to, "Unreplied", "Body");

        provider.saveInboxEmails(repliedMsg);
        provider.saveInboxEmails(unrepliedMsg);

        ArrayList<EmailMessage> repliedEmails = provider.getRepliedEmails();
        assertEquals(1, repliedEmails.size());
        assertEquals(repliedMsg, repliedEmails.get(0));
    }


    @Test
    public void testInInbox() {
        EmailProviderStub provider = new EmailProviderStub();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body");

        provider.saveInboxEmails(msg);

        assertTrue(provider.inInbox(msg));
        assertFalse(provider.inInbox(new EmailMessage(from, to, "Other", "Body")));
    }


    @Test
    public void testInSent() {
        EmailProviderStub provider = new EmailProviderStub();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body");

        provider.saveSentEmails(msg);

        assertTrue(provider.inSent(msg));
        assertFalse(provider.inSent(new EmailMessage(from, to, "Other", "Body")));
    }

}