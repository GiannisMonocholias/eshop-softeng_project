package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee responsible for managing and updating the product catalogue.
 * As a pure domain entity, this class is decoupled from DAOs and solely tracks
 * the employee's performance statistics.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployee extends Employee {
    private int totalCatalogueUpdates;

    /**
     * Default constructor for framework instantiation.
     */
    public UpdateCatalogueEmployee() {
        this.totalCatalogueUpdates = 0;
    }

    /**
     * Constructs a new UpdateCatalogueEmployee with the specified details.
     */
    public UpdateCatalogueEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalCatalogueUpdates = 0;
    }

    /**
     * @return the total number of catalogue updates executed by this employee.
     */
    public int getTotalCatalogueUpdates() {
        return totalCatalogueUpdates;
    }

    /**
     * Increments the total number of successful catalogue updates.
     */
    public void incrementTotalCatalogueUpdates() {
        this.totalCatalogueUpdates++;
    }
}