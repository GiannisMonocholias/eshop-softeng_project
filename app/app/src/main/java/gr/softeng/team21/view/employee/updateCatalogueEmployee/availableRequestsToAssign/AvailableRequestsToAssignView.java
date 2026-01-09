package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Defines methods for managing the assignment workflow, refreshing the request list,
 * and providing feedback to the Catalogue Employee.
 * @author Γιάννης  Μονοχολιάς
 */
public interface AvailableRequestsToAssignView {

    /**
     * Displays an informative message to the user.
     * @param message The text content of the message.
     */
    void showMessage(String message);

    /**
     * Displays an error alert dialog.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Triggered upon successful assignment of a request.
     * Typically removes the request from the available list UI.
     * @param request The newly assigned catalogue update request.
     */
    void onRequestAssignedSuccess(CatalogueUpdateRequest request);

    /**
     * Refreshes the request list to reflect the current data state.
     */
    void updateList();

    /**
     * Shows a confirmation dialog before an employee assigns a request to themselves.
     * @param request The request targeted for assignment.
     * @param confirmationMessage The text prompt for the user.
     */
    void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage);
}