package gr.softeng.team21.view.customer.ShoppingCart;

import java.util.ArrayList;

import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Money;

public class CustomerShoppingCartPresenter {
    private CustomerShoppingCartView view;
    private Customer customer;

    public CustomerShoppingCartPresenter(CustomerShoppingCartView view, Customer customer) {
        this.view = view;
        this.customer = customer;

    }

    public void ContinuePaymentClicked() {
        if (customer.getShoppingCart().getItems().isEmpty()) {
        view.showMessage( "Το καλάθι είναι άδειο!");
        } else {
           view.goToPayment();
        }
    }

    public void plusClicked(CartItem item) {
        try {
            customer.addItemToCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    public void minusClicked(CartItem item) {
        try {
            customer.removeItemFromCart(item.getProductType(), 1);
            refreshClicked();
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    public void deleteClicked(CartItem item) {
        try {
            customer.removeItemFromCart(item.getProductType(), item.getQuantity());
            refreshClicked();
           view.showMessage("Αφαιρέθηκε");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    public void setTotalprice() {
        if(customer.getShoppingCart()!=null){
            Money totalCost = customer.getShoppingCart().getTotalCost();
            view.showTotalPrice(String.format("%.2f €", totalCost.getAmount()));
        }
    }

    public void loadCartData() {
        if (customer.getShoppingCart() != null) {
            view.showCartItems(new ArrayList<>(customer.getShoppingCart().getItems()));
        } else {
            view.showCartItems(new ArrayList<>());
        }
    }

    public void refreshClicked() {
        setTotalprice();
        loadCartData();
    }
}
