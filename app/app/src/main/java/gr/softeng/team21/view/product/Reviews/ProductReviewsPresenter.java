package gr.softeng.team21.view.product.Reviews;


import java.util.ArrayList;

import gr.softeng.team21.dao.ProductReviewsDao;
import gr.softeng.team21.domain.ProductReview;

public class ProductReviewsPresenter {
   private ProductReviewsView view;
private ProductReviewsDao reviewsDao;
    public ProductReviewsPresenter(ProductReviewsView view,
                                   ProductReviewsDao productReviewsDao) {
        this.view=view;
        this.reviewsDao=productReviewsDao;
    }
    public void loadReviews(String productCode) {
        reviewsDao.getReviewsByProduct(productCode).thenAccept(reviewsMap -> {
            ArrayList<ProductReview> filteredList = new ArrayList<>(reviewsMap.values());
            if (!filteredList.isEmpty()) {
                if (view != null) {
                    view.showReviews(filteredList);
                }
            }else {
                view.showMessage("Δεν υπάρχουν αξιολογήσεις για αυτό το προϊόν.");
            }
        }).exceptionally(e -> {
            if (view != null) {
                view.showMessage("Σφάλμα φόρτωσης αξιολογήσεων: " + e.getMessage());
            }
            return null;
        });
    }
}
