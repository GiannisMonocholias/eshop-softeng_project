package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

/**
 * A stub implementation of the {@link UpdateCatalogueEmployeeMenuView} interface for unit testing.
 * It simulates the main menu interface for an Update Catalogue Employee, capturing
 * asynchronous navigation requests and UI feedback state for assertion purposes.
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
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAssignedRequests(String employeeId) {
        this.navigatedAssignedRequestsId = employeeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAvailableRequestsToAssign(String employeeId) {
        this.navigatedAvailableRequestsId = employeeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDeleteAccountConfirmation() {
        this.deleteConfirmationShown = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToLogin() {
        this.navigateToLoginCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigatedProcessAccountId = employeeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Getters for Testing verification ---

    public String getShownName() { return shownName; }
    public String getNavigatedAssignedRequestsId() { return navigatedAssignedRequestsId; }
    public String getNavigatedAvailableRequestsId() { return navigatedAvailableRequestsId; }
    public String getNavigatedProcessAccountId() { return navigatedProcessAccountId; }
    public boolean isDeleteConfirmationShown() { return deleteConfirmationShown; }
    public boolean isNavigateToLoginCalled() { return navigateToLoginCalled; }
    public String getMessageShown() { return messageShown; }
}