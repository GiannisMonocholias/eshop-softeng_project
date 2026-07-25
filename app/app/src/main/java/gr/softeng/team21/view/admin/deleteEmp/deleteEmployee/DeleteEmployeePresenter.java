package gr.softeng.team21.view.admin.deleteEmp.deleteEmployee;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;

/**
 * Presenter responsible for asynchronously fetching active employees from the database.
 * Utilizes Dependency Injection for the EmployeeDAO to support both Memory and Firebase implementations.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class DeleteEmployeePresenter {

    private final DeleteEmployeeView view;
    private final EmployeeDAO employeeDAO;

    /**
     * Constructs the presenter with the provided view and DAO.
     * @param view The UI contract interface.
     * @param employeeDAO The injected Data Access Object for employees.
     */
    public DeleteEmployeePresenter(DeleteEmployeeView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Fetches all registered employees asynchronously and commands the view to render them.
     * Handles potential exceptions during the fetch process.
     */
    public void loadEmployees() {
        employeeDAO.getEmployees().thenAccept(map -> {
            List<Employee> employeeList = new ArrayList<>(map.values());
            if (view != null) {
                view.showEmployees(employeeList);
            }
        }).exceptionally(e -> {
            if (view != null) {
                view.showError("Σφάλμα φόρτωσης: " + e.getMessage());
            }
            return null;
        });
    }
}