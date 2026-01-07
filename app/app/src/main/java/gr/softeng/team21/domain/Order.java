package gr.softeng.team21.domain;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Represents an order in the system containing transaction details, status,cost and the associated shopping cart.

 * @author PAVLOS GRATSANIS
 * @version 1.0
 * AM: 3230036
 */
public class Order {
    /**
     * The unique code identifying the order
     */
    private String ordercode;

    /**
     * The date when the order was submitted
     */
    private Date submissiondate;

    /**
     * The expected or actual delivery date
     */
    private Date deliverydate;

    /**
     * The current status of the order
     */
    private OrderStatusType orderstatus;

    /**
     * Indicates if the order has been paid
     */
    private boolean paid;

    /**
     * The method used for payment
     */
    private PaymentType paymentmethod;

    /**
     * The shopping cart associated with this order
     */
    private ShoppingCart shoppingCart;

    /**
     * The total monetary amount of the order
     */
    private Money total_amount;

    /**
     * Creates a new Order with the specified details.
     * A copy  shopping cart is created and assigned to the order.
     *
     * @param ordercode      the unique code for the order
     * @param submissiondate the date the order was submitted
     * @param orderstatus    the initial status of the order
     * @param paid           true if the order is paid, false otherwise
     * @param paymentmethod  the method of payment
     * @param deliverydate   the delivery date
     * @param shoppingCart   the shopping cart to be copied for this order
     */
    public Order(String ordercode, Date submissiondate, OrderStatusType orderstatus,
                 boolean paid, PaymentType paymentmethod, Date deliverydate, ShoppingCart shoppingCart) {


        this.ordercode = ordercode;
        this.submissiondate = submissiondate;
        this.deliverydate = deliverydate;
        this.orderstatus = orderstatus;
        this.paid = paid;
        this.paymentmethod = paymentmethod;
        this.shoppingCart = shoppingCart.copy();
    }

    public Money getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Money total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public Date getSubmissiondate() {
        return submissiondate;
    }

    public void setSubmissiondate(Date submissiondate) {
        this.submissiondate = submissiondate;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public OrderStatusType getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(OrderStatusType orderstatus) {
        this.orderstatus = orderstatus;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public PaymentType getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(PaymentType paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
