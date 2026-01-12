package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Money;

/**
 * Presenter for the ShoppingCart activity.
 * Handles interactions between the {@link CustomerShoppingCartView} and the domain logic,
 * managing cart updates, calculations, and payment validation.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartPresenter {
    private CustomerShoppingCartView view;
    private Customer customer;

    /**
     * Initializes the presenter with the view and  customer.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public CustomerShoppingCartPresenter(CustomerShoppingCartView view, Customer customer) {
        this.view = view;
        this.customer = customer;

    }

    /**
     * Handles the click  for viewing the payment.
     * Checks if the cart is empty before proceeding to payment.
     */
    public void ContinuePaymentClicked() {
        if (customer.getShoppingCart().getItems().isEmpty()) {
            view.showMessage( "Το καλάθι είναι άδειο!");
        } else {
            view.goToPayment();
        }
    }

    /**
     * Increases the quantity of a specific item in the cart.
     * @param item The cart item to update.
     */
    public void plusClicked(CartItem item) {
        try {
            customer.addItemToCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    /**
     * Decreases the quantity of a specific item in the cart.
     * @param item The cart item to update.
     */
    public void minusClicked(CartItem item) {
        try {
            customer.removeItemFromCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    /**
     * Removes a specific item completely from the cart.
     * @param item The cart item to delete.
     */
    public void deleteClicked(CartItem item) {
        try {
            customer.removeItemFromCart(item.getProductType(), item.getQuantity());
            refreshClicked();
            view.showMessage("Αφαιρέθηκε");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    /**
     * Calculates and updates the total price in the screen.
     */
    public void setTotalprice() {
        if(customer.getShoppingCart()!=null){
            Money totalCost = customer.getShoppingCart().getTotalCost();
            view.showTotalPrice(String.format("%.2f €", totalCost.getAmount()));
        }
    }

    /**
     * Retrieves the cart items from the domain and updates the view.
     */
    public void loadCartData() {
        if (customer.getShoppingCart() != null) {
            view.showCartItems(new ArrayList<>(customer.getShoppingCart().getItems()));
        } else {
            view.showCartItems(new ArrayList<>());
        }
    }

    /**
     * Refreshes the shopping cart data and the total price by calling the corresponding methods.
     */
    public void refreshClicked() {
        setTotalprice();
        loadCartData();
    }
}