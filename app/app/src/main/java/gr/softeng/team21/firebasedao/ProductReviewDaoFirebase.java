package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.dao.ProductReviewDao;
import gr.softeng.team21.domain.ProductReview;

public class ProductReviewDaoFirebase implements ProductReviewDao {
    private final FirebaseFirestore db;
    private static final String COLLECTION = "reviews";

    public ProductReviewDaoFirebase() {
        this.db = FirebaseFirestore.getInstance();
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
        future.completeExceptionally(new UnsupportedOperationException("Bulk delete requires Cloud Functions."));
        return future;
    }
}
