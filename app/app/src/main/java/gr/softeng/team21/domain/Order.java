package gr.softeng.team21.domain;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Represents an order in the system containing transaction details, status, cost,
 * and foreign keys identifying the assigned employees.
 * @author PAVLOS GRATSANIS
 */
public class Order {
    private String orderCode;
    private Date submissiondate;
    private Date deliverydate;
    private OrderStatusType orderStatus;
    private boolean paid;
    private PaymentType paymentMethod;
    private ShoppingCart shoppingCart;
    private Money total_amount;

    // Foreign Keys for Employee Assignments (null initially)
    private String delivererId = null;
    private String customerServiceId = null;
    private String preparationEmployeeId = null;

    /**
     * Default constructor required for framework instantiation (e.g., Firebase).
     */
    public Order() {
    }

    /**
     * Creates a new Order with the specified details.
     * Note: Employee IDs are left as null because they are assigned later during processing.
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
        this.orderCode = ordercode;
        this.submissiondate = submissiondate;
        this.deliverydate = deliverydate;
        this.orderStatus = orderstatus;
        this.paid = paid;
        this.paymentMethod = paymentmethod;
        this.shoppingCart = shoppingCart.copy();
    }

    public String getDelivererId() { return delivererId; }
    public void setDelivererId(String delivererId) { this.delivererId = delivererId; }

    public String getCustomerServiceId() { return customerServiceId; }
    public void setCustomerServiceId(String customerServiceId) { this.customerServiceId = customerServiceId; }

    public String getPreparationEmployeeId() { return preparationEmployeeId; }
    public void setPreparationEmployeeId(String preparationEmployeeId) { this.preparationEmployeeId = preparationEmployeeId; }

    public Money getTotal_amount() { return total_amount; }
    public void setTotal_amount(Money total_amount) { this.total_amount = total_amount; }

    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String ordercode) { this.orderCode = ordercode; }

    public Date getSubmissiondate() { return submissiondate; }
    public void setSubmissionDate(Date submissiondate) { this.submissiondate = submissiondate; }

    public Date getDeliverydate() { return deliverydate; }
    public void setDeliveryDate(Date deliverydate) { this.deliverydate = deliverydate; }

    public OrderStatusType getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatusType orderstatus) { this.orderStatus = orderstatus; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public PaymentType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentType paymentmethod) { this.paymentMethod = paymentmethod; }

    public ShoppingCart getShoppingCart() { return shoppingCart; }
    public void setShoppingCart(ShoppingCart shoppingCart) { this.shoppingCart = shoppingCart; }
}