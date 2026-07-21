package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeMenu;

import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuView;

/**
 * A stub implementation of the {@link CustomerServiceMenuView} interface for unit testing.
 * It simulates the main menu interface for a Customer Service Employee, capturing
 * asynchronous navigation events and UI state changes for verification.
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
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullName) {
        this.shownEmployeeName = fullName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToOrderStatus(String employeeId) {
        this.navigateToOrderStatusId = employeeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToEmailInbox(String employeeId) {
        this.navigateToInboxId = employeeId;
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
        this.navigateToProcessAccountId = employeeId;
    }

    /**
     * {@inheritDoc}
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