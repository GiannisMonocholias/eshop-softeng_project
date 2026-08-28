package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee responsible for preparing orders.
 * As a pure domain entity, this class is stripped of DAO dependencies and solely manages
 * the employee's preparation statistics and domain attributes.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployee extends Employee {
    private int totalOrdersPreparations;
    private int totalUpdateReserveRequests;

    /**
     * Default constructor for framework instantiation.
     */
    public OrderPreparationEmployee() {
        this.totalOrdersPreparations = 0;
        this.totalUpdateReserveRequests = 0;
    }

    /**
     * Constructs a new OrderPreparationEmployee with the specified details.
     *
     * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business identifier.
     * @param bonus         Performance-based bonus amount.
     * @param salary        Base salary.
     * @param workingHours  Contracted weekly working hours.
     * @param employeeState The current employment status.
     * @param hireDate      The official date of hire.
     */
    public OrderPreparationEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalOrdersPreparations = 0;
        this.totalUpdateReserveRequests = 0;
    }

    public int getTotalOrdersPreparations() {
        return totalOrdersPreparations;
    }

    public int getTotalUpdateReserveRequests() {
        return totalUpdateReserveRequests;
    }

    /**
     * Increments the total orders successfully prepared by this employee.
     */
    public void incrementOrdersPrepared() {
        this.totalOrdersPreparations++;
    }

    /**
     * Increments the count of stock replenishment requests sent by this employee.
     */
    public void incrementUpdateReserveRequests() {
        this.totalUpdateReserveRequests++;
    }
}