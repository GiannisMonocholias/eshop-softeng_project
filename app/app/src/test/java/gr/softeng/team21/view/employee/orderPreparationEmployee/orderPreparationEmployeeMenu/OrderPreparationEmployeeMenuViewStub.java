package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

public class OrderPreparationEmployeeMenuViewStub implements OrdersPreparationEmployeeMenuView {

    private String shownName = "";
    private String navigatedAssignedOrdersId = "";
    private String navigatedAvailableOrdersId = "";
    private String navigatedProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    @Override
    public void navigateToAssignedOrders(String employeeId) {
        this.navigatedAssignedOrdersId = employeeId;
    }

    @Override
    public void navigateToAvailableOrdersToAssign(String employeeId) {
        this.navigatedAvailableOrdersId = employeeId;
    }

    @Override
    public void navigateToProcessAccount(String employeeId) {
        this.navigatedProcessAccountId = employeeId;
    }

    @Override
    public void showDeleteAccountConfirmation() {
        this.deleteConfirmationShown = true;
    }

    @Override
    public void navigateToLogin() {
        this.navigateToLoginCalled = true;
    }

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    // --- Getters for Testing ---

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