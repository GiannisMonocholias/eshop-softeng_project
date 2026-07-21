package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;

/**
 * In-memory implementation of the {@link CustomerDAO} interface.
 * This class provides a centralized repository for customer data,
 * utilizing the Singleton pattern to ensure a single global point of access.
 * Adapted to return CompletableFuture objects while preserving the original logic flow.
 * @author PAVLOS GRATSANIS
 */
public class CustomerDAOMemory implements CustomerDAO {

    private static HashMap<String, Customer> customers;
    private static CustomerDAOMemory instance;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the map used for customer storage.
     */
    private CustomerDAOMemory() {
        customers = new HashMap<>();
    }

    /**
     * Returns the singleton instance of CustomerDAOMemory.
     * @return The unique instance of this DAO.
     */
    public static CustomerDAOMemory getInstance() {
        if (instance == null) {
            instance = new CustomerDAOMemory();
        }
        return instance;
    }

    /**
     * Returns the entire map of registered customers wrapped in a CompletableFuture.
     * @return A CompletableFuture containing a HashMap where the key is the customer ID and the value is the Customer object.
     */
    @Override
    public CompletableFuture<HashMap<String, Customer>> getCustomers() {
        return CompletableFuture.completedFuture(customers);
    }

    /**
     * Retrieves a specific customer by their unique identifier.
     * @param id The unique customer ID.
     * @return A CompletableFuture containing the Customer object associated with the given ID, or null if not found.
     */
    @Override
    public CompletableFuture<Customer> getCustomer(String id) {
        return CompletableFuture.completedFuture(customers.get(id));
    }

    /**
     * Searches for a customer based on their email address.
     * @param emailAddress The email address to search for.
     * @return A CompletableFuture containing the Customer object if a match is found, or null otherwise.
     */
    @Override
    public CompletableFuture<Customer> getCustomerByEmail(String emailAddress) {
        for (String id : customers.keySet()) {
            if (customers.get(id).getEmailAddress().toString().equals(emailAddress)) {
                return CompletableFuture.completedFuture(customers.get(id));
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Adds a new customer to the repository.
     * @param customer The Customer object to be added.
     * @return A CompletableFuture representing the success or failure of the operation.
     */
    @Override
    public CompletableFuture<Void> addCustomer(Customer customer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (customer != null) {
            String id = customer.getCustomer_id();
            if (!customers.containsKey(id)) {
                customers.put(id, customer);
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("Customer with this id already exists."));
            }
        } else {
            future.completeExceptionally(new IllegalArgumentException("Cannot add null customer."));
        }

        return future;
    }

    /**
     * Removes a customer from the repository.
     * @param customer The Customer object to be removed.
     * @return A CompletableFuture representing the success or failure of the operation.
     */
    @Override
    public CompletableFuture<Void> removeCustomer(Customer customer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (customer != null) {
            String id = customer.getCustomer_id();
            if (customers.containsKey(id)) {
                customers.remove(id);
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("Customer with this id does not exist."));
            }
        } else {
            future.completeExceptionally(new IllegalArgumentException("Cannot remove null customer."));
        }

        return future;
    }

    /**
     * Clears all customer records from the memory.
     * @return A CompletableFuture representing the completion of the action.
     */
    @Override
    public CompletableFuture<Void> clear() {
        customers.clear();
        return CompletableFuture.completedFuture(null);
    }
}