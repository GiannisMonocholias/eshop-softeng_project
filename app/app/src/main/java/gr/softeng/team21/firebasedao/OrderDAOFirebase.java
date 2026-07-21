package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

/**
 * Firebase implementation of the {@link OrderDAO} interface.
 * Bridges Firebase's async Tasks to Java's CompletableFuture for non-blocking UI.
 * Handles Firestore database operations for the Order entity.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderDAOFirebase implements OrderDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "orders";

    /**
     * Initializes the Firebase Firestore instance.
     */
    public OrderDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * {@inheritDoc}
     * <p>Fetches the order document directly from the Firestore database.</p>
     */
    @Override
    public CompletableFuture<Order> getOrder(String orderCode) {
        CompletableFuture<Order> future = new CompletableFuture<>();

        if (orderCode == null || orderCode.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("The orderCode must not be null or empty"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(orderCode).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Order order = documentSnapshot.toObject(Order.class);
                        future.complete(order);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Saves the given order as a document in the Firestore database.
     * Verifies if the order code already exists in the collection to prevent accidental data overwriting.</p>
     */
    @Override
    public CompletableFuture<Void> addOrder(Order order) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (order == null || order.getOrdercode() == null) {
            future.completeExceptionally(new IllegalArgumentException("The Order argument or orderCode must not be null"));
            return future;
        }

        // Check if the order already exists to prevent duplication
        db.collection(COLLECTION_NAME).document(order.getOrdercode()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        future.completeExceptionally(new IllegalArgumentException("The given order is already in the repository"));
                    } else {
                        db.collection(COLLECTION_NAME).document(order.getOrdercode())
                                .set(order)
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Retrieves all order documents from the Firestore collection and maps them into a HashMap.</p>
     */
    @Override
    public CompletableFuture<HashMap<String, Order>> getOrders() {
        CompletableFuture<HashMap<String, Order>> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, Order> ordersMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        ordersMap.put(order.getOrdercode(), order);
                    }
                    future.complete(ordersMap);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Iterates through all documents within the Firestore orders collection and deletes them sequentially.</p>
     */
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                    future.complete(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}