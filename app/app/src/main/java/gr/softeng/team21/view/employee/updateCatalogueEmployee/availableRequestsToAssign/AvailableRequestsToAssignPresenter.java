package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the Available Requests to Assign screen.
 * Handles the logic of filtering available catalogue requests and manages the
 * domain-level assignment to the logged-in employee.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignPresenter {
    private AvailableRequestsToAssignView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with required DAOs and view interface.
     * @param view The view implementation.
     * @param employeeDAO Data source for employee records.
     * @param updateRequestDAO Data source for catalogue update requests.
     */
    public AvailableRequestsToAssignPresenter(AvailableRequestsToAssignView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
    }

    /**
     * Loads all catalogue update requests that currently have a status of NEW.
     * @param employeeId The ID of the employee browsing the requests.
     * @return An ArrayList of available requests.
     */
    public ArrayList<CatalogueUpdateRequest> loadAvailableRequests(String employeeId){
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        ArrayList<CatalogueUpdateRequest> requests = new ArrayList<>();
        for(int req_id : updateRequestDAO.getUpdateRequests().keySet()){
            CatalogueUpdateRequest cur_request = updateRequestDAO.getUpdateRequests().get(req_id);
            if(cur_request != null && cur_request.getStatus() == RequestStatusType.NEW) {
                requests.add(cur_request);
            }
        }
        return requests;
    }

    /**
     * Triggered when a request is clicked. Requests a confirmation dialog from the view.
     * @param request The selected request.
     */
    public void onRequestClicked(CatalogueUpdateRequest request) {
        String confirmationMessage = "Θέλετε να αναλάβετε αυτή την παραγγελία;";
        view.showConfirmationDialog(request, confirmationMessage);
    }

    /**
     * Finalizes the assignment in the domain model and updates the request status.
     * @param request The request confirmed for assignment.
     */
    public void onRequestConfirmed(CatalogueUpdateRequest request) {
        boolean result = loggedInEmployee.assignRequest(request.getId());

        if(!result){
            view.showError("Σφάλμα: το αίτημα με ID " + request.getId() + " δεν υπάρχει ή δεν σας έχει ανατεθεί");
            return;
        }

        request.setStatus(RequestStatusType.ASSIGNED);
        view.showMessage("Το αίτημα ανατέθηκε επιτυχώς!");
        view.onRequestAssignedSuccess(request);
        view.updateList();
    }
}