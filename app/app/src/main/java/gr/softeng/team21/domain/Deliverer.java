package gr.softeng.team21.domain;

import java.util.ArrayList;

public class Deliverer extends Employee{

    private int max_quantity;
    private boolean available;
    private ArrayList<Order> orders;


    Deliverer(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress,
              String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate,int quan ,
              boolean available , ArrayList<Order> orders){
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.max_quantity = quan;
        this.available = available;
        this.orders = orders;
    }

    public int getQuantity(){
        return max_quantity;
    }

    public void setQuantity(int quan){
        this.max_quantity = quan;
    }

    public boolean getAvailability(){
        return available;
    }

    public void setAvailability(boolean available){
        this.available = available;
    }

    public ArrayList<Order> getOrders(){
        return new ArrayList<>(orders);
    }

    public void addOrder(Order order){
        if(orders.size() < max_quantity){
            orders.add(order);
        }else{
            setAvailability(false);
            throw new IllegalArgumentException("Η λίστα του διανομέα είναι γεμάτη");
        }

    }

    public boolean checkfor(Order order){
        if(orders.contains(order)){
            order.setPaid(true);
            return true;
        }

        return false;
    }

}

