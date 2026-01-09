package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the Assigned Requests list.
 * Handles the retrieval of requests from the logged-in employee's personal queue
 * and processes click events to trigger navigation.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecutePresenter {
    private AssignedRequestsToExecuteView view;
    private EmployeeDAO employeeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and employee repository.
     * @param view The view implementation (Activity).
     * @param employeeDAO The data access object for employees.
     */
    public AssignedRequestsToExecutePresenter(AssignedRequestsToExecuteView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Loads the map of requests assigned to the specific employee and converts them to a list.
     * @param employeeId The unique identifier of the catalogue employee.
     * @return An ArrayList of CatalogueUpdateRequest objects.
     */
    public ArrayList<CatalogueUpdateRequest> loadAssignedRequests(String employeeId){
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        ArrayList<CatalogueUpdateRequest> assignedRequests = new ArrayList<>();
        if (loggedInEmployee != null && loggedInEmployee.getAssignedRequests() != null) {
            assignedRequests.addAll(loggedInEmployee.getAssignedRequests().values());
        }

        return assignedRequests;
    }

    /**
     * Triggered when a request is selected from the list.
     * @param request The catalogue update request to be executed.
     */
    public void onClickRequest(CatalogueUpdateRequest request){
        view.navigateToRequestDetails(loggedInEmployee.getEmployeeId(), request);
    }
}