package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

/**
 * Defines the navigation methods for managing catalogue update requests
 * and user account operations asynchronously.
 *
 * @author Γιάννης Μονοχολιάς
 */
public interface UpdateCatalogueEmployeeMenuView {

    /**
     * Updates the UI header with the employee's full name asynchronously.
     * @param fullName The employee's concatenated first and last name.
     */
    void showEmployeeName(String fullName);

    /**
     * Asynchronously loads and displays the employee's profile image.
     * @param profileImageUrl The download URL of the profile image stored in Cloud Storage.
     */
    void loadProfileImage(String profileImageUrl);

    /**
     * Navigates to the list of requests already assigned to this employee.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToAssignedRequests(String employeeId);

    /**
     * Navigates to the list of unassigned requests available for pickup.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToAvailableRequestsToAssign(String employeeId);

    /**
     * Displays a confirmation alert before proceeding with permanent account deletion.
     */
    void showDeleteAccountConfirmation();

    /**
     * Redirects the user to the Login screen.
     */
    void navigateToLogin();

    /**
     * Navigates to the user profile modification screen.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToProcessAccount(String employeeId);

    /**
     * Displays a transient notification (Toast) to the user.
     * @param message The text content of the notification.
     */
    void showMessage(String message);
}