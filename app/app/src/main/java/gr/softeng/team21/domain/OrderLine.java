package gr.softeng.team21.domain;

import java.math.BigDecimal;

/**
 * Represents a single line item within a supply order.
 */
public class OrderLine {

    private WholesaleProduct product;
    private int quantity;

    /**
     * Default constructor required for Firebase deserialization.
     */
    public OrderLine() {
    }

    /**
     * Constructs a new OrderLine.
     * The SupOrder reference was removed to prevent circular dependencies in Firebase.
     */
    public OrderLine(WholesaleProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public WholesaleProduct getProduct() {
        return product;
    }

    public void setProduct(WholesaleProduct product) {
        this.product = product;
    }

    /**
     * Calculates the total bill for this specific order line.
     */
    public BigDecimal totalBill() {
        if (product == null || product.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().getAmount().multiply(BigDecimal.valueOf(quantity));
    }
}