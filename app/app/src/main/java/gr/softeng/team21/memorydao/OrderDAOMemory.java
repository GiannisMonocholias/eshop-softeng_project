package gr.softeng.team21.memorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

/**
 * In-memory implementation of the {@link OrderDAO} interface for Unit Testing.
 * Simulates database queries and persistence without network overhead.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOMemory implements OrderDAO {

    private static OrderDAOMemory instance;
    private final HashMap<String, Order> orders = new HashMap<>();

    private OrderDAOMemory() {}

    public static OrderDAOMemory getInstance() {
        if (instance == null) instance = new OrderDAOMemory();
        return instance;
    }

    @Override
    public CompletableFuture<Order> getOrder(String orderCode) {
        return CompletableFuture.completedFuture(orders.get(orderCode));
    }

    @Override
    public CompletableFuture<HashMap<String, Order>> getOrders() {
        return CompletableFuture.completedFuture(new HashMap<>(orders));
    }

    @Override
    public CompletableFuture<ArrayList<Order>> getOrdersByDelivererId(String delivererId) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (delivererId.equals(order.getDelivererId())) {
                result.add(order);
            }
        }
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<ArrayList<Order>> getOrdersByPreparationEmployeeId(String employeeId) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (employeeId.equals(order.getPreparationEmployeeId())) {
                result.add(order);
            }
        }
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Void> addOrder(Order order) {
        if (orders.containsKey(order.getOrdercode())) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Order exists"));
            return future;
        }
        orders.put(order.getOrdercode(), order);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> updateOrder(Order order) {
        if (order == null || order.getOrdercode() == null) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Order cannot be null"));
            return future;
        }
        orders.put(order.getOrdercode(), order);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> clear() {
        orders.clear();
        return CompletableFuture.completedFuture(null);
    }
}