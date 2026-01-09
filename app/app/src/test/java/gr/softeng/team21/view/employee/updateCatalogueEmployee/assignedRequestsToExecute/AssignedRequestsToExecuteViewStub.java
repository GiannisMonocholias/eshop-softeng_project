package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * A stub implementation of the {@link AssignedRequestsToExecuteView} interface for unit testing.
 * It provides a way to verify that navigation to the detailed request execution screen
 * is triggered with the correct parameters (employee ID and the request object).
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecuteViewStub implements AssignedRequestsToExecuteView {

    private String navigatedEmployeeId = "";
    private CatalogueUpdateRequest navigatedRequest = null;
    private boolean navigationCalled = false;

    /**
     * Captures navigation data when the presenter requests to show the details of a specific request.
     * @param employeeId The ID of the employee who will execute the request.
     * @param request The catalogue update request object to be displayed.
     */
    @Override
    public void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request) {
        this.navigationCalled = true;
        this.navigatedEmployeeId = employeeId;
        this.navigatedRequest = request;
    }

    // --- Accessor methods for verification during testing ---

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