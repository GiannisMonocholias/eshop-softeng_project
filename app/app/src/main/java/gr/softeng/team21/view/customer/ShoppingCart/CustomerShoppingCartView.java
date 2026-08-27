package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;

/**
 * Interface for the ShoppingCart .
 * Defines the methods for displaying cart items, total price and payment navigation.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerShoppingCartView {
    /**
     * Displays a message to the user (error or success).
     * @param msg The message to display.
     */
    void showMessage(String msg);

    /**
     * Navigates to the payment screen.
     */
    void goToPayment();

    /**
     * Displays the calculated total price of the cart.
     * @param price The formatted price string.
     */
    void showTotalPrice(String price);

    /**
     * Displays the list of items currently in the shopping cart.
     * @param cartItems The list of CartItem objects.
     */
    void showCartItems(ArrayList<CartItem> cartItems);

    void goBack();
}