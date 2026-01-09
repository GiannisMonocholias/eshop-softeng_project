package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Defines the navigation logic to branch into specific execution screens
 * (Insert, Delete, or process) based on the request content.
 * @author Γιάννης Μονοχολιάς
 */
public interface AssignedRequestsToExecuteView {
    /**
     * Navigates to the specialized details/execution screen for the given request.
     * @param employeeId The ID of the employee performing the update.
     * @param request The specific catalogue update request object.
     */
    void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request);
}