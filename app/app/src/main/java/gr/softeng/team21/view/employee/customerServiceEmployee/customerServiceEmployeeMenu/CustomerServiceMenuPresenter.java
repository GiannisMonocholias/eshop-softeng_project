package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;

/**
 * Presenter for the Customer Service Menu.
 * Coordinates user actions, retrieves employee profile data asynchronously from the DAO,
 * and executes the logic for account management and navigation.
 * Utilizes Dependency Injection to decouple data sources from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceMenuPresenter {
    private CustomerServiceMenuView view;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Initializes the Presenter with the corresponding View and injected DAO references.
     * @param view The View implementation (Activity or Stub).
     * @param employeeDAO The data source for employee records.
     * @param userCredentialsDAO The data source for user authentication credentials.
     */
    public CustomerServiceMenuPresenter(CustomerServiceMenuView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Triggered upon view creation to prepare the screen data.
     * Fetches employee full name asynchronously and requests the view to display it.
     * @param employeeId The ID of the currently logged-in employee.
     */
    public void onViewCreated(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee != null) {
                view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
            }
        }).exceptionally(e -> {
            view.showMessage("Error loading employee data: " + e.getMessage());
            return null;
        });
    }

    /**
     * Handles the logic for selecting the Email Inbox navigation.
     * @param employeeId The unique identifier of the employee.
     */
    public void onInboxSelected(String employeeId) {
        view.navigateToEmailInbox(employeeId);
    }

    /**
     * Handles the logic for selecting the Order Status notification navigation.
     * @param employeeId The unique identifier of the employee.
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
     * @param employeeId The unique identifier of the employee.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Permanently removes the employee's account from the system asynchronously.
     * Sequentially deletes records from both the Credentials and Employee repositories.
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
                        view.showMessage("Σφάλμα διαγραφής προφίλ: " + e.getMessage());
                        return null;
                    });
                }).exceptionally(e -> {
                    view.showMessage("Σφάλμα διαγραφής κωδικών: " + e.getMessage());
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