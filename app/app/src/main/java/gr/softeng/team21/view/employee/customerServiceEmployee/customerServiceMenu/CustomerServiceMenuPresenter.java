package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class CustomerServiceMenuPresenter {
    CustomerServiceMenuView view;
    EmployeeDAO employeeDAO;
    public CustomerServiceMenuPresenter(CustomerServiceMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    public String getEmployeeFullname(String EmployeeId){
        Employee employee = employeeDAO.getEmployee(EmployeeId);
        return employee.getFirstname() + " " + employee.getLastname();
    }

}
