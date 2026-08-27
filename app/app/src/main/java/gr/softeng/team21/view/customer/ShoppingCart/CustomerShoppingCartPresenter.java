package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Money;

/**
 * Presenter for the ShoppingCart activity.
 * Handles interactions between the {@link CustomerShoppingCartView} and the domain logic,
 * managing cart updates, calculations, and payment validation using asynchronous DAOs.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartPresenter {
    private final CustomerShoppingCartView view;
    private final CustomerDAO customerDAO;
    private Customer customer;

    /**
     * Initializes the presenter with the view and the customer data access object.
     * @param view The view interface.
     * @param customerDAO The customer data access object (injected).
     */
    public CustomerShoppingCartPresenter(CustomerShoppingCartView view, CustomerDAO customerDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
    }

    /**
     * Asynchronously loads the customer data and refreshes the cart view.
     * @param customerId The unique identifier of the customer.
     */
    public void loadInitialData(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
                refreshClicked(); // Φορτώνει τα δεδομένα στο UI μόλις έρθει ο πελάτης
            } else {
                if (view != null) view.showMessage("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
            return null;
        });
    }

    /**
     * Handles the click for viewing the payment.
     * Checks if the cart is empty before proceeding to payment.
     */
    public void ContinuePaymentClicked() {
        if (customer == null || customer.getShoppingCart() == null || customer.getShoppingCart().getItems().isEmpty()) {
            if (view != null) view.showMessage("Το καλάθι είναι άδειο!");
        } else {
            if (view != null) view.goToPayment();
        }
    }

    /**
     * Increases the quantity of a specific item in the cart.
     * @param item The cart item to update.
     */
    public void plusClicked(CartItem item) {
        if (customer == null || item == null) return;
        try {
            customer.addItemToCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            if (view != null) view.showMessage(e.getMessage());
        }
    }

    /**
     * Decreases the quantity of a specific item in the cart.
     * @param item The cart item to update.
     */
    public void minusClicked(CartItem item) {
        if (customer == null || item == null) return;
        try {
            customer.removeItemFromCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            if (view != null) view.showMessage(e.getMessage());
        }
    }

    /**
     * Removes a specific item completely from the cart.
     * @param item The cart item to delete.
     */
    public void deleteClicked(CartItem item) {
        if (customer == null || item == null) return;
        try {
            customer.removeItemFromCart(item.getProductType(), item.getQuantity());
            refreshClicked();
            if (view != null) view.showMessage("Αφαιρέθηκε");
        } catch (Exception e) {
            if (view != null) view.showMessage(e.getMessage());
        }
    }

    /**
     * Calculates and updates the total price in the screen.
     */
    public void setTotalprice() {
        if (customer != null && customer.getShoppingCart() != null) {
            Money totalCost = customer.getShoppingCart().getTotalCost();
            if (view != null) view.showTotalPrice(String.format("%.2f €", totalCost.getAmount()));
        }
    }

    /**
     * Retrieves the cart items from the domain and updates the view.
     */
    public void loadCartData() {
        if (customer != null && customer.getShoppingCart() != null) {
            if (view != null) view.showCartItems(new ArrayList<>(customer.getShoppingCart().getItems()));
        } else {
            if (view != null) view.showCartItems(new ArrayList<>());
        }
    }

    /**
     * Refreshes the shopping cart data and the total price by calling the corresponding methods.
     */
    public void refreshClicked() {
        setTotalprice();
        loadCartData();
    }

    public void BackToSearchClicked() {
        if (view != null) {
            view.showMessage("Μετάβαση στην Αναζήτηση Προϊόντων....");
            view.goBack();
        }
    }
}