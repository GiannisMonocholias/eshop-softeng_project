package gr.softeng.team21.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Interface for the Update Request Data Access Object.
 * Defines asynchronous operations for managing catalogue update requests
 * throughout their lifecycle, including Foreign Key-based queries.
 * @author Γιάννης Μονοχολιάς
 */
public interface UpdateRequestDAO {

     CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId);

     CompletableFuture<Void> addUpdateRequest(CatalogueUpdateRequest request);

     /**
      * Updates (overwrites) an existing catalogue update request in the repository asynchronously.
      * @param request The request object to be updated.
      * @return A CompletableFuture representing the completion of the update.
      */
     CompletableFuture<Void> updateRequest(CatalogueUpdateRequest request);

     CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request);

     CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> getUpdateRequests();

     /**
      * Efficiently retrieves all update requests assigned to a specific employee using database-level indexing.
      * @param employeeId The unique Foreign Key of the assigned employee.
      * @return A CompletableFuture containing a list of assigned requests.
      */
     CompletableFuture<ArrayList<CatalogueUpdateRequest>> getRequestsByEmployeeId(String employeeId);

     CompletableFuture<Void> clear();
}