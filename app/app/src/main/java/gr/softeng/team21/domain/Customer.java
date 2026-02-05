package gr.softeng.team21.domain;
import java.util.*;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents a customer of the eshop.
 * Extends the User class.
 * Contains customer-specific details
 * such as registration date, customer ID and a shopping cart.
 * @author PAVLOS GRATSANIS
 */
public class Customer extends User {

    /**
     * The unique ID of the customer
     */
    private String customer_id;

    /**
     * The date the customer registered
     */
    private Date registdate;

    /**
     * The customer's shopping cart
     */
    private ShoppingCart shoppingCart;

    /**
     * Creates a new Customer with the specified details.
     *
     * @param username     the username of the customer
     * @param firstname    the first name of the customer
     * @param password     the password of the customer
     * @param lastname     the last name of the customer
     * @param phoneNumber  the phone number of the customer
     * @param emailaddress the email address of the customer
     * @param customer_id  the unique customer ID
     * @param registdate   the date of registration
     */
    public Customer(String username, String firstname, String password, String lastname, String phoneNumber,
                    EmailAddress emailaddress, String customer_id, Date registdate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        this.customer_id = customer_id;
        this.registdate = registdate;
    }

    /**
     * Returns the registration date of the customer.
     *
     * @return the registration date
     */
    public Date getRegistdateDate() {
        return registdate;
    }

    /**
     * Returns the unique customer ID.
     *
     * @return the customer ID
     */
    public String getCustomer_id() {
        return customer_id;
    }

    /**
     * Returns the shopping cart of the customer.
     *
     * @return the shopping cart
     */
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Sets the shopping cart for the customer.
     *
     * @param shoppingCart the new shopping cart
     */
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * Adds a product to the customer's shopping cart.
     * If the customer does not have a shopping cart, a cart associated with them is created.
     * If the product already exists in the cart, its quantity is increased.
     *
     * @param productType the product to add
     * @param quantity    the quantity to add
     * @throws IllegalArgumentException if the product is null or quantity <= 0
     */
    public void addItemToCart(ProductType productType, int quantity) {
        if (this.shoppingCart == null) shoppingCart = new ShoppingCart(this);
        if (productType == null) throw new IllegalArgumentException("The product is null.");
        if (quantity <= 0) throw new IllegalArgumentException("The quantity must be > 0.");

        int currentQty;
        for (CartItem item : shoppingCart.getItems()) {
            if (item.getProductType().getProductCode().equals(productType.getProductCode())) {
                currentQty = item.getQuantity();
                item.setQuantity(currentQty + quantity);
                return;
            }
        }
        CartItem newItem = new CartItem(productType, quantity);
        shoppingCart.addItem(newItem);
    }

    /**
     * Removes a specific quantity of a product from the shopping cart.
     * If the quantity to remove equals the current quantity, the item is removed completely.
     *
     * @param productType the product to remove
     * @param quantity    the quantity to remove
     * @throws IllegalArgumentException if the cart is empty, inputs are invalid, product not found or insufficient quantity
     */
    public void removeItemFromCart(ProductType productType, int quantity) {
        if (shoppingCart == null)
            throw new IllegalArgumentException("Shopping cart is empty");
        if (productType == null)
            throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be >0");

        CartItem targetItem = null;
        for (CartItem item : this.shoppingCart.getItems()) {
            if (item.getProductType().getProductCode().equals(productType.getProductCode())) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null)
            throw new IllegalArgumentException("The product isn't in shopping cart!!!");

        int currentquantity = targetItem.getQuantity();
        if (quantity == currentquantity) {
            shoppingCart.removeItem(targetItem);
        } else if (quantity < currentquantity) {
            targetItem.setQuantity(currentquantity - quantity);
        } else {
            throw new IllegalArgumentException("There is no available quantity to deduct the amount given");
        }
    }

    /**
     * Creates a new order based on the current shopping cart contents.
     * The order is assigned a new unique code and a delivery date 30 days from now.
     * The order has cash as the default payment method and is considered unpaid.
     *
     * @return the created order, or null if the shopping cart is null
     */
    public Order Checkout() {
        if (shoppingCart == null) return null;

        Date deliverydate = new Date();
        deliverydate.changeDays(30);
        String orderCode = "ORD-" + UUID.randomUUID().toString();
        Order neworder = new Order(orderCode, new Date(), OrderStatusType.NEW, false, PaymentType.CASH, deliverydate, shoppingCart);
        neworder.setTotal_amount(shoppingCart.getTotalCost());
        return neworder;
    }

    /**
     * Selects the payment type for the order and validates card details if applicable.
     * If the card payment method is selected and it is valid, then the order is paid.
     *
     * @param paymentType the selected payment type
     * @param cardNumber  the card number (required if payment type is CARD)
     * @param order       the order to update
     * @throws IllegalArgumentException if order or payment type is null or card format is invalid
     */
    public void selectPaymentType(PaymentType paymentType, String cardNumber, Order order) {
        if (order == null)
            throw new IllegalArgumentException("Order cannot be null!!!");
        if (paymentType == null)
            throw new IllegalArgumentException("Payment type cannot be null !!!");

        if (paymentType == PaymentType.CARD) {
            if (!cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))
                throw new IllegalArgumentException("Invalid card number format");
            else {
                order.setPaymentmethod(PaymentType.CARD);
                order.setPaid(true);
            }
        }
    }

    /**
     * Confirms the order locally for the customer domain logic.
     * If choice is "CONFIRM", it links the current shopping cart to the order and clears the active cart.
     * NOTE: The actual saving to the database/DAO should be handled by the Presenter.
     *
     * @param confirmchoice the confirmation choice (must be "CONFIRM" or "CANCEL")
     * @param order         the order being confirmed
     * @throws IllegalArgumentException if order is null or confirm choice is invalid
     */
    public void Confirm(String confirmchoice, Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null!!!");
        if (confirmchoice == null || confirmchoice.equals(""))
            throw new IllegalArgumentException("Confirmchoice cannot be null or empty string!!!");
        if (confirmchoice.equals("CONFIRM")) {
            this.shoppingCart = null;
        }
    }
}