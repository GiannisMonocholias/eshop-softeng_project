package gr.softeng.team21.view.user.emailComposition;

/**
 * Defines the methods for capturing message input and providing
 * feedback during the sending process.
 * @author Γιάννης Μονοχολιάς
 */
public interface EmailCompositionView {

    /**
     * @return recipient email input
     */
    String getRecipientEmail();

    /**
     * @return email subject input
     */
    String getSubject();

    /**
     * @return email body input
     */
    String getBody();

    /**
     * Displays the sender's profile information in the UI header.
     * @param name  The full name of the logged-in user.
     * @param email The email address of the logged-in user.
     */
    void setSenderDetails(String name, String email);

    /**
     * Displays an error message, usually via a Toast or Alert.
     * @param message The error description.
     */
    void showErrorMessage(String message);

    /**
     * Displays a success confirmation message.
     * @param message The success description.
     */
    void showSuccessMessage(String message);

    /**
     * Terminates the composition activity.
     */
    void finishActivity();
}