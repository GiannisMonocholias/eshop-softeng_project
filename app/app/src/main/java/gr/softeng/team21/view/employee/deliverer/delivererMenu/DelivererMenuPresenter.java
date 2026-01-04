package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class DelivererMenuPresenter {
    private DelivererMenuView view;
    private EmployeeDAO employeeDAO;

    public DelivererMenuPresenter(DelivererMenuView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    public void onOrdersListSelected(String employeeId) {
        view.navigateToOrdersList(employeeId);
    }


    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
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