package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ShoppingCart;

/**
 * Firebase Firestore implementation of the {@link CustomerDAO}.
 * Handles CRUD operations using CompletableFuture to bridge Firestore Tasks.
 */
public class CustomerDAOFirebase implements CustomerDAO {

    private final FirebaseFirestore db;
    private final FirebaseFunctions functions;
    private static final String COLLECTION = "customers";


    public CustomerDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.functions = FirebaseFunctions.getInstance();
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

    /**
     * {@inheritDoc}
     * Clears the collection by invoking a Firebase Cloud Function.
     * This avoids downloading data to the client and deletes the collection server-side.
     */
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Prepare the data to be sent to the Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("collectionPath", COLLECTION);

        // Call of the Cloud Function 'deleteCollection'
        functions.getHttpsCallable("deleteCollection")
                .call(data)
                .addOnSuccessListener(result -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updateShoppingCart(String customerId, ShoppingCart shoppingCart) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (shoppingCart == null)
            future.completeExceptionally(new IllegalArgumentException("Customer's shopping cart cannot be null"));
        else {
            db.collection("customers").document(customerId)
                    .update("shoppingCart", shoppingCart)
                    .addOnSuccessListener(v -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        }
        return future;
    }

}