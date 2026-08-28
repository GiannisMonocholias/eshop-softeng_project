package gr.softeng.team21.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.domain.Order;

/**
 * Data Access Object (DAO) interface for managing orders.
 * Handles asynchronous database operations using CompletableFuture.
 * @author PAVLOS GRATSANIS
 */
public interface OrderDAO {

     /**
      * Retrieves an order by its unique code.
      * @param orderCode The unique order ID.
      * @return A CompletableFuture containing the order, or null if not found.
      */
     CompletableFuture<Order> getOrder(String orderCode);

     /**
      * Retrieves all orders in the system. (Use with caution on large datasets).
      * @return A CompletableFuture containing a Map of all orders.
      */
     CompletableFuture<HashMap<String, Order>> getOrders();

     /**
      * Efficiently queries the database for all orders assigned to a specific deliverer
      * using database-level indexes.
      * @param delivererId The unique ID of the deliverer.
      * @return A CompletableFuture containing a list of assigned orders.
      */
     CompletableFuture<ArrayList<Order>> getOrdersByDelivererId(String delivererId);

     /**
      * Efficiently queries the database for all orders assigned to a specific preparation employee
      * using database-level indexes.
      * @param employeeId The unique ID of the preparation employee.
      * @return A CompletableFuture containing a list of assigned orders.
      */
     CompletableFuture<ArrayList<Order>> getOrdersByPreparationEmployeeId(String employeeId);

     /**
      * Saves a completely new order to the database. Throws an error if it already exists.
      * @param order The order to add.
      */
     CompletableFuture<Void> addOrder(Order order);

     /**
      * Updates (overwrites) an existing order in the database.
      * @param order The order to update.
      */
     CompletableFuture<Void> updateOrder(Order order);

     /**
      * Clears all orders from the database.
      */
     CompletableFuture<Void> clear();
}