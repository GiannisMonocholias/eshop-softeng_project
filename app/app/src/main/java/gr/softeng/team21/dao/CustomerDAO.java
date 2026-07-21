package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.domain.Customer;

/**
 * Data Access Object (DAO) interface for managing {@link Customer} entities.
 * Uses CompletableFuture for non-blocking, asynchronous operations.
 */
public interface CustomerDAO {

     /**
      * Retrieves all registered customers from the database.
      * @return A CompletableFuture containing the map of customers or an error.
      */
     CompletableFuture<HashMap<String, Customer>> getCustomers();

     /**
      * Retrieves a specific customer by their unique identifier.
      * @param id The unique customer ID.
      * @return A CompletableFuture containing the Customer object, or null if not found.
      */
     CompletableFuture<Customer> getCustomer(String id);

     /**
      * Searches for a customer based on their email address.
      * @param emailAddress The email address string to search for.
      * @return A CompletableFuture containing the found Customer object, or null.
      */
     CompletableFuture<Customer> getCustomerByEmail(String emailAddress);

     /**
      * Adds a new customer to the database.
      * @param customer The Customer object to persist.
      * @return A CompletableFuture representing the completion of the operation.
      */
     CompletableFuture<Void> addCustomer(Customer customer);

     /**
      * Removes an existing customer from the database.
      * @param customer The Customer object to delete.
      * @return A CompletableFuture representing the completion of the deletion.
      */
     CompletableFuture<Void> removeCustomer(Customer customer);

     /**
      * Clears all customer records from the database.
      * @return A CompletableFuture representing the completion of the bulk deletion.
      */
     CompletableFuture<Void> clear();
}