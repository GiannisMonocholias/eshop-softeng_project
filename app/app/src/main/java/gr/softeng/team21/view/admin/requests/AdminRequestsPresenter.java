package gr.softeng.team21.view.admin.requests;

import android.util.Log;

import java.util.ArrayList;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;


public class AdminRequestsPresenter {

    private AdminRequestsView view;
    private UpdateRequestDAO updateRequestDAO;

    public AdminRequestsPresenter(AdminRequestsView view , UpdateRequestDAO updateRequestDAO){
        this.view = view;
        this.updateRequestDAO = updateRequestDAO;
    }

    public ArrayList<CatalogueUpdateRequest> loadRequests(){

        ArrayList<CatalogueUpdateRequest> reqs = new ArrayList<>();
        for (CatalogueUpdateRequest req : updateRequestDAO.getUpdateRequests().values()){
            reqs.add(req);
        }

        return reqs;
    }
}
