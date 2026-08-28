package gr.softeng.team21.dao;

import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.ProductReview;

public interface ProductReviewDao {

    CompletableFuture<ProductReview> getReview(String id);
    CompletableFuture<Void> addReview(ProductReview productReview);
    CompletableFuture<Void> removeReview(ProductReview productReview);
    CompletableFuture<Void> clear();
}
