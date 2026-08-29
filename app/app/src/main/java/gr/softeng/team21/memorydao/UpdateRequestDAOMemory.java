package gr.softeng.team21.memorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * In-memory implementation of the {@link UpdateRequestDAO} interface.
 * Simulates asynchronous database queries, state persistence, and Foreign Key indexing
 * entirely within RAM. Designed specifically for reliable Unit Testing without network overhead.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOMemory implements UpdateRequestDAO {

    private static UpdateRequestDAOMemory instance;
    private static HashMap<Integer, CatalogueUpdateRequest> requests;

    /**
     * Private constructor to enforce the Singleton design pattern.
     * Initializes the underlying HashMap used for data storage.
     */
    private UpdateRequestDAOMemory() {
        requests = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the in-memory DAO.
     *
     * @return The active instance of {@link UpdateRequestDAOMemory}.
     */
    public static UpdateRequestDAOMemory getInstance() {
        if (instance == null) instance = new UpdateRequestDAOMemory();
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId) {
        return CompletableFuture.completedFuture(requests.getOrDefault(requestId, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> addUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request != null) {
            if (!requests.containsKey(request.getId())) {
                requests.put(request.getId(), request);
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("Request already in repository"));
            }
        } else {
            future.completeExceptionally(new IllegalArgumentException("Request argument must not be null"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> updateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request != null) {
            requests.put(request.getId(), request);
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalArgumentException("Request argument must not be null"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request != null && requests.containsKey(request.getId())) {
            requests.remove(request.getId());
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalArgumentException("Request is not in repository"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> getUpdateRequests() {
        return CompletableFuture.completedFuture(new HashMap<>(requests));
    }

    /**
     * {@inheritDoc}
     * Iterates through the in-memory map to simulate a database query filtered by a Foreign Key.
     */
    @Override
    public CompletableFuture<ArrayList<CatalogueUpdateRequest>> getRequestsByEmployeeId(String employeeId) {
        ArrayList<CatalogueUpdateRequest> employeeRequests = new ArrayList<>();
        for (CatalogueUpdateRequest request : requests.values()) {
            if (employeeId.equals(request.getAssignedEmployeeId())) {
                employeeRequests.add(request);
            }
        }
        return CompletableFuture.completedFuture(employeeRequests);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> clear() {
        requests.clear();
        return CompletableFuture.completedFuture(null);
    }
}