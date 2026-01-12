package gr.softeng.team21.memorydao;
import java.util.HashMap;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * In-memory implementation of the {@link UpdateRequestDAO} interface.
 * This class acts as a central repository for all catalogue update requests,
 * providing global access via the Singleton pattern.
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
     * Retrieves a specific update request based on its ID.
     * @param requestId The unique identifier of the catalogue update request.
     * @return The {@link CatalogueUpdateRequest} object, or null if no request is found with that ID.
     */
    public CatalogueUpdateRequest getUpdateRequest(int requestId) {
        if (requests.containsKey(requestId))
            return requests.get(requestId);
        else
            return null;
    }

    /**
     * Adds a new catalogue update request to the repository.
     * @param request The request object to be stored.
     * @throws IllegalArgumentException if the request is null or if a request with the same ID already exists.
     */
    public void addUpdateRequest(CatalogueUpdateRequest request) {
        if (request != null) {
            if (!requests.containsKey(request.getId())) {
                requests.put(request.getId(), request);
            } else {
                throw new IllegalArgumentException("Request already in repository");
            }
        } else {
            throw new IllegalArgumentException("Request argument must not be null");
        }
    }

    /**
     * Removes a specific catalogue update request from the repository.
     * @param request The request object to be removed.
     * @throws IllegalArgumentException if the request is null or if the request is not found in the repository.
     */
    public void deleteUpdateRequest(CatalogueUpdateRequest request) {
        if (request != null) {
            if (requests.containsKey(request.getId())) {
                requests.remove(request.getId());
            } else {
                throw new IllegalArgumentException("Request is not in repository");
            }
        } else {
            throw new IllegalArgumentException("Request argument must not be null");
        }
    }

    /**
     * Returns a copy of all currently stored update requests.
     * @return A new HashMap containing all catalogue update requests.
     */
    public HashMap<Integer, CatalogueUpdateRequest> getUpdateRequests() {
        return new HashMap<>(requests);
    }

    /**
     * Clears all stored update requests from the memory.
     */
    public void clear() {
        requests.clear();
    }
}