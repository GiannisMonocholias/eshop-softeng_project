package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateRequestsRepository {

    private static UpdateRequestsRepository instance;

    private HashMap<Integer,CatalogueUpdateRequest> requests;

    private UpdateRequestsRepository(){
        requests = new HashMap<>();
    }

    public static UpdateRequestsRepository getInstance(){
        if (instance == null){
            instance = new UpdateRequestsRepository();
        }
        return instance;
    }


    public CatalogueUpdateRequest getUpdateRequest(int requestId){
        if(requests.containsKey(requestId))
            return requests.get(requestId);
        else
            return null;
    }

    public void addUpdateRequest(CatalogueUpdateRequest request){
        if(request != null) {
            if (!requests.containsKey(request.getId())) {
                requests.put(request.getId(), request);
            }
            else {
                throw new IllegalArgumentException("Request already in repository");
            }
        }
        else {
            throw new IllegalArgumentException("Request argument must not be null");
        }
    }

    public void deleteUpdateRequest(CatalogueUpdateRequest request){
        if(request != null) {
            if (requests.containsKey(request.getId())) {
                requests.remove(request.getId());
            }
            else {
                throw new IllegalArgumentException("Request is not in repository");
            }
        }
        else {
            throw new IllegalArgumentException("Request argument must not be null");
        }
    }

    public HashMap<Integer,CatalogueUpdateRequest> getUpdateRequests(){
        return new HashMap<>(requests);
    }

    public void clear(){
        requests.clear();
    }
}
