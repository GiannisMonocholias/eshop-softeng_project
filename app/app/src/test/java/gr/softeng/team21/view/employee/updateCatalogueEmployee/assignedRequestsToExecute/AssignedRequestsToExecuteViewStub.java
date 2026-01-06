package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

public class AssignedRequestsToExecuteViewStub implements AssignedRequestsToExecuteView {

    private String navigatedEmployeeId = "";
    private CatalogueUpdateRequest navigatedRequest = null;
    private boolean navigationCalled = false;

    @Override
    public void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request) {
        this.navigationCalled = true;
        this.navigatedEmployeeId = employeeId;
        this.navigatedRequest = request;
    }


    public String getNavigatedEmployeeId() {
        return navigatedEmployeeId;
    }

    public CatalogueUpdateRequest getNavigatedRequest() {
        return navigatedRequest;
    }

    public boolean isNavigationCalled() {
        return navigationCalled;
    }
}