package gr.softeng.team21.view.admin.deleteEmp;

import java.util.List;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.view.admin.deleteEmp.deleteEmployee.DeleteEmployeeView;

/**
 * A stub implementation of the {@link DeleteEmployeeView} interface used for unit testing.
 * Captures asynchronous data loading behavior and error states to allow assertion validation
 * without requiring an actual Android UI context.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class DeleteEmployeeViewStub implements DeleteEmployeeView {

    private List<Employee> loadedEmployees;
    private String errorMessage;

    @Override
    public void showEmployees(List<Employee> employees) {
        this.loadedEmployees = employees;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    // --- Accessor methods for verification during testing ---

    public List<Employee> getLoadedEmployees() {
        return loadedEmployees;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}