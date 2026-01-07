package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

public interface UpdateRequestDAO {

     /**
      * Retrieves a specific update request based on its ID.
      * @param requestId The unique identifier of the catalogue update request.
      * @return The {@link CatalogueUpdateRequest} object, or null if no request is found with that ID.
      */
     CatalogueUpdateRequest getUpdateRequest(int requestId);

     /**
      * Adds a new catalogue update request to the repository.
      * @param request The request object to be stored.
      * @throws IllegalArgumentException if the request is null or if a request with the same ID already exists.
      */
     void addUpdateRequest(CatalogueUpdateRequest request);

     /**
      * Removes a specific catalogue update request from the repository.
      * @param request The request object to be removed.
      * @throws IllegalArgumentException if the request is null or if the request is not found in the repository.
      */
     void deleteUpdateRequest(CatalogueUpdateRequest request);

     /**
      * Returns a copy of all currently stored update requests.
      * @return A new HashMap containing all catalogue update requests.
      */
     HashMap<Integer,CatalogueUpdateRequest> getUpdateRequests();

     /**
      * Clears all stored update requests from the memory.
      */
     void clear();
}
