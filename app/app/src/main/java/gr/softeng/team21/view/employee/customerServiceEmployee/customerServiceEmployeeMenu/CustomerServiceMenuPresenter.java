package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the Customer Service Menu.
 * Coordinates user actions, retrieves employee profile data from the DAO,
 * and executes the logic for account management and navigation.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceMenuPresenter {
    private CustomerServiceMenuView view;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the Presenter with the corresponding View and Employee DAO references.
     * @param view The View implementation (Activity).
     * @param employeeDAO The data source for employee records.
     */
    public CustomerServiceMenuPresenter(CustomerServiceMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Triggered upon view creation to prepare the screen data.
     * Fetches employee full name and requests the view to display it.
     * @param employeeId The ID of the currently logged-in employee.
     */
    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    /**
     * Handles the logic for selecting the Email Inbox navigation.
     */
    public void onInboxSelected(String employeeId) {
        view.navigateToEmailInbox(employeeId);
    }

    /**
     * Handles the logic for selecting the Order Status notification navigation.
     */
    public void onOrderStatusSelected(String employeeId) {
        view.navigateToOrderStatus(employeeId);
    }

    /**
     * Triggers the confirmation dialog for account deletion via the view.
     */
    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    /**
     * Handles the logic for selecting the Account Processing (Edit) navigation.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Permanently removes the employee's account from the system,
     * deleting records from both the Credentials and Employee repositories.
     * @param employeeId The ID of the account to be deleted.
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