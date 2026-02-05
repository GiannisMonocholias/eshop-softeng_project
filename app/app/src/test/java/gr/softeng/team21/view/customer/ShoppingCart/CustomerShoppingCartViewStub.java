package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;

/**
 * Stub implementation of {@link CustomerShoppingCartView} for testing purposes.
 * It provides a mechanism to capture UI feedback (messages, price updates) and
 * track the state of UI actions (navigation, displaying items) during shopping cart interactions.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartViewStub implements CustomerShoppingCartView {
    private String message;
    private int goToPaymentCount = 0;
    private String TotalPrice;
    private ArrayList<CartItem> CartItems;
    private int showCartItemsCount = 0;

    /**
     * Returns the total price.
     * Used for verification in tests.
     * @return The total price string.
     */
    public String getTotalPrice() {
        return TotalPrice;
    }

    /**
     * Returns the list of cart items displayed in the view.
     * Used for verification in tests.
     * @return The list of CartItem objects.
     */
    public ArrayList<CartItem> getCartItems() {
        return CartItems;
    }

    /**
     * Returns the number of times the cart items were updated.
     * Used for verification in tests.
     * @return The show cart items count.
     */
    public int getShowCartItemsCount() {
        return showCartItemsCount;
    }

    /**
     * Returns the last message (success or error).
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the number of times navigation to payment was triggered.
     * Used for verification in tests.
     * @return The navigation count.
     */
    public int getGoToPaymentCount() {
        return goToPaymentCount;
    }

    /**
     * {@inheritDoc}
     * Stores the message in a variable for verification.
     */
    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for payment navigation.
     */
    @Override
    public void goToPayment() {
        goToPaymentCount++;
    }

    /**
     * {@inheritDoc}
     * Stores the formatted total price string.
     */
    @Override
    public void showTotalPrice(String price) {
        TotalPrice = price;
    }

    /**
     * {@inheritDoc}
     * Stores the list of cart items and increments the display counter.
     */
    @Override
    public void showCartItems(ArrayList<CartItem> cartItems) {
        this.CartItems = cartItems;
        this.showCartItemsCount++;
    }

    @Override
    public void goBack() {

    }
}