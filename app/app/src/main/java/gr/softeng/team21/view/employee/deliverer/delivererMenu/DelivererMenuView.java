package gr.softeng.team21.view.employee.deliverer.delivererMenu;

/**
 * Defines the essential UI operations for displaying profile info,
 * navigating to delivery lists, and handling account lifecycle actions.
 * Acts as the contract between the Presenter and the Activity for asynchronous operations.
 * @author Γιάννης Μονοχολιάς
 */
public interface DelivererMenuView {

    /**
     * Updates the UI to display the Deliverer's full name.
     * @param fullName The concatenated first and last name of the employee.
     */
    void showEmployeeName(String fullName);

    /**
     * Navigates to the screen displaying orders assigned for delivery.
     * @param employeeId The unique identifier of the deliverer.
     */
    void navigateToOrdersList(String employeeId);

    /**
     * Displays a confirmation dialog to prevent accidental account deletion.
     */
    void showDeleteAccountConfirmation();

    /**
     * Redirects the user to the Login screen, typically after logout or account deletion.
     */
    void navigateToLogin();

    /**
     * Navigates to the account modification screen.
     * @param employeeId The unique identifier of the deliverer.
     */
    void navigateToProcessAccount(String employeeId);

    /**
     * Displays a transient notification message (Toast) to the user.
     * @param message The text content of the message.
     */
    void showMessage(String message);
}