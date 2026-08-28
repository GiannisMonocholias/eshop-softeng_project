package gr.softeng.team21.domain;

public class ProductReview {

    private int stars;
    private String comment;
    private String ProductReviewId;
    private int productId;
    private int customerId;


    public ProductReview(int stars, int customerId, int productId, String productReviewId, String comment) {
        this.stars = stars;
        this.customerId = customerId;
        this.productId = productId;
        ProductReviewId = productReviewId;
        this.comment = comment;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getProductReviewId() {
        return ProductReviewId;
    }

    public void setProductReviewId(String productReviewId) {
        ProductReviewId = productReviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
