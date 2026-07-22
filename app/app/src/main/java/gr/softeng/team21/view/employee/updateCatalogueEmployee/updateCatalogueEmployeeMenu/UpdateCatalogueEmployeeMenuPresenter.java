package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;

/**
 * Presenter for the Update Catalogue Employee Menu.
 * Coordinates asynchronous data retrieval for the employee profile and handles the logic
 * for navigation and account management transactions using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeMenuPresenter {
    private UpdateCatalogueEmployeeMenuView view;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Initializes the presenter with the view interface and injected repositories.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data access object for employees.
     * @param userCredentialsDAO The data access object for user authentication credentials.
     */
    public UpdateCatalogueEmployeeMenuPresenter(UpdateCatalogueEmployeeMenuView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Asynchronously prepares the view with the employee's information upon UI creation.
     * @param employeeId The ID of the currently logged-in employee.
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
     * Triggered when the user selects 'Assigned Requests'.
     * @param employeeId The unique identifier of the employee.
     */
    public void onClickAssignedRequests(String employeeId){
        view.navigateToAssignedRequests(employeeId);
    }

    /**
     * Triggered when the user selects 'Process Account'.
     * @param employeeId The unique identifier of the employee.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Triggered when the user selects 'Available Requests'.
     * @param employeeId The unique identifier of the employee.
     */
    public void onClickAvailableRequestsToAssign(String employeeId) {
        view.navigateToAvailableRequestsToAssign(employeeId);
    }

    /**
     * Triggers a deletion confirmation request to the view.
     */
    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    /**
     * Asynchronously finalizes account deletion by sequentially removing credentials
     * and employee records from the databases.
     * @param employeeId The ID of the employee to be removed.
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