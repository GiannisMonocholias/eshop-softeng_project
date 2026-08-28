package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

/**
 * Firebase implementation of the {@link OrderDAO} interface.
 * Utilizes native Firestore indexed queries to efficiently retrieve filtered datasets.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOFirebase implements OrderDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "orders";

    public OrderDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public CompletableFuture<Order> getOrder(String orderCode) {
        // [Existing implementation remains exactly the same]
        CompletableFuture<Order> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).document(orderCode).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) future.complete(documentSnapshot.toObject(Order.class));
            else future.complete(null);
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    @Override
    public CompletableFuture<HashMap<String, Order>> getOrders() {
        // [Existing implementation remains exactly the same]
        CompletableFuture<HashMap<String, Order>> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            HashMap<String, Order> ordersMap = new HashMap<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Order order = document.toObject(Order.class);
                ordersMap.put(order.getOrdercode(), order);
            }
            future.complete(ordersMap);
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Uses Firestore native indexes to quickly fetch only the orders for a specific Deliverer.
     * This avoids downloading the entire collection to the client device.
     */
    @Override
    public CompletableFuture<ArrayList<Order>> getOrdersByDelivererId(String delivererId) {
        CompletableFuture<ArrayList<Order>> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME)
                .whereEqualTo("delivererId", delivererId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Order> assignedOrders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        assignedOrders.add(document.toObject(Order.class));
                    }
                    future.complete(assignedOrders);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Uses Firestore native indexes to quickly fetch only the orders for a specific Preparation Employee.
     */
    @Override
    public CompletableFuture<ArrayList<Order>> getOrdersByPreparationEmployeeId(String employeeId) {
        CompletableFuture<ArrayList<Order>> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME)
                .whereEqualTo("preparationEmployeeId", employeeId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Order> assignedOrders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        assignedOrders.add(document.toObject(Order.class));
                    }
                    future.complete(assignedOrders);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    @Override
    public CompletableFuture<Void> addOrder(Order order) {
        // [Existing implementation remains exactly the same]
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).document(order.getOrdercode()).get().addOnSuccessListener(doc -> {
            if (doc.exists()) future.completeExceptionally(new IllegalArgumentException("Order exists"));
            else {
                db.collection(COLLECTION_NAME).document(order.getOrdercode()).set(order)
                        .addOnSuccessListener(aVoid -> future.complete(null)).addOnFailureListener(future::completeExceptionally);
            }
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Updates an existing order document in Firestore.
     */
    @Override
    public CompletableFuture<Void> updateOrder(Order order) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (order == null || order.getOrdercode() == null) {
            future.completeExceptionally(new IllegalArgumentException("Order cannot be null"));
            return future;
        }
        db.collection(COLLECTION_NAME).document(order.getOrdercode()).set(order)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    @Override
    public CompletableFuture<Void> clear() {
        // [Existing implementation remains exactly the same]
        return new CompletableFuture<>();
    }
}