package gr.softeng.team21.view.product;

/**
 * Stub implementation of {@link ProductDetailsView} for testing purposes.
 * It provides a mechanism to capture UI feedback (messages, quantity updates) and
 * track the state of UI actions (add to cart) during the product details flow.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsViewStub implements ProductDetailsView {

    private String name, code, price, description;
    private int quantity;
    private String message;
    private int addToCartCount = 0;
    private int CartCount = 0;

    /**
     * {@inheritDoc}
     * Stores the product details in local variables to simulate UI population.
     */
    @Override
    public void showProductDetails(String name, String code, String price, String description, String imgCode) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.description = description;
    }

    /**
     * {@inheritDoc}
     * Stores the current quantity displayed to the user.
     */
    @Override
    public void showQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * {@inheritDoc}
     * Stores the message displayed to the user.
     */
    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    /**
     * {@inheritDoc}
     * Increments the counter tracking successful additions to the cart.
     */
    @Override
    public void showAddToCartSuccess() {
        this.addToCartCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter tracking navigation events to the cart.
     */
    @Override
    public void goToCart() {
        this.CartCount++;
    }

    /**
     * Returns the product name
     * Used for verification in tests.
     * @return The product name.
     */
    public String getName() { return name; }

    /**
     * Returns the product code .
     * Used for verification in tests.
     * @return The product code.
     */
    public String getCode() { return code; }

    /**
     * Returns the product price.
     * Used for verification in tests.
     * @return The product price.
     */
    public String getPrice() { return price; }

    /**
     * Returns the quantity.
     * Used for verification in tests.
     * @return The quantity.
     */
    public int getQuantity() { return quantity; }

    /**
     * Returns the last message.
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() { return message; }

    /**
     * Returns the count of successful add-to-cart actions.
     * Used for verification in tests.
     * @return The add-to-cart count.
     */
    public int getAddToCartCount() { return addToCartCount; }

    /**
     * Returns the count of navigation actions to the cart.
     * Used for verification in tests.
     * @return The cart navigation count.
     */
    public int getCartCount() { return CartCount; }
}