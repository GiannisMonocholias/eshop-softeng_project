package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.HashMap;

import gr.softeng.team21.dao.ProductReviewDao;
import gr.softeng.team21.domain.ProductReview;

public class ProductReviewDaoFirebase implements ProductReviewDao {
    private final FirebaseFirestore db;
    private final FirebaseFunctions functions;
    private static final String COLLECTION = "reviews";

    public ProductReviewDaoFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.functions = FirebaseFunctions.getInstance();
    }

    @Override
    public CompletableFuture<HashMap<String, ProductReview>> getReviews() {
        CompletableFuture<HashMap<String, ProductReview>> future = new CompletableFuture<>();

        db.collection(COLLECTION).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, ProductReview> map = new HashMap<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ProductReview review = doc.toObject(ProductReview.class);
                        map.put(review.getProductReviewId(), review);
                    }
                    future.complete(map);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<ProductReview> getReview(String id) {
        CompletableFuture<ProductReview> future = new CompletableFuture<>();
        db.collection(COLLECTION).document(id).get()
                .addOnSuccessListener(doc -> future.complete(doc.exists() ? doc.toObject(ProductReview.class) : null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    @Override
    public CompletableFuture<Void> addReview(ProductReview productReview) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (productReview == null) {
            future.completeExceptionally(new IllegalArgumentException("ProductReview cannot be null"));
        } else {
            db.collection(COLLECTION).document(productReview.getProductReviewId()).set(productReview)
                    .addOnSuccessListener(v -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        }

        return future;
    }

    @Override
    public CompletableFuture<Void> removeReview(ProductReview productReview) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (productReview == null) {
            future.completeExceptionally(new IllegalArgumentException("ProductReview cannot be null"));
        } else {
            db.collection(COLLECTION).document(productReview.getProductReviewId()).delete()
                    .addOnSuccessListener(v -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        }
        return future;
    }

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
}
