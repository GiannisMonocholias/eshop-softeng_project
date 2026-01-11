package gr.softeng.team21.domain;

import java.util.ArrayList;

/**
 * Deliverer class extends Employee as deliverer is also
 * working for the company.
 *
 * Max_quantity is the maximum number of orders a deliverer can have.
 *
 * Boolean available is true when a deliverer can take one more order and false when
 * the number of orders that he has to deliver is equal to the max_quantity.
 *
 * ArrayList orders contains Order objects that the deliverer has to deliver.
 */

public class Deliverer extends Employee{

    private int max_quantity;
    private boolean available;
    private ArrayList<Order> orders;


    public Deliverer(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress,
                     String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate, int quan,
                     boolean available){
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.max_quantity = quan;
        this.available = available;
        orders = new ArrayList<>();
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

    /**
     *
     * @param order Order that addOrder has to add.
     *
     * addOrder(...) takes as argument one order object and checks if the deliverer
     * is able to take it over. In that case the order is put in the deliverer's list.
     */

    public void addOrder(Order order){
        if(orders.size() < max_quantity){
            orders.add(order);
        }else{
            setAvailability(false);
            throw new IllegalArgumentException("Η λίστα του διανομέα είναι γεμάτη");
        }

    }

    /**
     *
     * @param order Order that is checked to be set paid.
     * @return true if order is paid or false instead.
     */
    public boolean checkfor(Order order){
        if(orders.contains(order)){
            order.setPaid(true);
            return true;
        }

        return false;
    }

}

