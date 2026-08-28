package gr.softeng.team21.memorydao;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Unit tests for the {@link EmailDAOMemory} class.
 * This suite verifies the functionality of the in-memory email repository,
 * accommodating the asynchronous {@link java.util.concurrent.CompletableFuture}
 * contract by utilizing the {@code .join()} method for test assertions and saves.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOMemoryTest {

    private final EmailAddress from = new EmailAddress("sender@example.com");
    private final EmailAddress to = new EmailAddress("recipient@example.com");

    /**
     * Verifies that messages saved to the inbox are correctly stored and retrieved.
     */
    @Test
    public void testSaveInboxEmailsAndGetInbox() {
        EmailDAOMemory provider = new EmailDAOMemory();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        // Use .join() since saveInboxEmails now returns a CompletableFuture<Void>
        provider.saveInboxEmails(msg).join();

        ArrayList<EmailMessage> inbox = provider.getInboxEmails().join();
        assertEquals(1, inbox.size());
        assertEquals(msg, inbox.get(0));
    }

    /**
     * Verifies that messages saved to the sent folder are correctly stored and retrieved.
     */
    @Test
    public void testSaveSentEmailsAndGetSent() {
        EmailDAOMemory provider = new EmailDAOMemory();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        // Use .join() since saveSentEmails now returns a CompletableFuture<Void>
        provider.saveSentEmails(msg).join();

        ArrayList<EmailMessage> sent = provider.getSentEmails().join();
        assertEquals(1, sent.size());
        assertEquals(msg, sent.get(0));
    }

    /**
     * Tests the filtering logic for retrieving only unread messages from the inbox.
     */
    @Test
    public void testGetUnreadEmails() {
        EmailDAOMemory provider = new EmailDAOMemory();

        EmailMessage unreadMsg = new EmailMessage(from, to, "Unread", "Body", new Date());
        EmailMessage readMsg = new EmailMessage(from, to, "Read", "Body", new Date());
        readMsg.setRead(true);

        provider.saveInboxEmails(unreadMsg).join();
        provider.saveInboxEmails(readMsg).join();

        ArrayList<EmailMessage> unread = provider.getUnreadEmails().join();
        assertEquals(1, unread.size());
        assertEquals(unreadMsg, unread.get(0));
    }

    /**
     * Tests the filtering logic for retrieving only read messages from the inbox.
     */
    @Test
    public void testGetReadEmails() {
        EmailDAOMemory provider = new EmailDAOMemory();

        EmailMessage readMsg = new EmailMessage(from, to, "Read", "Body", new Date());
        readMsg.setRead(true);
        EmailMessage unreadMsg = new EmailMessage(from, to, "Unread", "Body", new Date());

        provider.saveInboxEmails(readMsg).join();
        provider.saveInboxEmails(unreadMsg).join();

        ArrayList<EmailMessage> readEmails = provider.getReadEmails().join();
        assertEquals(1, readEmails.size());
        assertEquals(readMsg, readEmails.get(0));
    }

    /**
     * Tests the filtering logic for retrieving messages that have not yet been replied to.
     */
    @Test
    public void testGetUnrepliedEmails() {
        EmailDAOMemory provider = new EmailDAOMemory();

        EmailMessage unrepliedMsg = new EmailMessage(from, to, "Unreplied", "Body", new Date());
        EmailMessage repliedMsg = new EmailMessage(from, to, "Replied", "Body", new Date());
        repliedMsg.setReplied(true);

        provider.saveInboxEmails(unrepliedMsg).join();
        provider.saveInboxEmails(repliedMsg).join();

        ArrayList<EmailMessage> unreplied = provider.getUnrepliedEmails().join();
        assertEquals(1, unreplied.size());
        assertEquals(unrepliedMsg, unreplied.get(0));
    }

    /**
     * Tests the filtering logic for retrieving only the messages that have been replied to.
     */
    @Test
    public void testGetRepliedEmails() {
        EmailDAOMemory provider = new EmailDAOMemory();

        EmailMessage repliedMsg = new EmailMessage(from, to, "Replied", "Body", new Date());
        repliedMsg.setReplied(true);
        EmailMessage unrepliedMsg = new EmailMessage(from, to, "Unreplied", "Body", new Date());

        provider.saveInboxEmails(repliedMsg).join();
        provider.saveInboxEmails(unrepliedMsg).join();

        ArrayList<EmailMessage> repliedEmails = provider.getRepliedEmails().join();
        assertEquals(1, repliedEmails.size());
        assertEquals(repliedMsg, repliedEmails.get(0));
    }

    /**
     * Verifies the search functionality to check if a specific message exists in the inbox.
     */
    @Test
    public void testInInbox() {
        EmailDAOMemory provider = new EmailDAOMemory();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        provider.saveInboxEmails(msg).join();

        assertTrue(provider.inInbox(msg).join());
        assertFalse(provider.inInbox(new EmailMessage(from, to, "Other", "Body", new Date())).join());
    }

    /**
     * Verifies the search functionality to check if a specific message exists in the sent folder.
     */
    @Test
    public void testInSent() {
        EmailDAOMemory provider = new EmailDAOMemory();
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        provider.saveSentEmails(msg).join();

        assertTrue(provider.inSent(msg).join());
        assertFalse(provider.inSent(new EmailMessage(from, to, "Other", "Body", new Date())).join());
    }
}