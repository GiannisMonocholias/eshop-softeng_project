package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents a delivery employee (Deliverer) in the domain model.
 * This class manages the deliverer's order capacity and calculates availability
 * based on the current workload count, without storing full Order objects.
 * @author Γιάννης Μονοχολιάς
 */
public class Deliverer extends Employee {

    private int max_quantity;
    private boolean available;
    private int assignedOrdersCount;

    /**
     * Default constructor for framework instantiation.
     */
    public Deliverer() {
        this.assignedOrdersCount = 0;
    }

    /**
     * Constructs a new Deliverer with personal, professional, and delivery-specific details.
     *
     * @param username      The unique account username.
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
     * @param available     The initial availability status.
     */
    public Deliverer(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress,
                     String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate, int quan,
                     boolean available) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.max_quantity = quan;
        this.available = available;
        this.assignedOrdersCount = 0;
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
     * @return true if the current number of assigned orders is less than the maximum capacity.
     */
    public boolean getAvailability() {
        return assignedOrdersCount < max_quantity;
    }

    /**
     * @return the explicit availability state of the deliverer.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Updates the availability state of the deliverer.
     * @param available the new availability state of the deliverer.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the current count of active orders assigned to this deliverer.
     */
    public int getAssignedOrdersCount() {
        return assignedOrdersCount;
    }

    /**
     * Increments the deliverer's workload.
     * @throws IllegalArgumentException if the deliverer's capacity is full.
     */
    public void assignOrder() {
        if (assignedOrdersCount < max_quantity) {
            assignedOrdersCount++;
        } else {
            throw new IllegalArgumentException("Η λίστα του διανομέα είναι γεμάτη");
        }
    }

    /**
     * Decrements the deliverer's workload upon successful delivery.
     */
    public void completeOrder() {
        if (assignedOrdersCount > 0) {
            assignedOrdersCount--;
        }
    }
}