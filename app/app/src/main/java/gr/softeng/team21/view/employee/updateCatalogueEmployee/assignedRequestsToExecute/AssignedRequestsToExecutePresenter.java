package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for managing assigned catalogue update requests.
 * Connects the View with the Domain and DAOs. Uses highly optimized indexed DAO queries
 * to fetch active tasks avoiding full database downloads.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecutePresenter {

    private final AssignedRequestsToExecuteView view;
    private final EmployeeDAO employeeDAO;
    private final UpdateRequestDAO updateRequestDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected dependencies.
     *
     * @param view The View contract implementation handling UI updates.
     * @param employeeDAO The data source for verifying employee sessions.
     * @param updateRequestDAO The data source for querying update requests via Foreign Keys.
     */
    public AssignedRequestsToExecutePresenter(AssignedRequestsToExecuteView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
    }

    /**
     * Asynchronously loads the requests currently assigned to the logged-in employee.
     * Utilizes database indexing to fetch records directly and filters locally to display
     * only active (ASSIGNED) tasks.
     *
     * @param employeeId The unique identifier of the logged-in catalogue employee.
     */
    public void loadAssignedRequests(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                // Optimized DAO call relying on Foreign Keys
                updateRequestDAO.getRequestsByEmployeeId(employeeId).thenAccept(requests -> {
                    ArrayList<CatalogueUpdateRequest> activeRequests = new ArrayList<>();
                    // Filter locally to display only ASSIGNED requests (hide SERVED ones)
                    for (CatalogueUpdateRequest req : requests) {
                        if (req.getStatus() == RequestStatusType.ASSIGNED) {
                            activeRequests.add(req);
                        }
                    }
                    if (view != null) view.updateAssignedRequestsList(activeRequests);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα ανάκτησης: " + e.getMessage());
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

    /**
     * Handles user interaction when a specific request is clicked in the list.
     * Initiates navigation to the detailed execution screen.
     *
     * @param request The catalogue update request selected by the user.
     */
    public void onClickRequest(CatalogueUpdateRequest request) {
        if (loggedInEmployee != null && view != null) {
            view.navigateToRequestDetails(loggedInEmployee.getEmployeeId(), request);
        } else if (view != null) {
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
        }
    }
}