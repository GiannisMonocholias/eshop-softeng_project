package gr.softeng.team21.domain;

import java.util.*;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

public class SupOrder {

    gr.softeng.team21.util.Date orderDate;
    int orderid;
    Admin admin;
    ArrayList<OrderLine> orderProducts;

    public SupOrder(gr.softeng.team21.util.Date date , int id , Admin admin , ArrayList<OrderLine> orproducts){
        this.orderDate = date;
        this.orderid = id;
        this.admin = admin;
        this.orderProducts = orproducts;
    }

    public Admin getAdmin(){
        return admin;
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public int getId(){
        return orderid;
    }

    public void setId(int id){
        this.orderid = id;
    }

    public gr.softeng.team21.util.Date getDate(){
        return orderDate;
    }

    public void setDate(Date date){
        this.orderDate = date;
    }

    public Money fullAmount(){
        Money sum = new Money(0,"euro");
        for(OrderLine line : orderProducts){
            sum.setAmount(sum.getAmount().add(line.totalBill()));
        }
        return sum;
    }

}

