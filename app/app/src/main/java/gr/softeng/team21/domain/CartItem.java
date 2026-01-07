package gr.softeng.team21.domain;
import gr.softeng.team21.util.Money;
/**
 * Represents an item in the shopping cart.
 * Each CartItem corresponds to a specific product type
 * and a quantity, and maintains a subtotal amount.
 *
 * @author PAVLOS GRATSANIS
 * @version 1.0
 * AM: 3230036
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
     * Constructs a CartItem with a given product type and quantity.
     * Automatically assigns an ID and calculates the subtotal.
     * @param productType the product type
     * @param quantity the quantity of the product
     */
    public CartItem(ProductType productType, int quantity) {
        this.productType = productType;
        this.quantity = quantity;
        this.id = ++counter;
        calculateSubtotal();
    }

    /**
     * Constructs a CartItem with a given product type.
     * @param productType the product type
     */
    public CartItem(ProductType productType) {
        this.productType = productType;
    }


    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product and recalculates the subtotal.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    /**
     * Calculates the subtotal amount based on product price and quantity.
     */
    public void calculateSubtotal() {
        this.subtotal_amount = productType.getPrice().multiply(quantity);
    }


    public Money getSubtotal_amount() {
        return subtotal_amount;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public ProductType getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        String productName = productType.getProductname();
        String totalStr = subtotal_amount.toString();
        return productName + " (x" + quantity + ") -- " + totalStr;
    }
}
