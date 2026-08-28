package gr.softeng.team21.dao;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;

/**
 * Data Access Object (DAO) interface for managing email messages.
 * <p>
 * This interface uses {@link CompletableFuture} for all data retrieval operations
 * to support asynchronous database implementations (e.g., Firebase) without
 * blocking the main UI thread.
 *
 * @author Γιάννης Μονοχολιάς
 */
public interface EmailDAO {

    /**
     * Retrieves all emails currently stored in the user's inbox asynchronously.
     *
     * @return A CompletableFuture containing a list of inbox EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getInboxEmails();

    /**
     * Retrieves all emails that have been sent by the user asynchronously.
     *
     * @return A CompletableFuture containing a list of sent EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getSentEmails();

    /**
     * Filters and returns all emails in the inbox that have not been read yet.
     *
     * @return A CompletableFuture containing a list of unread EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getUnreadEmails();

    /**
     * Filters and returns all emails in the inbox that have already been read.
     *
     * @return A CompletableFuture containing a list of read EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getReadEmails();

    /**
     * Filters and returns all emails in the inbox that have not received a reply yet.
     *
     * @return A CompletableFuture containing a list of unreplied EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getUnrepliedEmails();

    /**
     * Filters and returns all emails in the inbox that have been replied to.
     *
     * @return A CompletableFuture containing a list of replied EmailMessages.
     */
    CompletableFuture<ArrayList<EmailMessage>> getRepliedEmails();


    /**
     * Saves a new message to the inbox storage in the database.
     * @param msg The email message to be saved.
     */
    CompletableFuture<Void> saveInboxEmails(EmailMessage msg);

    /**
     * Saves a new message to the sent messages storage in the database.
     * @param msg The email message to be saved.
     */
    CompletableFuture<Void> saveSentEmails(EmailMessage msg);


    /**
     * Checks if a specific message exists in the inbox.
     *
     * @param msg The email message to search for.
     * @return A CompletableFuture containing true if found, false otherwise.
     */
    CompletableFuture<Boolean> inInbox(EmailMessage msg);

    /**
     * Checks if a specific message exists in the sent items.
     *
     * @param msg The email message to search for.
     * @return A CompletableFuture containing true if found, false otherwise.
     */
    CompletableFuture<Boolean> inSent(EmailMessage msg);
}