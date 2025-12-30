package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

public interface UpdateRequestDAO {

     CatalogueUpdateRequest getUpdateRequest(int requestId);
     void addUpdateRequest(CatalogueUpdateRequest request);
     void deleteUpdateRequest(CatalogueUpdateRequest request);
     HashMap<Integer,CatalogueUpdateRequest> getUpdateRequests();

     void clear();
}
