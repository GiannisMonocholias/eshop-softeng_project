package gr.softeng.team21.view.contact.emailDetails;

/**
 * Defines the methods required to display the full metadata and content
 * of a specific email message.
 * @author Γιάννης Μονοχολιάς
 */
public interface EmailDetailsView {

    /**
     * Updates the UI to show the email's subject line.
     * @param subject The subject text of the email.
     */
    void displaySubject(String subject);

    /**
     * Displays the human-readable name of the sender.
     * This may include role labels (e.g., "John Doe (Customer)").
     * @param sender The resolved name of the sender.
     */
    void displaySenderName(String sender);

    /**
     * Displays the human-readable name of the receiver.
     * @param receiver The resolved name of the receiver.
     */
    void displayReceiverName(String receiver);

    /**
     * Displays the main content or message body of the email.
     * @param body The text content of the message.
     */
    void displayBody(String body);

    /**
     * Displays the raw email address of the sender.
     * @param email The sender's email address string.
     */
    void displaySenderEmail(String email);

    /**
     * Displays the raw email address of the receiver.
     * @param email The receiver's email address string.
     */
    void displayReceiverEmail(String email);
}