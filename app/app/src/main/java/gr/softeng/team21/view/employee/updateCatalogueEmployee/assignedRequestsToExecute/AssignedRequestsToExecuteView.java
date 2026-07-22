package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Defines the navigation and asynchronous UI update logic to branch into specific execution screens
 * (Insert, Delete, or process) based on the request content.
 * @author Γιάννης Μονοχολιάς
 */
public interface AssignedRequestsToExecuteView {

    /**
     * Updates the UI list asynchronously with the loaded assigned requests.
     * @param requests An ArrayList of the assigned catalogue update requests.
     */
    void updateAssignedRequestsList(ArrayList<CatalogueUpdateRequest> requests);

    /**
     * Displays an error message to the user, typically via a dialog.
     * @param message The error description to display.
     */
    void showError(String message);

    /**
     * Navigates to the specialized details/execution screen for the given request.
     * @param employeeId The ID of the employee performing the update.
     * @param request The specific catalogue update request object.
     */
    void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request);
}