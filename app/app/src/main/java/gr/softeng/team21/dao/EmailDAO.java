package gr.softeng.team21.dao;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;

/**
 * Data Access Object (DAO) interface for managing email messages.
 * Uses a single unified collection approach, delegating the sorting and filtering
 * (e.g., retrieving an inbox) to database-level queries rather than keeping separate lists.
 * <p>
 * This interface uses {@link CompletableFuture} for all data operations
 * to support asynchronous database implementations (e.g., Firebase) without
 * blocking the main UI thread.
 *
 * @author Γιάννης Μονοχολιάς
 */
public interface EmailDAO {

    /**
     * Retrieves all incoming emails specifically targeted to a given email address asynchronously.
     * This functionally acts as the "Inbox" query.
     *
     * @param receiverEmailAddress The exact email address of the recipient (to.address).
     * @return A CompletableFuture containing a list of EmailMessages for the user.
     */
    CompletableFuture<ArrayList<EmailMessage>> getEmailsForUser(String receiverEmailAddress);

    /**
     * Saves a new message to the centralized database collection asynchronously.
     *
     * @param msg The email message to be saved.
     * @return A CompletableFuture completing when the save operation is successful.
     */
    CompletableFuture<Void> saveEmail(EmailMessage msg);

    /**
     * Updates an existing email message in the centralized database collection asynchronously.
     * Primarily used for state changes (e.g., marking an email as read).
     *
     * @param msg The email message object containing the updated state and a valid emailId.
     * @return A CompletableFuture completing when the update operation is successful.
     */
    CompletableFuture<Void> updateEmail(EmailMessage msg);

    /**
     * Clears all stored emails from the repository asynchronously.
     * Primarily intended for resetting state during Unit Testing.
     *
     * @return A CompletableFuture representing the completion of the clearing operation.
     */
    CompletableFuture<Void> clear();
}