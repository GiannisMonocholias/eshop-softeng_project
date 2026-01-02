package gr.softeng.team21.memorydao;

import java.util.HashMap;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Address;
import gr.softeng.team21.domain.Employee;

public class EmployeeDAOMemory implements EmployeeDAO {

    private static HashMap<String , Employee> employees;
    private static EmployeeDAOMemory instance;

    public EmployeeDAOMemory(){
        employees = new HashMap<>();
    }

    public static EmployeeDAOMemory getInstance(){
        if(instance == null){
            instance = new EmployeeDAOMemory();
        }
        return  instance;
    }

    public HashMap<String, Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployeeByEmail(String emailAddress){
        for(String id: employees.keySet()){
            if(employees.get(id).getEmailAddress().toString().equals(emailAddress)){
                return employees.get(id);
            }
        }
        return null;
    }
    public Employee getEmployee(String id){
        return employees.get(id);
    }

    public void addEmployee(Employee employee){
        if(employee != null){
            if(!employees.containsKey(employee.getEmployeeId())){
                employees.put(employee.getEmployeeId() , employee);
            }
            else {
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
