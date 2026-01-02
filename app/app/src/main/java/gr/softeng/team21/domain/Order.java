package gr.softeng.team21.domain;

public class Order {
    private String ordercode;
    private Date submissiondate;
    private Date deliverydate;
    private OrderStatusType orderstatus;
    private boolean paid;
    private PaymentType paymentmethod;
    private ShoppingCart shoppingCart;
    private Money total_amount;


//ENUM ORDERSTATUS
    public Order (String ordercode, Date submissiondate , OrderStatusType orderstatus,
                  boolean paid, PaymentType paymentmethod, Date deliverydate, ShoppingCart shoppingCart) {
        this.ordercode = ordercode;
        this.submissiondate = submissiondate;
       this.deliverydate = deliverydate;
        this.orderstatus = orderstatus;
        this.paid = paid;
        this.paymentmethod = paymentmethod;
        this.shoppingCart=shoppingCart.copy ();
    }

    public Money getTotal_amount ( ) {
        return total_amount;
    }

    public void setTotal_amount (Money total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrdercode ( ) {
        return ordercode;
    }

    public void setOrdercode (String ordercode) {
        this.ordercode = ordercode;
    }

    public Date getSubmissiondate ( ) {
        return submissiondate;
    }

    public void setSubmissiondate (Date submissiondate) {
        this.submissiondate = submissiondate;
    }

    public Date getDeliverydate ( ) {
        return deliverydate;
    }

    public void setDeliverydate (Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public OrderStatusType getOrderstatus ( ) {
        return orderstatus;
    }

    public void setOrderstatus (OrderStatusType orderstatus) {
        this.orderstatus = orderstatus;
    }

    public boolean getPaid ( ) {
        return paid;
    }

    public void setPaid (boolean paid) {
        this.paid = paid;
    }

    public PaymentType getPaymentmethod ( ) {
        return paymentmethod;
    }

    public void setPaymentmethod (PaymentType paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public ShoppingCart getShoppingCart ( ) {
        return shoppingCart;
    }

    public void setShoppingCart (ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

}
