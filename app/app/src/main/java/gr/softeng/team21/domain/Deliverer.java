package gr.softeng.team21.domain;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
<<<<<<< Updated upstream
 * Represents a delivery employee (Deliverer) in the domain model.
 * This class manages the deliverer's order capacity and calculates availability
 * based on the current workload.
 * @author Γιάννης Μονοχολιάς
 */
public class Deliverer extends Employee {
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
    private int max_quantity;
    private boolean available;
    private ArrayList<Order> orders;


    /**
     * Default constructor
     * */
    public Deliverer() {
    }

    /**
     * Constructs a new Deliverer with personal, professional, and delivery-specific details.
     * * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business identifier for the employee.
     * @param bonus         Performance-based bonus amount.
     * @param salary        The employee's base salary.
     * @param workingHours  The contracted weekly working hours.
     * @param employeeState The current employment status (e.g., ACTIVE).
     * @param hireDate      The official date of hire.
     * @param quan          The maximum number of orders the deliverer can handle.
     * @param available     The initial availability status (legacy support).
     */
    public Deliverer(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress,
                     String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate, int quan,
                     boolean available) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.max_quantity = quan;
        this.available = available;
        orders = new ArrayList<>();
    }

    /**
     * @return the maximum number of orders this deliverer is allowed to carry.
     */
    public int getQuantity() {
        return max_quantity;
    }

    /**
     * Updates the maximum capacity of the deliverer.
     * @param quan the new capacity limit.
     */
    public void setQuantity(int quan) {
        this.max_quantity = quan;
    }

    /**
     * Calculates the deliverer's availability dynamically.
     * @return true if the current number of orders is less than the maximum capacity.
     */
    public boolean getAvailability() {
        return orders.size() < max_quantity;
    }

    /**
     * @return the availability state of the deliverer
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Updatesthe availability state of the deliverer
     * @param available the new availability state of the deliverer.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }


    /**
     * @return the list of orders currently assigned to this deliverer.
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * Assigns a new order to the deliverer if they have not reached their maximum capacity.
     * Validates capacity using the dynamic availability check.
     * @param order the order to be assigned.
     * @throws IllegalArgumentException if the deliverer's capacity is full.
     */
    public void addOrder(Order order){
        if(orders.size() < max_quantity){
            orders.add(order);
        } else {
            throw new IllegalArgumentException("Η λίστα του διανομέα είναι γεμάτη");
        }
    }

    /**
     * Verifies if a specific order is in the deliverer's list and marks it as paid.
     * @param order the order to be checked and updated.
     * @return true if the order exists in the list and was updated, false otherwise.
     */
    public boolean checkfor(Order order){
        if(orders.contains(order)){
            order.setPaid(true);
            return true;
        }
        return false;
    }
}