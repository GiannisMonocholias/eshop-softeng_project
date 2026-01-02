package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;

public class CustomerServiceMenuPresenter {
    private CustomerServiceMenuView view;
    private EmployeeDAO employeeDAO;
    public CustomerServiceMenuPresenter(CustomerServiceMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    public String getEmployeeFullname(String EmployeeId){
        Employee employee = employeeDAO.getEmployee(EmployeeId);
        return employee.getFirstname() + " " + employee.getLastname();
    }


    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    public void onInboxSelected(String employeeId) {
        view.navigateToEmailInbox(employeeId);
    }

    public void onOrderStatusSelected(String employeeId) {
        view.navigateToOrderStatus(employeeId);
    }



}
