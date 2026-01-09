package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the Deliverer Menu.
 * Mediates between the Employee repository and the View, handling profile loading,
 * navigation logic, and the administrative process of account deletion.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuPresenter {
    private DelivererMenuView view;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the presenter with a view implementation and employee data source references.
     * @param view The view implementation
     * @param employeeDAO The data access object for employee records
     */
    public DelivererMenuPresenter(DelivererMenuView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Prepares the view by fetching and displaying employee details.
     * @param employeeId The unique ID of the deliverer.
     */
    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    /**
     * Triggered when the user selects the Orders List option.
     */
    public void onOrdersListSelected(String employeeId) {
        view.navigateToOrdersList(employeeId);
    }

    /**
     * Triggered when the user selects the Account Processing option.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Requests the view to show a deletion confirmation dialog.
     */
    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    /**
     * Permanently removes the Deliverer from the system.
     * Clears credentials from UserCredentialsDAOMemory and record from EmployeeDAO.
     * @param employeeId The ID of the employee to be deleted.
     */
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