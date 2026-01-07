package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;

public class CustomerShoppingCartViewStub implements CustomerShoppingCartView {
    private String message;
    private int goToPaymentCount = 0;
    private String TotalPrice;
    private ArrayList<CartItem> CartItems;
    private int showCartItemsCount = 0;

    public String getTotalPrice() {
        return TotalPrice;
    }

    public ArrayList<CartItem> getCartItems() {
        return CartItems;
    }

    public int getShowCartItemsCount() {
        return showCartItemsCount;
    }

    public String getMessage() {
        return message;
    }

    public int getGoToPaymentCount() {
        return goToPaymentCount;
    }



    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    @Override
    public void goToPayment() {
        goToPaymentCount++;
    }

    @Override
    public void showTotalPrice(String price) {
        TotalPrice=price;
    }

    @Override
    public void showCartItems(ArrayList<CartItem> cartItems) {
        this.CartItems = cartItems;
        this.showCartItemsCount++;
    }
}
