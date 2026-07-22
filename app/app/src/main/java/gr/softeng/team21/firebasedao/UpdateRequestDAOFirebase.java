package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Firebase implementation of the {@link UpdateRequestDAO} interface.
 * Bridges Firebase's async Tasks to Java's CompletableFuture for non-blocking UI.
 * Handles Firestore database operations for Catalogue Update Requests.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOFirebase implements UpdateRequestDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "update_requests";

    /**
     * Initializes the Firebase Firestore instance.
     */
    public UpdateRequestDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * {@inheritDoc}
     * <p>Fetches the update request document directly from the Firestore database.</p>
     */
    @Override
    public CompletableFuture<CatalogueUpdateRequest> getUpdateRequest(int requestId) {
        CompletableFuture<CatalogueUpdateRequest> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).document(String.valueOf(requestId)).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        CatalogueUpdateRequest request = documentSnapshot.toObject(CatalogueUpdateRequest.class);
                        future.complete(request);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Saves the given request as a document in the Firestore database.
     * Verifies if the request ID already exists to prevent accidental data overwriting.</p>
     */
    @Override
    public CompletableFuture<Void> addUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (request == null) {
            future.completeExceptionally(new IllegalArgumentException("Request argument must not be null"));
            return future;
        }

        String docId = String.valueOf(request.getId());

        db.collection(COLLECTION_NAME).document(docId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        future.completeExceptionally(new IllegalArgumentException("Request already in repository"));
                    } else {
                        db.collection(COLLECTION_NAME).document(docId)
                                .set(request)
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Deletes the document corresponding to the request ID from the Firestore database.</p>
     */
    @Override
    public CompletableFuture<Void> deleteUpdateRequest(CatalogueUpdateRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (request == null) {
            future.completeExceptionally(new IllegalArgumentException("Request argument must not be null"));
            return future;
        }

        String docId = String.valueOf(request.getId());

        db.collection(COLLECTION_NAME).document(docId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection(COLLECTION_NAME).document(docId).delete()
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    } else {
                        future.completeExceptionally(new IllegalArgumentException("Request is not in repository"));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Retrieves all request documents from the Firestore collection and maps them into a HashMap.</p>
     */
    @Override
    public CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> getUpdateRequests() {
        CompletableFuture<HashMap<Integer, CatalogueUpdateRequest>> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<Integer, CatalogueUpdateRequest> requestsMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CatalogueUpdateRequest request = document.toObject(CatalogueUpdateRequest.class);
                        requestsMap.put(request.getId(), request);
                    }
                    future.complete(requestsMap);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Iterates through all documents within the Firestore collection and deletes them sequentially.</p>
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