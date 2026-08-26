package gr.softeng.team21.view.customer.EmailList;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;

/**
 * The view interface for the Email List.
 * Defines the UI operations for displaying emails and handling navigation asynchronously.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerEmailListView {

    /**
     * Updates the UI with the retrieved list of emails.
     * @param emails The list of emails to display.
     */
    void showEmails(ArrayList<EmailMessage> emails);

    /**
     * Displays an error message to the user.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Navigates to the email composition screen.
     * @param customerId The ID of the customer sending the message.
     */
    void goToCreateNewMessge(String customerId);

    /**
     * Navigates to the details screen of a specific email.
     * @param subject The email subject.
     * @param body The email body content.
     * @param sender The sender's email address.
     * @param receiver The recipient's email address.
     * @param customerId The current user's ID for context.
     */
    void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId);
}