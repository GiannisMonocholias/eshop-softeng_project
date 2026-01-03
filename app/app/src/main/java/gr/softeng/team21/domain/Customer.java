package gr.softeng.team21.domain;

import java.util.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;

public class Customer extends User {
    private String customer_id;
    private Date registdate;
    private ShoppingCart shoppingCart;
    private SystemCalendar systemCalendar;



    public Customer (String username, String firstname, String password, String lastname, String phoneNumber,
                     EmailAddress emailaddress, String customer_id, Date registdate) {
        super ( username, firstname, password, lastname, phoneNumber, emailaddress );
        this.customer_id = customer_id;
        this.registdate = registdate;
    }

    public Date getRegistdateDate ( ) {
        return registdate;
    }

    public String getCustomer_id ( ) {
        return customer_id;
    }

    public void setCustomer_id (String customer_id) {
        this.customer_id = customer_id;
    }

    public ShoppingCart getShoppingCart ( ) {
        return shoppingCart;
    }

    public void setShoppingCart (ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public SystemCalendar getSystemCalendar ( ) {
        return systemCalendar;
    }

    public void setSystemCalendar (SystemCalendar systemCalendar) {
        this.systemCalendar = systemCalendar;
    }

    public ProductType findProduct (HashMap<String, ProductType> products, String prodcode) {
        if ( products.isEmpty ( ) ) return null;
        if ( !products.containsKey ( prodcode ) ) return null;
        return products.get ( prodcode );
    }


    public void addItemToCart (ProductType productType, int quantity) {//edo bazo to product pou epistrefei h findProduct
        if ( this.shoppingCart == null ) shoppingCart = new ShoppingCart ( this );
        if ( productType == null ) throw new IllegalArgumentException ( "The product is null." );
        if ( quantity <= 0 ) throw new IllegalArgumentException ( "The quantity must be > 0." );
        int currentQty;
        for ( CartItem item : shoppingCart.getItems ( ) ) {
            if ( item.getProductType ( ).getProductCode ( ).equals ( productType.getProductCode ( ) ) ) {
                currentQty = item.getQuantity ( );
                item.setQuantity ( currentQty + quantity );
                return;
            }
        }
        CartItem newItem = new CartItem ( productType, quantity );
        shoppingCart.addItem ( newItem );
    }

    public void removeItemFromCart (ProductType productType, int quantity) {
        if ( shoppingCart == null )
            throw new IllegalArgumentException ( "Shopping cart is empty" );
        if ( productType == null )
            throw new IllegalArgumentException ( "Product cannot be null" );
        if ( quantity <= 0 )
            throw new IllegalArgumentException ( "Quantity must be >0" );
        CartItem targetItem = null;
        for ( CartItem item : this.shoppingCart.getItems ( ) ) {
            if ( item.getProductType ( ).getProductCode ( ).equals ( productType.getProductCode ( ) ) ) {
                targetItem = item;
                break;
            }
        }
        if ( targetItem == null ) throw new IllegalArgumentException ( "The product isn't in shopping cart!!!" );
        int currentquantity = targetItem.getQuantity ( );
        if ( quantity == currentquantity ) {
            shoppingCart.removeItem ( targetItem );
        } else if ( quantity < currentquantity ) {
            targetItem.setQuantity ( currentquantity - quantity );
        } else {
            throw new IllegalArgumentException ( "There is no available quantity to deduct the amount given" );
        }
    }


    public Order Checkout ( ) {
        if ( shoppingCart == null ) return null;
        Date deliverydate = new Date ( );
        deliverydate.changeDays ( 30 );
        String orderCode = "ORD-" + UUID.randomUUID ( ).toString ( );
        Order neworder = new Order ( orderCode, new Date ( ), OrderStatusType.NEW, false, PaymentType.CASH, deliverydate, shoppingCart );
       neworder.setTotal_amount ( shoppingCart.getTotalCost () );
        return neworder;
    }

    public void selectPaymentType (PaymentType paymentType, String cardNumber, Order order) {
        if ( order == null )
            throw new IllegalArgumentException ( "Order cannot be null!!!" );
        if ( paymentType == null )
            throw new IllegalArgumentException ( "Payment type cannot be null !!!" );

        if ( paymentType == PaymentType.CARD ) {
            if ( !cardNumber.matches ( "\\d{4}-\\d{4}-\\d{4}-\\d{4}" ) )
                throw new IllegalArgumentException ( "Invalid card number format" );
            else {
                order.setPaymentmethod ( PaymentType.CARD );
                order.setPaid ( true );
            }

        }
    }

    public void Confirm (String confirmchoice, Order order) {
        if ( order == null ) throw new IllegalArgumentException ( "Order cannot be null!!!" );
        if ( confirmchoice == null || confirmchoice.equals ( "" ) )
            throw new IllegalArgumentException ( "Confirmchoice cannot be null or empty string!!!" );
        if ( confirmchoice.equals ( "CONFIRM" ) ) {
            OrderDAOMemory.getInstance ( ).addOrder ( order );
            if ( shoppingCart != null ) shoppingCart.setOrder ( order );
            shoppingCart = null;
        }
    }

    public void remove() {
        CustomerDAOMemory repo = CustomerDAOMemory.getInstance();
        String id = this.getCustomer_id();
        if (repo.getCustomers().containsKey(id)) {
            repo.removeCustomer(this);
        } else {
            throw new IllegalStateException("This customer does not exist in the CustomerRepository!!!");
        }
    }
}

