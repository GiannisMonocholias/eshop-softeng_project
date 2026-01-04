package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class CustomerServiceMenuPresenter {
    private CustomerServiceMenuView view;
    private EmployeeDAO employeeDAO;

    public CustomerServiceMenuPresenter(CustomerServiceMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
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

    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    public void onDeleteAccountConfirmed(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);

        if (employee != null) {
            UserCredentialsDAOMemory.getInstance().removeUser(employee.getUsername());
            employeeDAO.removeEmployee(employee);

            view.showMessage("Ο λογαριασμός διαγράφηκε επιτυχώς.");
            view.navigateToLogin();
        } else {
            view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
        }
    }
}