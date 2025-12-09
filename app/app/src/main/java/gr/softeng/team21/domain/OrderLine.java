package gr.softeng.team21.domain;

import java.math.BigDecimal;

public class OrderLine {

    WholesaleProduct product;
    int quantity;
    SupOrder order;

    OrderLine(WholesaleProduct product , int quantity , SupOrder order){
        this.product = product;
        this.quantity = quantity;
        this.order = order;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public SupOrder getOrder(){
        return order;
    }

    public void setOrder(SupOrder order){
        this.order = order;
    }

    public WholesaleProduct getProduct(){
        return product;
    }

    public void setProduct(WholesaleProduct product){
        this.product = product;
    }

    public BigDecimal totalBill(){

        BigDecimal bill = product.getPrice().getAmount().multiply(BigDecimal.valueOf(quantity));

        return bill;
    }
}

