package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

public interface UpdateCatalogueEmployeeMenuView {
    void showEmployeeName(String fullName);

    void navigateToAssignedRequests(String employeeId);

    void navigateToAvailableRequestsToAssign(String employeeId);

    void showDeleteAccountConfirmation();

    void navigateToLogin();

    void navigateToProcessAccount(String employeeId);


    void showMessage(String message);
}