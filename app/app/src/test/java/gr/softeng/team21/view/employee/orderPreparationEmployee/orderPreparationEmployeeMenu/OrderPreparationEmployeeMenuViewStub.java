package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

/**
 * A stub implementation of the {@link OrdersPreparationEmployeeMenuView} interface for unit testing.
 * It simulates the main menu interface for an Order Preparation Employee, capturing
 * navigation requests and UI feedback state for assertion purposes.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployeeMenuViewStub implements OrdersPreparationEmployeeMenuView {

    private String shownName = "";
    private String navigatedAssignedOrdersId = "";
    private String navigatedAvailableOrdersId = "";
    private String navigatedProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    /**
     * Captures the employee name display request.
     * @param fullName The full name to be shown on the menu.
     */
    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    /**
     * Captures navigation to the screen displaying orders already assigned to the employee.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToAssignedOrders(String employeeId) {
        this.navigatedAssignedOrdersId = employeeId;
    }

    /**
     * Captures navigation to the screen where new available orders can be assigned.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToAvailableOrdersToAssign(String employeeId) {
        this.navigatedAvailableOrdersId = employeeId;
    }

    /**
     * Captures navigation to the account editing screen.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigatedProcessAccountId = employeeId;
    }

    /**
     * Marks that the account deletion confirmation dialog was requested to be shown.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        this.deleteConfirmationShown = true;
    }

    /**
     * Marks that the navigation back to the login screen was triggered.
     */
    @Override
    public void navigateToLogin() {
        this.navigateToLoginCalled = true;
    }

    /**
     * Captures generic feedback or error messages sent to the view.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Accessor methods for verification during testing ---

    public String getShownName() {
        return shownName;
    }

    public String getNavigatedAssignedOrdersId() {
        return navigatedAssignedOrdersId;
    }

    public String getNavigatedAvailableOrdersId() {
        return navigatedAvailableOrdersId;
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