package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

public class UpdateCatalogueEmployeeMenuViewStub implements UpdateCatalogueEmployeeMenuView {

    private String shownName = "";
    private String navigatedAssignedRequestsId = "";
    private String navigatedAvailableRequestsId = "";
    private String navigatedProcessAccountId = "";
    private boolean deleteConfirmationShown = false;
    private boolean navigateToLoginCalled = false;
    private String messageShown = "";

    @Override
    public void showEmployeeName(String fullName) {
        this.shownName = fullName;
    }

    @Override
    public void navigateToAssignedRequests(String employeeId) {
        this.navigatedAssignedRequestsId = employeeId;
    }

    @Override
    public void navigateToAvailableRequestsToAssign(String employeeId) {
        this.navigatedAvailableRequestsId = employeeId;
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