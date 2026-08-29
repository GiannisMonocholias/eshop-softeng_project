package gr.softeng.team21.view.user.emailComposition;

/**
 * View interface contract for the Email Composition screen.
 * Defines methods for retrieving user input, setting sender details dynamically,
 * and providing validation feedback or process status messages asynchronously.
 * @author Γιάννης Μονοχολιάς
 */
public interface EmailCompositionView {

    /**
     * Retrieves the recipient email address entered by the user.
     * @return The recipient's email address string.
     */
    String getRecipientEmail();

    /**
     * Retrieves the subject line entered by the user.
     * @return The email subject string.
     */
    String getSubject();

    /**
     * Retrieves the main body text of the email entered by the user.
     * @return The email body string.
     */
    String getBody();

    /**
     * Updates the UI to display the name and email of the currently logged-in sender.
     * @param name  The full name of the sender.
     * @param email The email address of the sender.
     */
    void setSenderDetails(String name, String email);

    /**
     * Highlights validation errors on specific input fields (e.g., missing subject, invalid email).
     * @param field The name identifier of the field (e.g., "recipient", "subject", "body").
     * @param message The specific error message to display on the UI field.
     */
    void showInputError(String field, String message);

    /**
     * Displays a general error alert dialog for system or database failures.
     * @param message The error description message.
     */
    void showErrorMessage(String message);

    /**
     * Displays a success alert indicating the email was dispatched.
     * @param message The success description message.
     */
    void showSuccessMessage(String message);

    /**
     * Terminates the current activity and returns the user to the previous screen.
     */
    void finishActivity();
}