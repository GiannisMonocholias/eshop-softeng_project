package gr.softeng.team21.domain;

import java.io.Serializable;

import gr.softeng.team21.util.Date;

public class ProductReview implements Serializable {

    private int stars;
    private String comment;
    private String productReviewId;
    private String productCode;
    private String customerName;
    private Date reviewDate;

    public ProductReview() {
    }


    public ProductReview(int stars, Date reviewDate, String customerName, String productCode, String productReviewId,String comment) {
        this.stars = stars;
        this.reviewDate = reviewDate;
        this.customerName = customerName;
        this.productCode = productCode;
        this.productReviewId = productReviewId;
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
        return productReviewId;
    }

    public void setProductReviewId(String productReviewId) {
        this.productReviewId = productReviewId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
