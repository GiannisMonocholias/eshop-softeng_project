package gr.softeng.team21.domain;

import gr.softeng.team21.util.Money;

public class CartItem {
    private int quantity;
    private static  int counter;
    private Money subtotal_amount;
    private int id;
    private ProductType productType;


    public CartItem (ProductType productType, int quantity) {
        this.productType = productType;
        this.quantity = quantity;
        this.id = ++counter;
        calculateSubtotal ( );
    }
    public CartItem (ProductType productType) {
        this.productType = productType;
    }

    public int getQuantity ( ) {
        return quantity;
    }

    public void setQuantity (int quantity) {
        this.quantity = quantity;
        calculateSubtotal ( );
    }

    public void calculateSubtotal ( ) {
        this.subtotal_amount = productType.getPrice ( ).multiply ( quantity );
    }


    public Money getSubtotal_amount ( ) {
        return subtotal_amount;
    }

    public int getId ( ) {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public ProductType getProductType ( ) {
        return productType;
    }

    @Override
    public String toString() {
        String productName=productType.getProductname();
        String totalStr=subtotal_amount.toString();
        return productName + " (x" + quantity + ") -- " + totalStr;
    }

}
