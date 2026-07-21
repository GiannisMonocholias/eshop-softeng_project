package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;

/**
 * In-memory implementation of the {@link EmployeeDAO} interface.
 * Provides a centralized repository for managing employee records, returning them
 * wrapped in CompletableFutures to match the asynchronous architectural pattern.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeDAOMemory implements EmployeeDAO {

    private static HashMap<String, Employee> employees;
    private static EmployeeDAOMemory instance;

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes the internal HashMap used to store employee records in memory.
     */
    private EmployeeDAOMemory() {
        employees = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the EmployeeDAOMemory repository.
     * If the instance does not exist, it is created.
     * @return The unique instance of the EmployeeDAOMemory.
     */
    public static EmployeeDAOMemory getInstance() {
        if (instance == null) {
            instance = new EmployeeDAOMemory();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>This in-memory implementation returns a completed future containing the internal map.</p>
     */
    @Override
    public CompletableFuture<HashMap<String, Employee>> getEmployees() {
        return CompletableFuture.completedFuture(employees);
    }

    /**
     * {@inheritDoc}
     * <p>This in-memory implementation iterates through the map to find a matching email string.</p>
     */
    @Override
    public CompletableFuture<Employee> getEmployeeByEmail(String emailAddress) {
        for (String id : employees.keySet()) {
            if (employees.get(id).getEmailAddress().toString().equals(emailAddress)) {
                return CompletableFuture.completedFuture(employees.get(id));
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Employee> getEmployee(String id) {
        return CompletableFuture.completedFuture(employees.get(id));
    }

    /**
     * {@inheritDoc}
     * <p>If the employee already exists or is null, the future completes exceptionally with an {@link IllegalArgumentException}.</p>
     */
    @Override
    public CompletableFuture<Void> addEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (employee != null) {
            if (!employees.containsKey(employee.getEmployeeId())) {
                employees.put(employee.getEmployeeId(), employee);
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("The given employee is already in the repository"));
            }
        } else {
            future.completeExceptionally(new IllegalArgumentException("The Employee argument must not be null"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     * <p>If the employee does not exist or is null, the future completes exceptionally with an {@link IllegalArgumentException}.</p>
     */
    @Override
    public CompletableFuture<Void> removeEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (employee != null) {
            if (employees.containsKey(employee.getEmployeeId())) {
                employees.remove(employee.getEmployeeId());
                future.complete(null);
            } else {
                future.completeExceptionally(new IllegalArgumentException("The employee is not included in the employees' list"));
            }
        } else {
            future.completeExceptionally(new IllegalArgumentException("The employee argument must not be null"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> clear() {
        employees.clear();
        return CompletableFuture.completedFuture(null);
    }
}