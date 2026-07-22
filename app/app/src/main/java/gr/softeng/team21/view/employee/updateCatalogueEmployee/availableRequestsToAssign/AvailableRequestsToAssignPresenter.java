package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the Available Requests to Assign screen.
 * Handles the asynchronous logic of filtering available catalogue requests and manages the
 * domain-level assignment to the logged-in employee using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignPresenter {
    private AvailableRequestsToAssignView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO Data source for employee records.
     * @param updateRequestDAO Data source for catalogue update requests.
     */
    public AvailableRequestsToAssignPresenter(AvailableRequestsToAssignView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
    }

    /**
     * Asynchronously loads all catalogue update requests that currently have a status of NEW,
     * and triggers a UI update via the view.
     * @param employeeId The ID of the employee browsing the requests.
     */
    public void loadAvailableRequests(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                updateRequestDAO.getUpdateRequests().thenAccept(requestsMap -> {
                    ArrayList<CatalogueUpdateRequest> requests = new ArrayList<>();
                    for (CatalogueUpdateRequest cur_request : requestsMap.values()) {
                        if (cur_request != null && cur_request.getStatus() == RequestStatusType.NEW) {
                            requests.add(cur_request);
                        }
                    }
                    view.updateAvailableRequestsList(requests);
                }).exceptionally(e -> {
                    view.showError("Σφάλμα ανάκτησης αιτημάτων: " + e.getMessage());
                    return null;
                });
            } else {
                view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when a request is clicked. Requests a confirmation dialog from the view.
     * @param request The selected request.
     */
    public void onRequestClicked(CatalogueUpdateRequest request) {
        if (loggedInEmployee == null) {
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
            return;
        }
        String confirmationMessage = "Θέλετε να αναλάβετε αυτή την παραγγελία;";
        view.showConfirmationDialog(request, confirmationMessage);
    }

    /**
     * Finalizes the assignment in the domain model and updates the request status.
     * @param request The request confirmed for assignment.
     */
    public void onRequestConfirmed(CatalogueUpdateRequest request) {
        if (loggedInEmployee == null) return;

        boolean result = loggedInEmployee.assignRequest(request.getId());

        if (!result) {
            view.showError("Σφάλμα: το αίτημα με ID " + request.getId() + " δεν υπάρχει ή δεν σας έχει ανατεθεί");
            return;
        }

        request.setStatus(RequestStatusType.ASSIGNED);

        view.showMessage("Το αίτημα ανατέθηκε επιτυχώς!");
        view.onRequestAssignedSuccess(request);
        view.updateList();
    }
}