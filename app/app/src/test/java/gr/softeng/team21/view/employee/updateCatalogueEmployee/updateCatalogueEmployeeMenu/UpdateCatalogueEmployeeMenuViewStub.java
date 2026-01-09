package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

/**
 * A stub implementation of the {@link UpdateCatalogueEmployeeMenuView} interface for unit testing.
 * It simulates the main menu interface for an Update Catalogue Employee, capturing
 * navigation requests and UI feedback state for assertion purposes.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeMenuViewStub implements UpdateCatalogueEmployeeMenuView {

    private String shownName = "";
    private String navigatedAssignedRequestsId = "";
    private String navigatedAvailableRequestsId = "";
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
     * Captures navigation to the screen displaying requests already assigned to the employee.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToAssignedRequests(String employeeId) {
        this.navigatedAssignedRequestsId = employeeId;
    }

    /**
     * Captures navigation to the screen where new catalogue update requests can be assigned.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToAvailableRequestsToAssign(String employeeId) {
        this.navigatedAvailableRequestsId = employeeId;
    }

    /**
     * Marks that the account deletion confirmation dialog was requested.
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
     * Captures navigation to the account editing screen.
     * @param employeeId The ID of the employee navigating.
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigatedProcessAccountId = employeeId;
    }

    /**
     * Captures generic feedback or error messages sent to the view.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Getters for Testing verification ---

    public String getShownName() {
        return shownName;
    }

    public String getNavigatedAssignedRequestsId() {
        return navigatedAssignedRequestsId;
    }

    public String getNavigatedAvailableRequestsId() {
        return navigatedAvailableRequestsId;
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