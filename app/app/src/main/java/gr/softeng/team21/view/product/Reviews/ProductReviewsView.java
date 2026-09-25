package gr.softeng.team21.view.product.Reviews;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductReview;

public interface ProductReviewsView {
    void showReviews(ArrayList<ProductReview> reviews);
    void showMessage(String message);
}
