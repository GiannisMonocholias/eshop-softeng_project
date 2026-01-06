package gr.softeng.team21.domain;

import java.util.ArrayList;

import gr.softeng.team21.util.Money;

public class ShoppingCart {
    private Customer customer;
    private Money totalcost;
    private ArrayList<CartItem> items = new ArrayList<>();
    private Order order;


    public ShoppingCart (Customer customer) {
        this.customer=customer;
    }


    public ShoppingCart ( ) {

    }


    public ShoppingCart copy() {
        ShoppingCart newCart = new ShoppingCart(this.customer);
        for (CartItem item :items) {
            CartItem copyItem = new CartItem(item.getProductType(), item.getQuantity());
            newCart.addItem(copyItem);
        }
        return newCart;
    }
    public ArrayList<CartItem> getItems() {
        return items;
    }
    public void addItem(CartItem item) {
        items.add(item);
    }
    public void removeItem(CartItem item){items.remove(item);}


    public Money getTotalCost() {
        String currency;
        if ( !items.isEmpty ( ) ) {
            currency = items.get ( 0 ).getSubtotal_amount ( ).getCurrency ( );
        } else {
            currency = "$";
        }
        totalcost = new Money ( 0, currency );
        for ( CartItem item : items ) {
            totalcost = totalcost.add ( item.getSubtotal_amount ( ) );
        }
        return totalcost;
    }

    public Customer getCustomer ( ) {
        return customer;
    }

    public void setCustomer (Customer customer) {
        this.customer = customer;
    }

    public Order getOrder ( ) {
        return order;
    }

    public void setOrder (Order order) {
        this.order = order;
    }
}
