package gr.softeng.team21.view.contact.editdata.Password;

/**
 * Stub implementation of {@link PasswordView} for testing purposes.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI updates (password field) during the password editing flow.
 * @author PAVLOS GRATSANIS
 */
public class PasswordViewStub implements PasswordView {
    private String message;
    private String password;

    /**
     * Returns the password.
     * Used for verification in tests.
     * @return The password string.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the last message (success or error).
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
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
     * Stores the password in a local variable to simulate the UI population.
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     *Not used in tests
     */
    @Override
    public void finishView() {
    }
}