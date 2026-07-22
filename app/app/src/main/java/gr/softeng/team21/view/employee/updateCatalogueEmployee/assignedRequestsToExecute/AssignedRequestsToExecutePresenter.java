package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the Assigned Requests list.
 * Handles the asynchronous retrieval of requests from the logged-in employee's personal queue
 * and processes click events to trigger navigation using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecutePresenter {
    private AssignedRequestsToExecuteView view;
    private EmployeeDAO employeeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and injected employee repository.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data access object for employees.
     */
    public AssignedRequestsToExecutePresenter(AssignedRequestsToExecuteView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously loads the map of requests assigned to the specific employee,
     * converts them to a list, and triggers the UI update.
     * @param employeeId The unique identifier of the catalogue employee.
     */
    public void loadAssignedRequests(String employeeId){
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                ArrayList<CatalogueUpdateRequest> assignedRequests = new ArrayList<>();
                if (loggedInEmployee.getAssignedRequests() != null) {
                    assignedRequests.addAll(loggedInEmployee.getAssignedRequests().values());
                }

                view.updateAssignedRequestsList(assignedRequests);
            } else {
                view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when a request is selected from the list. Initiates navigation.
     * @param request The catalogue update request to be executed.
     */
    public void onClickRequest(CatalogueUpdateRequest request){
        if (loggedInEmployee != null) {
            view.navigateToRequestDetails(loggedInEmployee.getEmployeeId(), request);
        } else {
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
        }
    }
}