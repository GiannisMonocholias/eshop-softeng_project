package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;

/**
 * Presenter for the Order Preparation Menu.
 * Mediates between the domain logic and the menu view, handling asynchronous profile loading,
 * navigation requests, and account deletion transactions.
 * Utilizes Dependency Injection to decouple data sources from presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class OrdersPreparationEmployeeMenuPresenter {

    private OrdersPreparationEmployeeMenuView view;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Initializes the presenter with the required view and injected data access object references.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data source for employee information.
     * @param userCredentialsDAO The data source for user authentication credentials.
     */
    public OrdersPreparationEmployeeMenuPresenter(OrdersPreparationEmployeeMenuView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Asynchronously fetches employee data to display the user's name upon view initialization.
     * @param employeeId The ID of the employee to load.
     */
    public void onViewCreated(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee != null) {
                view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
            }
        }).exceptionally(e -> {
            view.showMessage("Σφάλμα φόρτωσης στοιχείων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Handles selection of the Assigned Orders module.
     * @param employeeId The unique identifier of the employee.
     */
    public void onClickAssignedOrders(String employeeId){
        view.navigateToAssignedOrders(employeeId);
    }

    /**
     * Handles selection of the Account Settings module.
     * @param employeeId The unique identifier of the employee.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Handles selection of the Available Orders pool module.
     * @param employeeId The unique identifier of the employee.
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
     * Executes the permanent, asynchronous removal of the employee account sequentially
     * from both credentials and employee DAOs.
     * @param employeeId The ID of the account to be deleted.
     */
    public void onDeleteAccountConfirmed(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee != null) {
                userCredentialsDAO.removeUser(employee.getUsername()).thenAccept(v1 -> {
                    employeeDAO.removeEmployee(employee).thenAccept(v2 -> {
                        view.showMessage("Ο λογαριασμός διαγράφηκε επιτυχώς.");
                        view.navigateToLogin();
                    }).exceptionally(e -> {
                        view.showMessage("Σφάλμα κατά τη διαγραφή προφίλ: " + e.getMessage());
                        return null;
                    });
                }).exceptionally(e -> {
                    view.showMessage("Σφάλμα κατά τη διαγραφή κωδικών: " + e.getMessage());
                    return null;
                });
            } else {
                view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            view.showMessage("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }
}