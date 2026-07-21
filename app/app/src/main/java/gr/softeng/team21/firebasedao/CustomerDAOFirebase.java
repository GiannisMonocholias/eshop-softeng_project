package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;

/**
 * Firebase Firestore implementation of the {@link CustomerDAO}.
 * Handles CRUD operations using CompletableFuture to bridge Firestore Tasks.
 */
public class CustomerDAOFirebase implements CustomerDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION = "customers";


    public CustomerDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }


    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<HashMap<String, Customer>> getCustomers() {
        CompletableFuture<HashMap<String, Customer>> future = new CompletableFuture<>();
        db.collection(COLLECTION).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, Customer> map = new HashMap<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Customer customer = doc.toObject(Customer.class);
                        map.put(customer.getCustomer_id(), customer);
                    }
                    future.complete(map);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Customer> getCustomer(String id) {
        CompletableFuture<Customer> future = new CompletableFuture<>();
        db.collection(COLLECTION).document(id).get()
                .addOnSuccessListener(doc -> future.complete(doc.exists() ? doc.toObject(Customer.class) : null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Customer> getCustomerByEmail(String email) {
        CompletableFuture<Customer> future = new CompletableFuture<>();
        db.collection(COLLECTION).whereEqualTo("emailAddress.address", email).get()
                .addOnSuccessListener(query -> future.complete(!query.isEmpty() ? query.getDocuments().get(0).toObject(Customer.class) : null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> addCustomer(Customer customer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (customer == null) {
            future.completeExceptionally(new IllegalArgumentException("Customer cannot be null"));
        } else {
            db.collection(COLLECTION).document(customer.getCustomer_id()).set(customer)
                    .addOnSuccessListener(v -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        }

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> removeCustomer(Customer customer) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (customer == null) {
            future.completeExceptionally(new IllegalArgumentException("Customer cannot be null"));
        } else {
            db.collection(COLLECTION).document(customer.getCustomer_id()).delete()
                    .addOnSuccessListener(v -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        }
        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(new UnsupportedOperationException("Bulk delete requires Cloud Functions."));
        return future;
    }
}