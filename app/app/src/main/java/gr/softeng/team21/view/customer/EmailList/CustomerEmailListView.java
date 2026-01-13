package gr.softeng.team21.view.customer.EmailList;

/**
 * The view interface for the Email List.
 * Defines the methods that the email list activity must implement.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerEmailListView {
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