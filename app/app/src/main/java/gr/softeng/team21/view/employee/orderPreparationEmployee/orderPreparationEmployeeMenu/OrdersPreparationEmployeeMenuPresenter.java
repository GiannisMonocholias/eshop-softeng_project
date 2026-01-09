package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the Order Preparation Menu.
 * Mediates between the domain logic and the menu view, handling profile loading,
 * navigation requests, and account deletion transactions.
 * @author Γιάννης Μονοχολιάς
 */
public class OrdersPreparationEmployeeMenuPresenter {

    private OrdersPreparationEmployeeMenuView view;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the presenter with the required view and data access object references.
     * @param view The view implementation (Activity).
     * @param employeeDAO The data source for employee information.
     */
    public OrdersPreparationEmployeeMenuPresenter(OrdersPreparationEmployeeMenuView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Fetches employee data to display the user's name upon view initialization.
     * @param employeeId The ID of the employee to load.
     */
    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    /**
     * Handles selection of the Assigned Orders module.
     */
    public void onClickAssignedOrders(String employeeId){
        view.navigateToAssignedOrders(employeeId);
    }

    /**
     * Handles selection of the Account Settings module.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Handles selection of the Available Orders pool module.
     */
    public void onClickAvailableOrdersToAssign(String employeeId){
        view.navigateToAvailableOrdersToAssign(employeeId);
    }

    /**
     * Triggers the confirmation dialog for account deletion via the view.
     */
    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    /**
     * Executes the permanent removal of the employee account from both
     * credentials and DAO memory.
     * @param employeeId The ID of the account to be deleted.
     */
    public void onDeleteAccountConfirmed(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);

        if (employee != null) {
            // Remove from authentication system
            UserCredentialsDAOMemory.getInstance().removeUser(employee.getUsername());
            // Remove from employee records
            employeeDAO.removeEmployee(employee);

            view.showMessage("Ο λογαριασμός διαγράφηκε επιτυχώς.");
            view.navigateToLogin();
        } else {
            view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
        }
    }
}