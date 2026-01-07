package gr.softeng.team21.memorydao;

import java.util.HashMap;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;

/**
 * In-memory implementation of the {@link EmployeeDAO} interface.
 * Provides a centralized repository for managing employee records, allowing
 * retrieval by ID or email address.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeDAOMemory implements EmployeeDAO {

    private static HashMap<String, Employee> employees;
    private static EmployeeDAOMemory instance;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the map used to store employee records.
     */
    public EmployeeDAOMemory() {
        employees = new HashMap<>();
    }

    /**
     * Returns the singleton instance of EmployeeDAOMemory.
     * @return The unique instance of this DAO.
     */
    public static EmployeeDAOMemory getInstance() {
        if (instance == null) {
            instance = new EmployeeDAOMemory();
        }
        return instance;
    }

    /**
     * @return a map containing all registered employees, where the key is the employee ID.
     */
    public HashMap<String, Employee> getEmployees() {
        return employees;
    }

    /**
     * Searches and retrieves an employee based on their email address.
     * @param emailAddress the email address to search for.
     * @return the Employee object if found, otherwise null.
     */
    public Employee getEmployeeByEmail(String emailAddress) {
        for (String id : employees.keySet()) {
            if (employees.get(id).getEmailAddress().toString().equals(emailAddress)) {
                return employees.get(id);
            }
        }
        return null;
    }

    /**
     * Retrieves an employee using their unique ID.
     * @param id the unique business identifier of the employee.
     * @return the Employee object, or null if no such ID exists.
     */
    public Employee getEmployee(String id) {
        return employees.get(id);
    }

    /**
     * Adds a new employee to the repository.
     * @param employee the Employee object to be added.
     * @throws IllegalArgumentException if the employee is null or already exists in the repository.
     */
    public void addEmployee(Employee employee) {
        if (employee != null) {
            if (!employees.containsKey(employee.getEmployeeId())) {
                employees.put(employee.getEmployeeId(), employee);
            } else {
                throw new IllegalArgumentException("The given employee is already in the repository");
            }
        } else {
            throw new IllegalArgumentException("The Employee argument must not be null");
        }
    }

    /**
     * Removes an employee from the repository.
     * @param employee the Employee object to be removed.
     * @throws IllegalArgumentException if the employee is null or not found in the repository.
     */
    public void removeEmployee(Employee employee) {
        if (employee != null) {
            if (employees.containsKey(employee.getEmployeeId())) {
                employees.remove(employee.getEmployeeId());
            } else {
                throw new IllegalArgumentException("The employee is not included ine the employees' list");
            }
        } else {
            throw new IllegalArgumentException("The employee argument must not be null");
        }
    }

    /**
     * Resets the repository by clearing all employee data.
     */
    public void clear() {
        employees.clear();
    }
}