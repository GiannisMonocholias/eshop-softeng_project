package gr.softeng.team21.domain;

import java.util.ArrayList;

import gr.softeng.team21.util.Money;

/**
 * Represents a shopping cart that contains cart items for a customer calculates the total cost.
 *
 * @author PAVLOS GRATSANIS
 * @version 1.0
 * AM: 3230036
 */
public class ShoppingCart {

    /** The customer that owns this shopping cart */
    private Customer customer;

    /** The total cost of the shopping cart */
    private Money totalcost;

    /** The list of cart items in the shopping cart */
    private ArrayList<CartItem> items = new ArrayList<>();

    /** The order associated with this shopping cart */
    private Order order;

    /**
     * Creates a ShoppingCart for the given customer.
     * The shopping cart is also assigned to the customer.
     *
     * @param customer the customer owning the shopping cart
     */
    public ShoppingCart(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            customer.setShoppingCart(this);
        }
    }


    public ShoppingCart() {
    }

    /**
     Creates and returns a copy of this shopping cart,
     specifically the products are copied to other shopping carts
     *
     * @return a copy of the shopping cart
     */
    public ShoppingCart copy() {
        ShoppingCart newCart = new ShoppingCart(this.customer);
        for (CartItem item : items) {
            CartItem copyItem = new CartItem(item.getProductType(), item.getQuantity());
            newCart.addItem(copyItem);
        }
        return newCart;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    /**
     * Adds a cart item to the shopping cart.
     *
     * @param item the cart item to add
     */
    public void addItem(CartItem item) {
        items.add(item);
    }

    /**
     * Removes a cart item from the shopping cart.
     *
     * @param item the cart item to remove
     */
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    /**
     * Calculates and returns the total cost of the shopping cart.
     *
     * @return the total cost
     */
    public Money getTotalCost() {
        String currency;
        if (!items.isEmpty()) {
            currency = items.get(0).getSubtotal_amount().getCurrency();
        } else {
            currency = "$";
        }
        totalcost = new Money(0, currency);
        for (CartItem item : items) {
            totalcost = totalcost.add(item.getSubtotal_amount());
        }
        return totalcost;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
