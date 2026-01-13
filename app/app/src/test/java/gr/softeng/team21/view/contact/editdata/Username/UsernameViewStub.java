package gr.softeng.team21.view.contact.editdata.Username;

/**
 * Stub implementation of {@link UsernameView} for testing purposes.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI updates (username) during the username editing flow.
 * @author PAVLOS GRATSANIS
 */
public class UsernameViewStub implements UsernameView {
    private String message;
    private String currentUsername;

    /**
     * Returns the last message (success or error).
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the username.
     * Used for verification in tests.
     * @return The username string.
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * {@inheritDoc}
     * Stores the success message in a variable for verification.
     */
    @Override
    public void SaveSuccess(String msg) {
        message = msg;
    }

    /**
     * {@inheritDoc}
     * Stores the error message in a variable for verification.
     */
    @Override
    public void showError(String msg) {
        message = msg;
    }

    /**
     * {@inheritDoc}
     * Stores the username in a local variable to simulate the UI population.
     */
    @Override
    public void setUsername(String username) {
        currentUsername = username;
    }

    /**
     * {@inheritDoc}
     * Not used in tests
     */
    @Override
    public void finishView() {

    }
}