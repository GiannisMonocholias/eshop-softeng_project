package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import android.util.Log;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

public class AvailableRequestsToAssignPresenter {
    private AvailableRequestsToAssignView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private UpdateCatalogueEmployee loggedInEmployee;

    public AvailableRequestsToAssignPresenter(AvailableRequestsToAssignView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
    }

    public ArrayList<CatalogueUpdateRequest> loadAvailableRequests(String employeeId){
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        CatalogueUpdateRequest cur_request;
        ArrayList<CatalogueUpdateRequest> requests = new ArrayList<>();

        for(int req_id: updateRequestDAO.getUpdateRequests().keySet()){
            cur_request = updateRequestDAO.getUpdateRequests().get(req_id);
            if(cur_request != null){
                if(cur_request.getStatus() == RequestStatusType.NEW)
                    requests.add(cur_request);
            }
        }

        return requests;
    }




    public void onRequestClicked(CatalogueUpdateRequest request) {
        String confirmationMessage = "Θέλετε να αναλάβετε αυτή την παραγγελία;";
        view.showConfirmationDialog(request ,confirmationMessage);
    }

    public void onRequestConfirmed(CatalogueUpdateRequest request) {
        boolean result = loggedInEmployee.assignRequest(request.getId());

        if(!result){
            view.showError("Σφάλμα: Δεν υπάρχει ή δεν σας έχει ανατεθεί το αίτημα με κωδικό" + request.getId());
            return;
        }
        request.setStatus(RequestStatusType.ASSIGNED);


        view.showMessage("Το αίτημα ανατέθηκε επιτυχώς!");
        view.onRequestAssignedSuccess(request);

        view.updateList();
    }

}
