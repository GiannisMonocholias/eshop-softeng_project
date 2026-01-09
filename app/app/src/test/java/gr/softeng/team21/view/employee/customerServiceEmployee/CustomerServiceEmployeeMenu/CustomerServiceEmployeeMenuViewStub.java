package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeMenu;

import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuView;

/**
 * A stub implementation of the {@link CustomerServiceMenuView} interface for unit testing.
 * It simulates the main menu interface for a Customer Service Employee, capturing
 * navigation events and UI state changes for verification.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeMenuViewStub implements CustomerServiceMenuView {

    private String shownEmployeeName = "";
    private String navigateToOrderStatusId = "";
    private String navigateToInboxId = "";
    private String navigateToProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    /**
     * Captures the employee name display request.
     * @param fullName The name to be displayed.
     */
    @Override
    public void showEmployeeName(String fullName) {
        this.shownEmployeeName = fullName;
    }

    /**
     * Captures the navigation event to the order status screen.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToOrderStatus(String employeeId) {
        this.navigateToOrderStatusId = employeeId;
    }

    /**
     * Captures the navigation event to the email inbox.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToEmailInbox(String employeeId) {
        this.navigateToInboxId = employeeId;
    }

    /**
     * Marks that the delete account confirmation dialog was requested.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        this.deleteConfirmationShown = true;
    }

    /**
     * Marks that the navigation to the login screen was called (e.g., after deletion).
     */
    @Override
    public void navigateToLogin() {
        this.navigateToLoginCalled = true;
    }

    /**
     * Captures the navigation event to the account processing/edit screen.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigateToProcessAccountId = employeeId;
    }

    /**
     * Captures generic feedback messages sent to the UI.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Accessor methods for verification during assertions ---

    public String getShownEmployeeName() {
        return shownEmployeeName;
    }

    public String getNavigateToOrderStatusId() {
        return navigateToOrderStatusId;
    }

    public String getNavigateToInboxId() {
        return navigateToInboxId;
    }

    public String getNavigateToProcessAccountId() {
        return navigateToProcessAccountId;
    }

    public boolean isDeleteConfirmationShown() {
        return deleteConfirmationShown;
    }

    public boolean isNavigateToLoginCalled() {
        return navigateToLoginCalled;
    }

    public String getMessageShown() {
        return messageShown;
    }
}