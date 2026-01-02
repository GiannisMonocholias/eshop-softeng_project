package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

public interface AssignedRequestsToExecuteView {
    void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request);

}
