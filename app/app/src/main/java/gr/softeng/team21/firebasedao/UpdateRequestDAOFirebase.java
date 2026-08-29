package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Firebase Firestore implementation of the {@link UpdateRequestDAO} interface.
 * Bridges Firebase's native asynchronous Tasks to Java's CompletableFuture for non-blocking UI.
 * Utilizes native Firestore indexed queries (whereEqualTo) to efficiently retrieve filtered requests.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOFirebase implements UpdateRequestDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "update_requests";

    /**
     * Initializes the DAO and obtains the active Firebase Firestore instance.
     */
    public UpdateRequestDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId) {
        CompletableFuture<CatalogueUpdateRequest> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).document(String.valueOf(requestId)).get()
                .addOnSuccessListener(doc -> future.complete(doc.exists() ? doc.toObject(CatalogueUpdateRequest.class) : null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> addUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request == null) {
            future.completeExceptionally(new IllegalArgumentException("Request must not be null"));
            return future;
        }
        String docId = String.valueOf(request.getId());
        db.collection(COLLECTION_NAME).document(docId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) future.completeExceptionally(new IllegalArgumentException("Request exists"));
            else {
                db.collection(COLLECTION_NAME).document(docId).set(request)
                        .addOnSuccessListener(aVoid -> future.complete(null))
                        .addOnFailureListener(future::completeExceptionally);
            }
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> updateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request == null) {
            future.completeExceptionally(new IllegalArgumentException("Request must not be null"));
            return future;
        }
        db.collection(COLLECTION_NAME).document(String.valueOf(request.getId())).set(request)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (request == null) {
            future.completeExceptionally(new IllegalArgumentException("Request must not be null"));
            return future;
        }
        db.collection(COLLECTION_NAME).document(String.valueOf(request.getId())).delete()
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> getUpdateRequests() {
        CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME).get().addOnSuccessListener(querySnapshot -> {
            HashMap<Integer, CatalogueUpdateRequest> map = new HashMap<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                CatalogueUpdateRequest req = doc.toObject(CatalogueUpdateRequest.class);
                map.put(req.getId(), req);
            }
            future.complete(map);
        }).addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     * Executes an indexed Firestore query to fetch only the documents matching the given Foreign Key.
     */
    @Override
    public CompletableFuture<ArrayList<CatalogueUpdateRequest>> getRequestsByEmployeeId(String employeeId) {
        CompletableFuture<ArrayList<CatalogueUpdateRequest>> future = new CompletableFuture<>();
        db.collection(COLLECTION_NAME)
                .whereEqualTo("assignedEmployeeId", employeeId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    ArrayList<CatalogueUpdateRequest> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        list.add(doc.toObject(CatalogueUpdateRequest.class));
                    }
                    future.complete(list);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * {@inheritDoc}
     * Throws an unsupported exception as bulk document deletion should be handled via Cloud Functions in production.
     */
    @Override
    public CompletableFuture<Void> clear() {
        return new CompletableFuture<>();
    }
}