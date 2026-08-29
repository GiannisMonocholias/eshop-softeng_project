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
 * database assignment using Foreign Keys via Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignPresenter {
    private final AvailableRequestsToAssignView view;
    private final EmployeeDAO employeeDAO;
    private final UpdateRequestDAO updateRequestDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    public AvailableRequestsToAssignPresenter(AvailableRequestsToAssignView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
    }

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
                    if (view != null) view.updateAvailableRequestsList(requests);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα ανάκτησης αιτημάτων: " + e.getMessage());
                    return null;
                });
            } else {
                if (view != null) view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    public void onRequestClicked(CatalogueUpdateRequest request) {
        if (loggedInEmployee == null) {
            if (view != null) view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
            return;
        }
        if (view != null) view.showConfirmationDialog(request, "Θέλετε να αναλάβετε αυτή την παραγγελία;");
    }

    /**
     * Finalizes the assignment by updating the Foreign Key and saving to the database.
     */
    public void onRequestConfirmed(CatalogueUpdateRequest request) {
        if (loggedInEmployee == null) return;

        // Apply Domain State Changes using Foreign Key
        request.setStatus(RequestStatusType.ASSIGNED);

        // ΣΗΜΕΙΩΣΗ: Πρέπει να προσθέσεις τη μέθοδο setAssignedEmployeeId στο CatalogueUpdateRequest!
        request.setAssignedEmployeeId(loggedInEmployee.getEmployeeId());

        // Save asynchronously via DAO
        updateRequestDAO.updateRequest(request).thenAccept(v -> {
            if (view != null) {
                view.showMessage("Το αίτημα ανατέθηκε επιτυχώς!");
                view.onRequestAssignedSuccess(request);
                view.updateList();
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Αποτυχία ενημέρωσης βάσης: " + e.getMessage());
            return null;
        });
    }
}