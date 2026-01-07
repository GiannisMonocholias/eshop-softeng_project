package gr.softeng.team21.memorydao;

import java.util.HashMap;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

/**
 * In-memory implementation of the {@link OrderDAO} interface.
 * This class serves as a centralized repository for tracking and managing
 * customer orders throughout their lifecycle, from submission to delivery.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOMemory implements OrderDAO {
    private static OrderDAOMemory instance;

    private static HashMap<String, Order> orders;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the map used to store orders in memory.
     */
    private OrderDAOMemory(){
        orders = new HashMap<>();
    }

    /**
     * Returns the singleton instance of OrderDAOMemory.
     * @return The unique instance of this DAO.
     */
    public static OrderDAOMemory getInstance(){
        if (instance == null){
            instance = new OrderDAOMemory();
        }
        return instance;
    }

    /**
     * Retrieves an order based on its unique order code.
     * @param orderCode The unique alphanumeric identifier of the order.
     * @return The {@link Order} object if found, otherwise null.
     * @throws IllegalArgumentException if the provided orderCode is null.
     */
    public Order getOrder(String orderCode){
        if(orderCode == null)
            throw new IllegalArgumentException("The orderCode must not be null");

        return orders.get(orderCode);
    }

    /**
     * Registers a new order in the repository.
     * @param order The order object to be added.
     * @throws IllegalArgumentException if the order is null or already exists in the system.
     */
    public void addOrder(Order order){
        if(order != null){
            if(!orders.containsKey(order.getOrdercode())){
                orders.put(order.getOrdercode(), order);
            }
            else {
                throw new IllegalArgumentException("The given order is already in the repository");
            }
        }
        else
            throw new IllegalArgumentException("The Order order argument must not be null");
    }

    /**
     * Returns a map containing all orders currently stored in memory.
     * @return A HashMap of all registered orders.
     */
    public HashMap<String, Order> getOrders(){
        return orders;
    }

    /**
     * Clears all order records from the repository.
     */
    public void clear(){
        orders.clear();
    }
}