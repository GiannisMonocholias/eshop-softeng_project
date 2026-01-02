package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;
import java.util.HashMap;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare.AssignedOrdersToPrepareView;

public class AssignedRequestsToExecutePresenter {
    private AssignedRequestsToExecuteView view;
    private EmployeeDAO employeeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;


    public AssignedRequestsToExecutePresenter(AssignedRequestsToExecuteView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    public ArrayList<CatalogueUpdateRequest> loadAssignedRequests(String employeeId){
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        CatalogueUpdateRequest cur_req;
        ArrayList<CatalogueUpdateRequest> assignedRequests = new ArrayList<>();

        for(Integer requestId: loggedInEmployee.getAssignedRequests().keySet()) {
            cur_req = loggedInEmployee.getAssignedRequests().get(requestId);
            assignedRequests.add(cur_req);
        }

        return assignedRequests;
    }

    public void onClickRequest(CatalogueUpdateRequest request){
        view.navigateToRequestDetails(loggedInEmployee.getEmployeeId(), request);
    }
}
