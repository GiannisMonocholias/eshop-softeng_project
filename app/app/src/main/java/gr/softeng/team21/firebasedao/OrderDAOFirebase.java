package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private final FirebaseFunctions functions;
    private static final String COLLECTION_NAME = "orders";

    public OrderDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.functions = FirebaseFunctions.getInstance();
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Order> getOrder(String orderCode) {
        CompletableFuture<Order> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).document(orderCode).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) future.complete(documentSnapshot.toObject(Order.class));
            else future.complete(null);
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<HashMap<String, Order>> getOrders() {
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
     * {@inheritDoc}
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
     *{@inheritDoc}
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

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> addOrder(Order order) {
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

    /**{@inheritDoc}*/
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

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Prepare the data to be sent to the Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("collectionPath", COLLECTION_NAME);

        // Call of the Cloud Function 'deleteCollection'
        functions.getHttpsCallable("deleteCollection")
                .call(data)
                .addOnSuccessListener(result -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}