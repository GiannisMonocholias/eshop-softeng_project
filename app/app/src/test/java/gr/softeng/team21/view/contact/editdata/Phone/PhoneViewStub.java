package gr.softeng.team21.view.contact.editdata.Phone;

/**
 * Stub implementation of {@link PhoneView} for testing purposes.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI updates (phone number) during the phone editing flow.
 * @author PAVLOS GRATSANIS
 */
public class PhoneViewStub implements PhoneView {
    private String message;
    private String phone;

    /**
     * Returns the phone number.
     * Used for verification in tests.
     * @return The phone number string.
     */
    public String getPhone() {
        return phone;
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
    public void SaveSuccess(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     * Stores the error message in a variable for verification.
     */
    @Override
    public void showError(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     * Stores the phone number in a local variable to simulate the UI population.
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * {@inheritDoc}
     * Not used in tests
     */
    @Override
    public void finishView() {
    }
}