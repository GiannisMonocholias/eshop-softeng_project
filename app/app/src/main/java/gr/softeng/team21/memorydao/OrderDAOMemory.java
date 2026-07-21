package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

/**
 * In-memory implementation of the {@link OrderDAO} interface.
 * This class serves as a centralized repository for tracking and managing
 * customer orders throughout their lifecycle, wrapped in CompletableFutures
 * to match the asynchronous architectural pattern.
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
     * {@inheritDoc}
     * <p>This memory implementation synchronously checks the map and returns a completed future.</p>
     */
    @Override
    public CompletableFuture<Order> getOrder(String orderCode){
        CompletableFuture<Order> future = new CompletableFuture<>();
        if(orderCode == null) {
            future.completeExceptionally(new IllegalArgumentException("The orderCode must not be null"));
        } else {
            future.complete(orders.get(orderCode));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Validates memory constraints before adding. Completes exceptionally if the order already exists.</p>
     */
    @Override
    public CompletableFuture<Void> addOrder(Order order){
        CompletableFuture<Void> future = new CompletableFuture<>();
        if(order != null){
            if(!orders.containsKey(order.getOrdercode())){
                orders.put(order.getOrdercode(), order);
                future.complete(null);
            }
            else {
                future.completeExceptionally(new IllegalArgumentException("The given order is already in the repository"));
            }
        }
        else {
            future.completeExceptionally(new IllegalArgumentException("The Order argument must not be null"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation returns an immediately completed future containing the memory map.</p>
     */
    @Override
    public CompletableFuture<HashMap<String, Order>> getOrders(){
        return CompletableFuture.completedFuture(orders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> clear(){
        orders.clear();
        return CompletableFuture.completedFuture(null);
    }
}