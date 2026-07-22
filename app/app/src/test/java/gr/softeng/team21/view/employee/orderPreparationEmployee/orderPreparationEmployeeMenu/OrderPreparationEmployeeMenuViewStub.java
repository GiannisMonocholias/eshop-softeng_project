package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

/**
 * A stub implementation of the {@link OrdersPreparationEmployeeMenuView} interface for unit testing.
 * It simulates the main menu interface for an Order Preparation Employee, capturing
 * navigation requests and UI feedback state for assertion purposes asynchronously.
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
    public void navigateToAssignedOrders(String employeeId) {
        this.navigatedAssignedOrdersId = employeeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAvailableOrdersToAssign(String employeeId) {
        this.navigatedAvailableOrdersId = employeeId;
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