package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.Employee;

/**
 * Interface for Employee Data Access Object.
 * Defines asynchronous CRUD operations for managing Employee entities.
 * @author Γιάννης Μονοχολιάς
 */
public interface EmployeeDAO {

     /**
      * Retrieves all registered employees asynchronously.
      * @return A CompletableFuture containing a map of all employees, where the key is the employee ID.
      */
     CompletableFuture<HashMap<String, Employee>> getEmployees();

     /**
      * Searches and retrieves an employee asynchronously based on their email address.
      * @param emailAddress The email address to search for.
      * @return A CompletableFuture containing the Employee object if found, otherwise null.
      */
     CompletableFuture<Employee> getEmployeeByEmail(String emailAddress);

     /**
      * Retrieves an employee asynchronously using their unique ID.
      * @param id The unique business identifier of the employee.
      * @return A CompletableFuture containing the Employee object, or null if no such ID exists.
      */
     CompletableFuture<Employee> getEmployee(String id);

     /**
      * Adds a new employee to the repository asynchronously.
      * @param employee The Employee object to be added.
      * @return A CompletableFuture representing the completion of the operation.
      */
     CompletableFuture<Void> addEmployee(Employee employee);

     /**
      * Removes an employee from the repository asynchronously.
      * @param employee The Employee object to be removed.
      * @return A CompletableFuture representing the completion of the operation.
      */
     CompletableFuture<Void> removeEmployee(Employee employee);

     /**
      * Resets the repository by clearing all employee data asynchronously.
      * @return A CompletableFuture representing the completion of the operation.
      */
     CompletableFuture<Void> clear();
}