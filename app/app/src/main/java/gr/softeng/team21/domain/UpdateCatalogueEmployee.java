package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;

public  class UpdateCatalogueEmployee extends Employee{
    private int totalCatalogueUpdates;
    HashMap<Integer, CatalogueUpdateRequest> assignedRequests;

    public UpdateCatalogueEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalCatalogueUpdates = 0;
        assignedRequests = new HashMap<>();
    }

    public int getTotalCatalogueUpdates() {
        return totalCatalogueUpdates;
    }


    public boolean assignRequest(int requestId){
        if(!UpdateRequestsRepository.getInstance().getUpdateRequests().containsKey(requestId))
            return false;

        CatalogueUpdateRequest selectedRequest = UpdateRequestsRepository.getInstance().getUpdateRequest(requestId);
        if(!assignedRequests.containsKey(requestId))
            assignedRequests.put(requestId, selectedRequest);
        else
            return false;
        return true;
    }

    public CatalogueUpdateRequest selectRequest(int requestId){
        if(!assignedRequests.containsKey(requestId))
            return  null;

        return assignedRequests.get(requestId);
    }


    public void executeUpdate(CatalogueUpdateRequest request){
        if(request == null)
            throw new IllegalArgumentException("Request cannot be null");

        switch(request.getType()){
            case INSERT_PRODUCT:
                ProductTypesRepository.getInstance().addProductType(request.getProduct());
                break;
            case DELETE_PRODUCT:
                ProductTypesRepository.getInstance().deleteProductType(request.getProduct());
                break;
            case PROCESS_PRODUCT:
                ProductTypesRepository.getInstance().processProduct(request.getProduct());
                break;
        }
        totalCatalogueUpdates++;
    }
}