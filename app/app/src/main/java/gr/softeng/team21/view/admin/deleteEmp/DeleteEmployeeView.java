package gr.softeng.team21.view.admin.deleteEmp;

import java.util.List;
import gr.softeng.team21.domain.Employee;

/**
 * Interface defining the UI operations for the Delete Employee selection screen.
 * Handles the asynchronous delivery of employee lists and error messages.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface DeleteEmployeeView {

    /**
     * Updates the UI with the fetched list of active employees.
     * @param employees The list of employees fetched from the database.
     */
    void showEmployees(List<Employee> employees);

    /**
     * Displays an error message to the user.
     * @param message The specific error description.
     */
    void showError(String message);
}