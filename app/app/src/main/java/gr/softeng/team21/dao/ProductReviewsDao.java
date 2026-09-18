package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.ProductReview;

public interface ProductReviewsDao {

    CompletableFuture<HashMap<String, ProductReview>> getReviews();

    CompletableFuture<ProductReview> getReview(String id);
    CompletableFuture<HashMap<String, ProductReview>> getReviewsByProduct(String productCode);
    CompletableFuture<Void> addReview(ProductReview productReview);
    CompletableFuture<Void> removeReview(ProductReview productReview);
    CompletableFuture<Void> clear();
}
