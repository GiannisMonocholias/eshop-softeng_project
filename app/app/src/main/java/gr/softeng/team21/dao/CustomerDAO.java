package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;

public interface CustomerDAO {

     /**
      * Returns the entire map of registered customers.
      * @return A HashMap where the key is the customer ID and the value is the Customer object.
      */
     HashMap<String, Customer> getCustomers();

     /**
      * Retrieves a specific customer by their unique identifier.
      * @param id The unique customer ID.
      * @return The Customer object associated with the given ID, or null if not found.
      */
     Customer getCustomer(String id);

     /**
      * Searches for a customer based on their email address.
      * @param emailAddress The email address to search for.
      * @return The Customer object if a match is found, or null otherwise.
      */
     Customer getCustomerByEmail(String emailAddress);

     /**
      * Adds a new customer to the repository.
      * @param customer The Customer object to be added.
      * @throws IllegalArgumentException if the customer is null or if a customer with the same ID already exists.
      */
     void addCustomer(Customer customer);

     /**
      * Removes a customer from the repository.
      * @param customer The Customer object to be removed.
      * @throws IllegalArgumentException if the customer is null or if the customer does not exist in the repository.
      */
     void removeCustomer(Customer customer);

     /**
      * Clears all customer records from the memory.
      */
     public void clear();
}
