package gr.softeng.team21.domain;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Represents an order in the system containing transaction details, status,cost
 * and the associated shopping cart.
 * @author PAVLOS GRATSANIS
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

    /**
     * Returns the total monetary amount of the order.
     * @return the total amount
     */
    public Money getTotal_amount() {
        return total_amount;
    }

    /**
     * Sets the total monetary amount of the order.
     * @param total_amount the new total amount
     */
    public void setTotal_amount(Money total_amount) {
        this.total_amount = total_amount;
    }

    /**
     * Returns the unique code of the order.
     * @return the order code
     */
    public String getOrdercode() {
        return ordercode;
    }

    /**
     * Sets the unique code of the order.
     * @param ordercode the new order code
     */
    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    /**
     * Returns the submission date of the order.
     * @return the submission date
     */
    public Date getSubmissiondate() {
        return submissiondate;
    }

    /**
     * Sets the submission date of the order.
     * @param submissiondate the new submission date
     */
    public void setSubmissiondate(Date submissiondate) {
        this.submissiondate = submissiondate;
    }

    /**
     * Returns the delivery date of the order.
     * @return the delivery date
     */
    public Date getDeliverydate() {
        return deliverydate;
    }

    /**
     * Sets the delivery date of the order.
     * @param deliverydate the new delivery date
     */
    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    /**
     * Returns the current status of the order.
     * @return the order status
     */
    public OrderStatusType getOrderstatus() {
        return orderstatus;
    }

    /**
     * Sets the current status of the order.
     * @param orderstatus the new order status
     */
    public void setOrderstatus(OrderStatusType orderstatus) {
        this.orderstatus = orderstatus;
    }

    /**
     * Checks if the order has been paid.
     * @return true if paid, false otherwise
     */
    public boolean getPaid() {
        return paid;
    }

    /**
     * Sets the payment status of the order.
     * @param paid true if paid, false otherwise
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Returns the payment method used for the order.
     * @return the payment method
     */
    public PaymentType getPaymentmethod() {
        return paymentmethod;
    }

    /**
     * Sets the payment method used for the order.
     * @param paymentmethod the new payment method
     */
    public void setPaymentmethod(PaymentType paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    /**
     * Returns the shopping cart associated with this order.
     * @return the shopping cart
     */
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Sets the shopping cart associated with this order.
     * @param shoppingCart the new shopping cart
     */
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}