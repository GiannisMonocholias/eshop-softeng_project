package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

/**
 * View contract for the Order Preparation Employee main menu.
 * Defines the navigation methods and UI interactions available from the dashboard.
 * @author Γιάννης Μονοχολιάς
 */
public interface OrdersPreparationEmployeeMenuView {

    /**
     * Displays the full name of the employee on the header.
     * @param fullName The employee's full name.
     */
    void showEmployeeName(String fullName);

    /**
     * Navigates to the list of orders already assigned to this employee.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToAssignedOrders(String employeeId);

    /**
     * Navigates to the pool of new orders available for pickup/assignment.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToAvailableOrdersToAssign(String employeeId);

    /**
     * Navigates to the account settings screen.
     * @param employeeId The unique identifier of the employee.
     */
    void navigateToProcessAccount(String employeeId);

    /**
     * Displays a confirmation dialog before proceeding with account deletion.
     */
    void showDeleteAccountConfirmation();

    /**
     * Redirects the user to the Login screen.
     */
    void navigateToLogin();

    /**
     * Displays a temporary notification message (Toast).
     * @param message The content of the message.
     */
    void showMessage(String message);
}