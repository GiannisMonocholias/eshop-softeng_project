package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * In-memory implementation of the {@link UpdateRequestDAO} interface.
 * This class acts as a central repository for all catalogue update requests,
 * providing global access via the Singleton pattern and wrapped in CompletableFutures
 * for an asynchronous architecture.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOMemory implements UpdateRequestDAO {

    private static UpdateRequestDAOMemory instance;
    private static HashMap<Integer, CatalogueUpdateRequest> requests;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the underlying map used to store update requests.
     */
    private UpdateRequestDAOMemory() {
        requests = new HashMap<>();
    }

    /**
     * Returns the unique instance of UpdateRequestDAOMemory.
     * @return The singleton instance of this DAO.
     */
    public static UpdateRequestDAOMemory getInstance() {
        if (instance == null) {
            instance = new UpdateRequestDAOMemory();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId) {
        CompletableFuture<CatalogueUpdateRequest> future = new CompletableFuture<>();
        if (requests.containsKey(requestId)) {
            future.complete(requests.get(requestId));
        } else {
            future.complete(null);
        }
        return future;
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
    public CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request != null) {
            if (requests.containsKey(request.getId())) {
                requests.remove(request.getId());
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("Request is not in repository"));
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
    public CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> getUpdateRequests() {
        return CompletableFuture.completedFuture(new HashMap<>(requests));
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