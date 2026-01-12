package gr.softeng.team21.view.contact.editdata.Username;

/**
 * Interface for the Username Edit .
 * Defines the methods for displaying messages and updating the username form.
 * @author PAVLOS GRATSANIS
 */
public interface UsernameView {
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
     * Completes the view with the user's current username.
     * @param username The username string.
     */
    void setUsername(String username);

    /**
     * Finishes the current activity.
     */
    void finishView();
}