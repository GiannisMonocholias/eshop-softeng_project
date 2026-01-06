package gr.softeng.team21.view.employee.deliverer.delivererMenu;

public class DelivererMenuViewStub implements DelivererMenuView {

    private String shownName = "";
    private String navigatedOrdersListId = "";
    private String navigatedProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    @Override
    public void navigateToOrdersList(String employeeId) {
        this.navigatedOrdersListId = employeeId;
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
        this.navigatedProcessAccountId = employeeId;
    }

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }


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