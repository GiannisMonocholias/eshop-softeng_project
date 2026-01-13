package gr.softeng.team21.view.product;

/**
 * Interface for the Product Details.
 * Defines the methods for displaying product information, quantity updates, and shopping cart navigation.
 * @author PAVLOS GRATSANIS
 */
public interface ProductDetailsView {

    /**
     * Displays the details of the product on the screen.
     * @param name The name of the product.
     * @param code The unique code of the product.
     * @param price The price of the product formatted as a string.
     * @param description The description of the product.
     * @param imgCode The code used to retrieve from drawable folder the product image.
     */
    void showProductDetails(String name, String code, String price, String description, String imgCode);

    /**
     * Updates the displayed quantity.
     * @param quantity The current quantity selected.
     */
    void showQuantity(int quantity);

    /**
     * Displays a message to the user (error or success).
     * @param msg The message to display.
     */
    void showMessage(String msg);

    /**
     * Shows a confirmation dialog or message indicating successful addition to the cart.
     */
    void showAddToCartSuccess();

    /**
     * Navigates to the ShoppingCart activity.
     */
    void goToCart();
}