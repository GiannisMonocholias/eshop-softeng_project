package gr.softeng.team21.domain;

import gr.softeng.team21.util.Money;

/**
 * Represents an item in the shopping cart.
 * Each CartItem corresponds to a specific product type
 * and a quantity, and maintains a subtotal amount dynamically.
 *
 * @author PAVLOS GRATSANIS
 */
public class CartItem {

    /** The quantity of the product in the cart */
    private int quantity;

    /** Static counter used to generate unique IDs */
    private static int counter;

    /** The subtotal amount for this cart item */
    private Money subtotal_amount;

    /** Unique id of the cart item */
    private int id;

    /** The type of product associated with this cart item */
    private ProductType productType;

    /**
     * Default constructor required for framework instantiation (e.g., Firebase).
     */
    public CartItem() {}

    /**
     * Constructs a CartItem with a given product type and quantity.
     * Automatically assigns an ID.
     * @param productType the product type
     * @param quantity the quantity of the product
     */
    public CartItem(ProductType productType, int quantity) {
        this.productType = productType;
        this.quantity = quantity;
        this.id = ++counter;
    }

    /**
     * Constructs a CartItem with a given product type.
     * @param productType the product type
     */
    public CartItem(ProductType productType) {
        this.productType = productType;
    }

    /**
     * Returns the quantity of the product.
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product.
     * Simply assigns the value without triggering calculations to ensure
     * safe asynchronous loading from Firebase.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the subtotal amount based on product price and quantity.
     * Retained for compatibility, includes null-checks to prevent NPEs.
     */
    public void calculateSubtotal() {
        if (this.productType != null && this.productType.getPrice() != null) {
            this.subtotal_amount = this.productType.getPrice().multiply(quantity);
        } else {
            this.subtotal_amount = new Money(0, "€");
        }
    }

    /**
     * Returns the subtotal amount for this cart item dynamically (On-Demand).
     * Calculates the subtotal only when requested to avoid exceptions from
     * incomplete Firebase data.
     *
     * @return the subtotal amount
     */
    public Money getSubtotal_amount() {
        // Calculate the accurate amount on-demand if data is fully loaded
        if (this.productType != null && this.productType.getPrice() != null) {
            return this.productType.getPrice().multiply(this.quantity);
        }

        // Return pre-existing or default value if called before full instantiation
        if (this.subtotal_amount != null) {
            return this.subtotal_amount;
        }
        return new Money(0, "€");
    }

    /**
     * Sets the subtotal amount for this cart item.
     * @param subtotal_amount the new subtotal amount
     */
    public void setSubtotal_amount(Money subtotal_amount) {
        this.subtotal_amount = subtotal_amount;
    }

    /**
     * Returns the unique ID of the cart item.
     * @return the cart item ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the cart item.
     * @param id the new cart item ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the product type associated with this cart item.
     * @return the product type
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the product type associated with this cart item.
     * Simply assigns the value without triggering calculations to ensure
     * safe asynchronous loading from Firebase.
     *
     * @param productType the new product type
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    /**
     * Returns a string representation of the cart item.
     * The string includes the product name, the quantity and the subtotal amount.
     * @return a formatted string representing the cart item
     */
    @Override
    public String toString() {
        // Safe access to product name to avoid NPE
        String productName = (productType != null && productType.getProductName() != null) ? productType.getProductName() : "Unknown Product";

        // Use the on-demand dynamic calculation
        Money currentSubtotal = getSubtotal_amount();
        String totalStr = (currentSubtotal != null) ? currentSubtotal.toString() : "0.00 €";

        return productName + " (x" + quantity + ") -- " + totalStr;
    }
}