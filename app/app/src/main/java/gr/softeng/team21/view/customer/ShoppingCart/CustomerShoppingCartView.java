package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;

public interface CustomerShoppingCartView {
    void showMessage(String message);
    void goToPayment();
    void showTotalPrice(String price);

    void showCartItems(ArrayList<CartItem> cartItems);
}
