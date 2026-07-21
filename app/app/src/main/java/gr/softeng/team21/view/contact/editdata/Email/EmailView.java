package gr.softeng.team21.view.contact.editdata.Email;

/**
 * Interface for the Email Edit View.
 * Defines the methods for displaying messages and updating the email form.
 * @author PAVLOS GRATSANIS
 */
public interface EmailView {
    /**
     * Displays a success message to the user.
     * @param message The message to display.
     */
    void SaveSuccess(String message);

    /**
     * Displays an error message to the user.
     * @param message The message to display.
     */
    void showError(String message);

    /**
     * Completes the view with the user's current email address.
     * @param email The email address string.
     */
    void setEmail(String email);

    /**
     * Finishes the current activity.
     */
    void finishView();
}