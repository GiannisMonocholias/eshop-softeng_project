package gr.softeng.team21.view.admin.deleteEmp;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;

/**
 * Presenter handling the asynchronous retrieval of employee details and the execution
 * of the final deletion process. Utilizes Dependency Injection for both required DAOs.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmpInfoPresenter {

    private final EmpInfoView view;
    private final EmployeeDAO employeeDAO;
    private final UserCredentialsDAO userCredentialsDAO;

    /**
     * Constructs the presenter with the necessary view and DAOs.
     * @param view The UI contract interface.
     * @param employeeDAO The injected Data Access Object for employees.
     * @param userCredentialsDAO The injected Data Access Object for user credentials.
     */
    public EmpInfoPresenter(EmpInfoView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Asynchronously retrieves specific employee details using their unique username.
     * @param username The unique identifier of the employee to load.
     */
    public void loadEmployeeDetails(String username) {
        employeeDAO.getEmployees().thenAccept(map -> {
            for (Employee emp : map.values()) {
                if (emp.getUsername().equals(username)) {
                    if (view != null) view.showEmployeeDetails(emp);
                    return;
                }
            }
            if (view != null) view.showError("Ο υπάλληλος δεν βρέθηκε.");
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα: " + e.getMessage());
            return null;
        });
    }

    /**
     * Permanently removes the employee from both the credentials and employee databases.
     * Commands the view to close upon successful execution.
     * @param employee The specific Employee object to delete.
     */
    public void executeDeletion(Employee employee) {
        if (employee == null) return;

        // Execute deletion across all relevant data sources
        userCredentialsDAO.removeUser(employee.getUsername());
        employeeDAO.removeEmployee(employee);

        if (view != null) {
            view.closeScreen();
        }
    }
}