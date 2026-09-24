package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.EmployeeRole;

/**
 * Presenter for the Deliverer Menu.
 * Mediates between the Employee repositories and the View, handling profile loading,
 * navigation logic, and the administrative process of account deletion asynchronously.
 * Utilizes Dependency Injection to decouple data sources from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuPresenter {
    private DelivererMenuView view;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Initializes the presenter with a view implementation and injected data source references.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data access object for employee records.
     * @param userCredentialsDAO The data access object for user credentials.
     */
    public DelivererMenuPresenter(DelivererMenuView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Prepares the view by fetching and displaying employee details asynchronously.
     * Explicitly requests the DELIVERY role to ensure subclass instantiation.
     * @param employeeId The unique ID of the deliverer.
     */
    public void onViewCreated(String employeeId) {
        // Explicitly request the specific role
        employeeDAO.getEmployee(employeeId, EmployeeRole.DELIVERY).thenAccept(employee -> {

            // Validate the subclass and cast
            if (employee instanceof Deliverer) {
                Deliverer deliverer = (Deliverer) employee;
                if (view != null) {
                    view.showEmployeeName(deliverer.getFirstname() + " " + deliverer.getLastname());
                }
            } else {
                if (view != null) view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }

        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα φόρτωσης στοιχείων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when the user selects the Orders List option.
     * @param employeeId The unique identifier of the deliverer.
     */
    public void onOrdersListSelected(String employeeId) {
        view.navigateToOrdersList(employeeId);
    }

    /**
     * Triggered when the user selects the Account Processing option.
     * @param employeeId The unique identifier of the deliverer.
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
     * Permanently removes the Deliverer from the system asynchronously.
     * Explicitly verifies the deliverer role before sequentially clearing credentials and records.
     * @param employeeId The ID of the employee to be deleted.
     */
    public void onDeleteAccountConfirmed(String employeeId) {
        // Explicitly request the specific role
        employeeDAO.getEmployee(employeeId, EmployeeRole.DELIVERY).thenAccept(employee -> {

            // Validate the subclass and cast
            if (employee instanceof Deliverer) {
                Deliverer deliverer = (Deliverer) employee;

                userCredentialsDAO.removeUser(deliverer.getUsername()).thenAccept(v1 -> {
                    employeeDAO.removeEmployee(deliverer).thenAccept(v2 -> {
                        if (view != null) {
                            view.showMessage("Ο λογαριασμός διαγράφηκε επιτυχώς.");
                            view.navigateToLogin();
                        }
                    }).exceptionally(e -> {
                        if (view != null) view.showMessage("Σφάλμα κατά τη διαγραφή προφίλ: " + e.getMessage());
                        return null;
                    });
                }).exceptionally(e -> {
                    if (view != null) view.showMessage("Σφάλμα κατά τη διαγραφή κωδικών: " + e.getMessage());
                    return null;
                });

            } else {
                if (view != null) view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }

        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }
}