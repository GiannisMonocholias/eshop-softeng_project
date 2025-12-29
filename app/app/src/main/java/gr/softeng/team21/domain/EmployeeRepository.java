package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeRepository {

    private HashMap<String , Employee> employees;
    private static  EmployeeRepository instance;

    public EmployeeRepository(){
        employees = new HashMap<>();
    }

    public static EmployeeRepository getInstance(){
        if(instance == null){
            instance = new EmployeeRepository();
        }
        return  instance;
    }

    public HashMap<String, Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployee(String id){
        return employees.get(id);
    }

    public void addEmployee(Employee employee){
        if(employee != null){
            if(!employees.containsKey(employee.getEmployeeId())){
                employees.put(employee.getEmployeeId() , employee);
            }else {
                throw new IllegalArgumentException("The given employee is already in the repository");
            }
        }else{
            throw new IllegalArgumentException("The Employee argument must not be null");
        }
    }

    public void removeEmployee(Employee employee){
        if(employee != null){
            if(employees.containsKey(employee.getEmployeeId())){
                employees.remove(employee.getEmployeeId() , employee);
            }else{
                throw new IllegalArgumentException("The employee is not included ine the employees' list");
            }
        }else{
            throw new IllegalArgumentException("The employee argument must not be null");
        }
    }

    public void clear(){
        employees.clear();
    }
}
