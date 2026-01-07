package gr.softeng.team21.memorydao;

import java.util.HashMap;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;

/**
 * In-memory implementation of the {@link CustomerDAO} interface.
 * This class provides a centralized repository for customer data,
 * utilizing the Singleton pattern to ensure a single global point of access.
 * @author Γιάννης Μονοχολιάς
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
     * Returns the entire map of registered customers.
     * @return A HashMap where the key is the customer ID and the value is the Customer object.
     */
    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

    /**
     * Retrieves a specific customer by their unique identifier.
     * @param id The unique customer ID.
     * @return The Customer object associated with the given ID, or null if not found.
     */
    public Customer getCustomer(String id){
        return customers.get(id);
    }

    /**
     * Searches for a customer based on their email address.
     * @param emailAddress The email address to search for.
     * @return The Customer object if a match is found, or null otherwise.
     */
    public Customer getCustomerByEmail(String emailAddress){
        for(String id: customers.keySet()){
            if(customers.get(id).getEmailAddress().toString().equals(emailAddress)){
                return customers.get(id);
            }
        }
        return null;
    }

    /**
     * Adds a new customer to the repository.
     * @param customer The Customer object to be added.
     * @throws IllegalArgumentException if the customer is null or if a customer with the same ID already exists.
     */
    public void addCustomer(Customer customer) {
        if (customer != null) {
            String id = customer.getCustomer_id();
            if (!customers.containsKey(id)) {
                customers.put(id, customer);
            } else {
                throw new IllegalArgumentException("Customer with this id already exists.");
            }
        } else {
            throw new IllegalArgumentException("Cannot add null customer.");
        }
    }

    /**
     * Removes a customer from the repository.
     * @param customer The Customer object to be removed.
     * @throws IllegalArgumentException if the customer is null or if the customer does not exist in the repository.
     */
    public void removeCustomer(Customer customer) {
        if (customer != null) {
            String id = customer.getCustomer_id();
            if (customers.containsKey(id)) {
                customers.remove(id);
            } else {
                throw new IllegalArgumentException("Customer with this id does not exist.");
            }
        } else {
            throw new IllegalArgumentException("Cannot remove null customer.");
        }
    }

    /**
     * Clears all customer records from the memory.
     */
    public void clear(){
        customers.clear();
    }
}