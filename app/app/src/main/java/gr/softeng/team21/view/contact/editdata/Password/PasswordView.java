package gr.softeng.team21.view.contact.editdata.Password;

/**
 * Interface for the Password Edit.
 * Defines the methods for displaying messages and updating the password form.
 * @author PAVLOS GRATSANIS
 */
public interface PasswordView {
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
     * Completes the view with the user's current password.
     * @param password The password string.
     */
    void setPassword(String password);

    /**
     * Finishes the current activity.
     */
    void finishView();
}