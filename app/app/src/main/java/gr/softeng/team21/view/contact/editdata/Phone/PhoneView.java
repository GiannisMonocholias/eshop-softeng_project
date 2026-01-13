package gr.softeng.team21.view.contact.editdata.Phone;

/**
 * Interface for the Phone Edit .
 * Defines the methods for displaying messages and updating the phone form.
 * @author PAVLOS GRATSANIS
 */
public interface PhoneView {
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
     * Completes the view with the user's current phone number.
     * @param phone The phone number string.
     */
    void setPhone(String phone);

    /**
     * Finishes the current activity.
     */
    void finishView();
}