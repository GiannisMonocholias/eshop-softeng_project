package gr.softeng.team21.view.employee.deliverer.delivererMenu;

/**
 * A stub implementation of the {@link DelivererMenuView} interface for unit testing.
 * It simulates the main menu interface for a Deliverer, capturing navigation
 * events and UI state transitions for verification during test execution.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuViewStub implements DelivererMenuView {

    private String shownName = "";
    private String navigatedOrdersListId = "";
    private String navigatedProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    /**
     * Captures the display of the deliverer's full name.
     * @param fullName The name string to be displayed.
     */
    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    /**
     * Captures navigation to the orders list screen.
     * @param employeeId The ID of the deliverer.
     */
    @Override
    public void navigateToOrdersList(String employeeId) {
        this.navigatedOrdersListId = employeeId;
    }

    /**
     * Marks that the account deletion confirmation dialog was requested.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        this.deleteConfirmationShown = true;
    }

    /**
     * Marks that navigation to the login screen was triggered (e.g., after deletion).
     */
    @Override
    public void navigateToLogin() {
        this.navigateToLoginCalled = true;
    }

    /**
     * Captures navigation to the account processing/edit screen.
     * @param employeeId The ID of the deliverer.
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigatedProcessAccountId = employeeId;
    }

    /**
     * Captures generic messages or toasts shown on the UI.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Accessor methods for verification during assertions ---

    public String getShownName() {
        return shownName;
    }

    public String getNavigatedOrdersListId() {
        return navigatedOrdersListId;
    }

    public String getNavigatedProcessAccountId() {
        return navigatedProcessAccountId;
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