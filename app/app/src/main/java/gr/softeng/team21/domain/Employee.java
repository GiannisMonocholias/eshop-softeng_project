package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * Represents a generic employee within the organization.
 * This class serves as a base for specific employee types and contains
 * core professional information such as salary, id, and employment state.
 * @author Γιάννης Μονοχολιάς
 */
public class Employee extends User {
    private String employeeId;
    private Date hireDate;
    private EmployeeState EmployeeState;
    private int WorkingHours;
    private int salary;
    private int bonus;

    /**
     * Constructs a new Employee with the provided personal and professional details.
     * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business identifier.
     * @param bonus         The performance-based bonus.
     * @param salary        The base salary amount.
     * @param workingHours  The weekly contracted hours.
     * @param employeeState The current status of the employee.
     * @param hireDate      The date the employee was hired.
     */
    public Employee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        setEmployeeId(employeeId);
        setBonus(bonus);
        setSalary(salary);
        setWorkingHours(workingHours);
        setEmployeeState(employeeState);
        setHireDate(hireDate);
    }

    /**
     * @return the unique business identifier for the employee.
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employee ID to set.
     */
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    /**
     * @return the current bonus amount.
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * @param bonus the bonus amount to set.
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * @return the base salary amount.
     */
    public int getSalary() {
        return salary;
    }

    /**
     * @param salary the salary amount to set.
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * @return the current state/status of the employee.
     */
    public EmployeeState getEmployeeState() {
        return EmployeeState;
    }

    /**
     * @param employeeState the state to set for the employee.
     */
    public void setEmployeeState(EmployeeState employeeState) { this.EmployeeState = employeeState; }

    /**
     * @return the weekly working hours.
     */
    public int getWorkingHours() {
        return WorkingHours;
    }

    /**
     * @param workingHours the number of hours to set.
     */
    public void setWorkingHours(int workingHours) {
        this.WorkingHours = workingHours;
    }

    /**
     * @return the official date of hire.
     */
    public Date getHireDate() {
        return hireDate;
    }

    /**
     * Sets the hire date of the employee.
     * This field can only be set once (if it is null).
     * @param hireDate the hire date to set.
     */
    public void setHireDate(Date hireDate) {
        if (this.hireDate == null)
            this.hireDate = hireDate;
    }
}