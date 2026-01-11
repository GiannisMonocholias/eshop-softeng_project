package gr.softeng.team21.view.admin.requests;

import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

public class NewRequestPresenter {

    CatalogueUpdateRequest updateRequest;
    UpdateRequestDAOMemory updateRequestDAOMemory = UpdateRequestDAOMemory.getInstance();
    ProductTypeDAOMemory productTypeDAOMemory = ProductTypeDAOMemory.getInstance();
    int reqId;

    public void createRequest(String choice , String description , String productName , String productId){

        Date date = new Date();
        String updateDescription = description;
        ProductType product = productTypeDAOMemory.getProduct(productId);
        AllowedRequest type = null;

        if(choice.equals("Εισαγωγή")){
            type = AllowedRequest.INSERT_PRODUCT;
        }

        if(choice.equals("Διαγραφή")){
            type = AllowedRequest.DELETE_PRODUCT;
        }

        if(choice.equals("Τροποποίηση")){
            type = AllowedRequest.PROCESS_PRODUCT;
        }

        if(updateRequestDAOMemory.getUpdateRequests().size() <= 5){
            reqId = 100;
        }else {
            reqId += 1;
        }

        updateRequest = new CatalogueUpdateRequest(date , updateDescription , product , type , reqId);
        updateRequestDAOMemory.addUpdateRequest(updateRequest);
    }
}
