package gr.softeng.team21.domain;

import java.util.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Represents a customer.
 * Extends the User class and provides functionality for shopping cart management (add & remove),
 * order creation,payment selection and confirm or not order.
 *
 * @author PAVLOS GRATSANIS
 * @version 1.0
 * AM: 3230036
 */
public class Customer extends User {
    private String customer_id;
    private Date registdate;
    private ShoppingCart shoppingCart;

    /**
     * Creates a new Customer with the specified details.
     *
     * @param username     the username
     * @param firstname    the first name
     * @param password     the password
     * @param lastname     the last name
     * @param phoneNumber  the phone number
     * @param emailaddress the email address
     * @param customer_id  the unique customer ID
     * @param registdate   the registration date
     */
    public Customer(String username, String firstname, String password, String lastname, String phoneNumber,
                    EmailAddress emailaddress, String customer_id, Date registdate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        this.customer_id = customer_id;
        this.registdate = registdate;
    }

    public Date getRegistdateDate() {
        return registdate;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * Finds a product in the provided map of products using its product code.
     *
     * @param products a HashMap containing available products
     * @param prodcode the code of the product to search for
     * @return the ProductType if found, or null otherwise
     */
    public ProductType findProduct(HashMap<String, ProductType> products, String prodcode) {
        if (products.isEmpty()) return null;
        if (!products.containsKey(prodcode)) return null;
        return products.get(prodcode);
    }

    /**
     * Adds a specific quantity of a product to the customer's shopping cart.
     * If the product already exists in the cart, the quantity is updated.
     *
     * @param productType the product to add
     * @param quantity    the quantity to add (must be > 0)
     * @throws IllegalArgumentException if the product is null or quantity is <= 0
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
     * If the quantity matches the current amount, the item is removed entirely.
     *
     * @param productType the product to remove
     * @param quantity    the quantity to remove
     * @throws IllegalArgumentException if cart is empty, product is null, quantity invalid, or product not found
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
     * Initiates the checkout process by creating a new Order based on the current shopping cart.
     *
     * @return a new Order object, or null if the shopping cart is empty
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
     * Selects the payment method for an order.
     *
     * @param paymentType the type of payment (e.g., CARD, CASH)
     * @param cardNumber  the card number string (if paying by card)
     * @param order       the order to apply payment to
     * @throws IllegalArgumentException if arguments are null or card format is invalid
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
     * Confirms the order, saves it to the repository, and clears the shopping cart.
     *
     * @param confirmchoice the confirmation string
     * @param order         the order to confirm
     * @throws IllegalArgumentException if order is null or choice is invalid
     */
    public void Confirm(String confirmchoice, Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null!!!");
        if (confirmchoice == null || confirmchoice.equals(""))
            throw new IllegalArgumentException("Confirmchoice cannot be null or empty string!!!");
        if (confirmchoice.equals("CONFIRM")) {
            OrderDAOMemory.getInstance().addOrder(order);
            if (shoppingCart != null) shoppingCart.setOrder(order);
            shoppingCart = null;
        }
    }

    /**
     * Removes the customer from the CustomerDao.
     *
     * @throws IllegalStateException if the customer does not exist in the repository
     */
    public void remove() {
        CustomerDAOMemory repo = CustomerDAOMemory.getInstance();
        String id = this.getCustomer_id();
        if (repo.getCustomers().containsKey(id)) {
            repo.removeCustomer(this);
        } else {
            throw new IllegalStateException("This customer does not exist in the CustomerDao!!!");
        }
    }
}