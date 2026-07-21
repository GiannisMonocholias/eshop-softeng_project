package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.Order;

/**
 * Interface for the Order Data Access Object.
 * Defines asynchronous operations for managing customer orders throughout their lifecycle.
 * @author Γιάννης Μονοχολιάς
 */
public interface OrderDAO {

     /**
      * Retrieves an order asynchronously based on its unique order code.
      * @param orderCode The unique alphanumeric identifier of the order.
      * @return A CompletableFuture containing the {@link Order} object if found, otherwise null.
      * Completes exceptionally with an IllegalArgumentException if the provided orderCode is null.
      */
     CompletableFuture<Order> getOrder(String orderCode);

     /**
      * Registers a new order in the repository asynchronously.
      * @param order The order object to be added.
      * @return A CompletableFuture representing the completion of the insertion.
      * Completes exceptionally with an IllegalArgumentException if the order is null or already exists.
      */
     CompletableFuture<Void> addOrder(Order order);

     /**
      * Returns a map containing all orders currently stored in the system asynchronously.
      * @return A CompletableFuture containing a HashMap of all registered orders.
      */
     CompletableFuture<HashMap<String,Order>> getOrders();

     /**
      * Clears all order records from the repository asynchronously.
      * @return A CompletableFuture representing the completion of the clearing operation.
      */
     CompletableFuture<Void> clear();
}