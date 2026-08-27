package gr.softeng.team21.view.customer.register;

/**
 * Defines the essential UI feedback and management operations for the
 * account creation process.
 * @author Γιάννης Μονοχολιάς
 */
public interface RegisterView {

    /**
     * Displays a success message to the user upon successful account creation.
     * @param message The confirmation message.
     */
    void showSuccessMessage(String message);

    /**
     * Displays an error message if the registration fails or validation is breached.
     * @param message The error description.
     */
    void showErrorMessage(String message);

    /**
     * Resets all input fields to their default state and refocuses the first field.
     */
    void clearInputFields();
}