package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Interface for the Update Request Data Access Object.
 * Defines asynchronous operations for managing catalogue update requests
 * throughout their lifecycle.
 * @author Γιάννης Μονοχολιάς
 */
public interface UpdateRequestDAO {

     /**
      * Retrieves a specific update request asynchronously based on its ID.
      * @param requestId The unique identifier of the catalogue update request.
      * @return A CompletableFuture containing the {@link CatalogueUpdateRequest} object, or null if no request is found.
      */
     CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId);

     /**
      * Adds a new catalogue update request to the repository asynchronously.
      * @param request The request object to be stored.
      * @return A CompletableFuture representing the completion of the insertion.
      *         Completes exceptionally with an IllegalArgumentException if the request is null or already exists.
      */
     CompletableFuture<Void> addUpdateRequest(CatalogueUpdateRequest request);

     /**
      * Removes a specific catalogue update request from the repository asynchronously.
      * @param request The request object to be removed.
      * @return A CompletableFuture representing the completion of the deletion.
      *         Completes exceptionally with an IllegalArgumentException if the request is null or not found.
      */
     CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request);

     /**
      * Returns a copy of all currently stored update requests asynchronously.
      * @return A CompletableFuture containing a HashMap of all catalogue update requests.
      */
     CompletableFuture<HashMap<Integer,CatalogueUpdateRequest>> getUpdateRequests();

     /**
      * Clears all stored update requests from the memory asynchronously.
      * @return A CompletableFuture representing the completion of the clearing operation.
      */
     CompletableFuture<Void> clear();
}