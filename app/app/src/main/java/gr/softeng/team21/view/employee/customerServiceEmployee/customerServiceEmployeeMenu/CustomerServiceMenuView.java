package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

/**
 * Defines methods to be implemented for displaying profile information, handling navigation
 * to various functional modules, and managing account-related alerts.
 * Defines the contract between the Presenter and the UI.
 * @author Γιάννης Μονοχολιάς
 */
public interface CustomerServiceMenuView {

    /**
     * Displays the full name of the employee on the header section of the menu.
     * @param fullName The concatenated first and last name to be displayed.
     */
    void showEmployeeName(String fullName);

    /**
     * Navigates the user to the Order Status notification screen.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToOrderStatus(String employeeId);

    /**
     * Navigates the user to their email inbox screen.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToEmailInbox(String employeeId);

    /**
     * Displays a confirmation dialog before proceeding with permanent account deletion.
     */
    void showDeleteAccountConfirmation();

    /**
     * Navigates the user back to the Login screen after a successful logout
     * or account deletion.
     */
    void navigateToLogin();

    /**
     * Navigates the user to the account data modification screen.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToProcessAccount(String employeeId);

    /**
     * Displays a temporary notification message (Toast) to the user.
     * @param message The text content of the notification.
     */
    void showMessage(String message);
}