package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Unit tests for the {@link EmailDAOMemory} class.
 * This suite verifies the functionality of the in-memory email repository,
 * ensuring the centralized index accurately mimics Firestore's O(1) query behavior.
 * Accommodates the asynchronous {@link java.util.concurrent.CompletableFuture}
 * contract by utilizing the {@code .join()} method for test assertions.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOMemoryTest {

    private final EmailAddress from = new EmailAddress("sender@example.com");
    private final EmailAddress to = new EmailAddress("recipient@example.com");
    private EmailDAOMemory dao;

    /**
     * Prepares the Singleton DAO instance and clears any residual data before each test.
     */
    @Before
    public void setUp() {
        dao = EmailDAOMemory.getInstance();
        dao.clear().join();
    }

    /**
     * Verifies that an email is successfully saved and can be retrieved using
     * the recipient's exact email address via the index structure.
     */
    @Test
    public void testSaveEmailAndGetEmailsForUser() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        dao.saveEmail(msg).join();

        // Ensure the auto-increment ID was assigned during the save process
        assertNotNull(msg.getEmailId());

        // Fetch emails specifically directed to 'recipient@example.com'
        ArrayList<EmailMessage> retrievedEmails = dao.getEmailsForUser(to.getAddress()).join();

        assertEquals(1, retrievedEmails.size());
        assertEquals("Subject", retrievedEmails.get(0).getSubject());
        assertEquals(msg.getEmailId(), retrievedEmails.get(0).getEmailId());

        // Ensure queries for other addresses return empty lists
        ArrayList<EmailMessage> otherEmails = dao.getEmailsForUser("someoneelse@example.com").join();
        assertTrue("Query for unrelated email should return empty list", otherEmails.isEmpty());
    }

    /**
     * Verifies that updating an existing email message correctly reflects
     * the state changes (e.g., marking it as read) in the repository.
     */
    @Test
    public void testUpdateEmailState() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        // Initial Save
        dao.saveEmail(msg).join();
        assertFalse(msg.isRead());

        // Update the state locally
        msg.setRead(true);

        // Persist the update
        dao.updateEmail(msg).join();

        // Fetch again from DAO to verify persistence
        ArrayList<EmailMessage> retrievedEmails = dao.getEmailsForUser(to.getAddress()).join();

        assertEquals(1, retrievedEmails.size());
        assertTrue("The email should be marked as read in the DAO", retrievedEmails.get(0).isRead());
    }

    /**
     * Verifies that attempting to update an email without a valid database ID
     * throws the appropriate exception, enforcing data integrity.
     */
    @Test
    public void testUpdateEmailWithoutIdThrowsException() {
        EmailMessage msg = new EmailMessage(from, to, "Subject", "Body", new Date());

        // Intentionally NOT calling dao.saveEmail(msg) so emailId remains null

        try {
            dao.updateEmail(msg).join();
            fail("Expected an exception because the email lacks a valid emailId.");
        } catch (Exception e) {
            // Test passes if exception is thrown (usually wrapped in CompletionException)
            assertTrue(e.getCause() instanceof IllegalArgumentException || e instanceof IllegalArgumentException);
        }
    }

    /**
     * Cleans up the memory state to ensure strict test isolation.
     */
    @After
    public void tearDown() {
        dao.clear().join();
    }
}