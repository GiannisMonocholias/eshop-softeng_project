package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeMenu;

import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuView;

public class CustomerServiceEmployeeMenuViewStub implements CustomerServiceMenuView {

    private String shownEmployeeName = "";
    private String navigateToOrderStatusId = "";
    private String navigateToInboxId = "";
    private String navigateToProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    @Override
    public void showEmployeeName(String fullName) {
        this.shownEmployeeName = fullName;
    }

    @Override
    public void navigateToOrderStatus(String employeeId) {
        this.navigateToOrderStatusId = employeeId;
    }

    @Override
    public void navigateToEmailInbox(String employeeId) {
        this.navigateToInboxId = employeeId;
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
    public void navigateToProcessAccount(String employeeId) {
        this.navigateToProcessAccountId = employeeId;
    }

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }


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