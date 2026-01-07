package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Order;

public interface OrderDAO {

     /**
      * Retrieves an order based on its unique order code.
      * @param orderCode The unique alphanumeric identifier of the order.
      * @return The {@link Order} object if found, otherwise null.
      * @throws IllegalArgumentException if the provided orderCode is null.
      */
     Order getOrder(String orderCode);

     /**
      * Registers a new order in the repository.
      * @param order The order object to be added.
      * @throws IllegalArgumentException if the order is null or already exists in the system.
      */
     void addOrder(Order order);

     /**
      * Returns a map containing all orders currently stored in memory.
      * @return A HashMap of all registered orders.
      */
     HashMap<String,Order> getOrders();

     /**
      * Clears all order records from the repository.
      */
     void clear();
}
