package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Employee;

public interface EmployeeDAO {

     HashMap<String, Employee> getEmployees();

     Employee getEmployee(String id);

     void addEmployee(Employee employee);

     void removeEmployee(Employee employee);

     void clear();
}
