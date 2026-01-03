package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory; // Χρειάζεται για τη διαγραφή credentials

public class OrdersPreparationEmployeeMenuPresenter {

    private OrdersPreparationEmployeeMenuView view;
    private EmployeeDAO employeeDAO;

    public OrdersPreparationEmployeeMenuPresenter(OrdersPreparationEmployeeMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    public void onClickAssignedOrders(String employeeId){
        view.navigateToAssignedOrders(employeeId);
    }

    public void onClickAvailableOrdersToAssign(String employeeId){
        view.navigateToAvailableOrdersToAssign(employeeId);
    }


    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
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