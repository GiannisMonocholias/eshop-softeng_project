package gr.softeng.team21.domain;

import java.util.ArrayList;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Represents a supply order made to wholesale providers.
 */
public class SupOrder {

    private Date orderDate;
    private int orderid; // Tip: Consider changing this to String if you use auto-generated Firebase IDs
    private String adminId; // Changed from 'Admin admin' to Foreign Key
    private ArrayList<OrderLine> orderProducts;

    /**
     * Default constructor required for Firebase deserialization.
     */
    public SupOrder() {
        this.orderProducts = new ArrayList<>();
    }

    /**
     * Constructs a new Supply Order.
     */
    public SupOrder(Date date, int id, String adminId, ArrayList<OrderLine> orproducts) {
        this.orderDate = date;
        this.orderid = id;
        this.adminId = adminId;
        this.orderProducts = (orproducts != null) ? orproducts : new ArrayList<>();
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getId() {
        return orderid;
    }

    public void setId(int id) {
        this.orderid = id;
    }

    public Date getDate() {
        return orderDate;
    }

    public void setDate(Date date) {
        this.orderDate = date;
    }

    public ArrayList<OrderLine> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(ArrayList<OrderLine> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * Calculates the total monetary amount of the supply order.
     */
    public Money fullAmount() {
        Money sum = new Money(0, "euro");
        for (OrderLine line : orderProducts) {
            sum.setAmount(sum.getAmount().add(line.totalBill()));
        }
        return sum;
    }
}