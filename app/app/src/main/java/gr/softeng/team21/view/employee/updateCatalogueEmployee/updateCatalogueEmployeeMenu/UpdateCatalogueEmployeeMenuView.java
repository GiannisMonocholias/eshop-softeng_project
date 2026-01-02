package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

public interface UpdateCatalogueEmployeeMenuView {
    public void showEmployeeName(String fullName);

    void navigateToAssignedRequests(String employeeId);

    void navigateToAvailableRequestsToAssign(String employeeId);

}
