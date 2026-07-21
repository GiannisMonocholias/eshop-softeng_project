package gr.softeng.team21.view.employee.deliverer.delivererMenu;

/**
 * A stub implementation of the {@link DelivererMenuView} interface for unit testing.
 * It simulates the main menu interface for a Deliverer, capturing asynchronous navigation
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
    public void navigateToOrdersList(String employeeId) {
        this.navigatedOrdersListId = employeeId;
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