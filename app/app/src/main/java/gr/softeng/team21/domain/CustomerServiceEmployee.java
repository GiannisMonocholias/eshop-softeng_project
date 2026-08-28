package gr.softeng.team21.domain;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee specialized in customer service.
 * Responsibilities include handling assigned orders and tracking the total
 * number of responses sent. As a pure domain entity, it does not handle data
 * persistence or direct email dispatching.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployee extends Employee {
    private int totalResponses;
    private ArrayList<Order> orders;

    /**
     * Default constructor for framework instantiation.
     */
    public CustomerServiceEmployee() {
        this.orders = new ArrayList<>();
        this.totalResponses = 0;
    }

    /**
     * Constructs a new CustomerServiceEmployee with full details.
     *
     * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business ID.
     * @param bonus         Performance-based bonus amount.
     * @param salary        Base salary.
     * @param workingHours  Contracted weekly working hours.
     * @param employeeState The current state (e.g., ACTIVE, ON_LEAVE).
     * @param hireDate      The official employment date.
     */
    public CustomerServiceEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalResponses = 0;
        this.orders = new ArrayList<>();
    }

    /**
     * @return the total number of customer responses processed by this employee.
     */
    public int getTotalResponses() {
        return totalResponses;
    }

    /**
     * Increments the total responses counter by one.
     * Called by an external Presenter after successfully dispatching a reply.
     */
    public void incrementTotalResponses() {
        this.totalResponses++;
    }

    /**
     * @return the list of orders managed by this employee.
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * Assigns a new order to this employee.
     * @param order the order to add.
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Removes an order from this employee's responsibility.
     * @param order the order to remove.
     */
    public void removeOrder(Order order) {
        orders.remove(order);
    }

    /**
     * Replaces the current list of orders with a new set.
     * @param orders the new list of orders.
     */
    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}