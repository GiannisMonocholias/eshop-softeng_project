package gr.softeng.team21.view.contact.editdata.Email;

/**
 * Stub implementation of {@link EmailView} for testing purposes.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI updates (email address) during the email editing flow.
 * @author PAVLOS GRATSANIS
 */
public class EmailViewStub implements EmailView {

    private String message;
    private String email;

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
     * Stores the email address in a variable to simulate the UI population.
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * {@inheritDoc}
     *  Not used in tests
     */
    @Override
    public void finishView() {

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
     * Returns the email address.
     * Used for verification in tests.
     * @return The email string.
     */
    public String getEmail() {
        return email;
    }
}