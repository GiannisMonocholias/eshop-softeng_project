package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Employee;

public interface EmployeeDAO {

     /**
      * @return a map containing all registered employees, where the key is the employee ID.
      */
     HashMap<String, Employee> getEmployees();

     /**
      * Searches and retrieves an employee based on their email address.
      * @param emailAddress the email address to search for.
      * @return the Employee object if found, otherwise null.
      */
     Employee getEmployeeByEmail(String emailAddress);

     /**
      * Retrieves an employee using their unique ID.
      * @param id the unique business identifier of the employee.
      * @return the Employee object, or null if no such ID exists.
      */
     Employee getEmployee(String id);

     /**
      * Adds a new employee to the repository.
      * @param employee the Employee object to be added.
      * @throws IllegalArgumentException if the employee is null or already exists in the repository.
      */
     void addEmployee(Employee employee);

     /**
      * Removes an employee from the repository.
      * @param employee the Employee object to be removed.
      * @throws IllegalArgumentException if the employee is null or not found in the repository.
      */
     void removeEmployee(Employee employee);

     /**
      * Resets the repository by clearing all employee data.
      */
     void clear();
}
