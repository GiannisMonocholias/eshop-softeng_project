package gr.softeng.team21.view.admin.deleteEmp.empInfo;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.view.admin.deleteEmp.empInfo.EmpInfoView;

/**
 * A stub implementation of the {@link EmpInfoView} interface for unit testing.
 * Records the employee details passed to the view, error messages, and whether
 * the view was instructed to close after a successful deletion.
 *
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmpInfoViewStub implements EmpInfoView {

    private Employee displayedEmployee;
    private String errorMessage;
    private boolean isScreenClosed = false;

    @Override
    public void showEmployeeDetails(Employee employee) {
        this.displayedEmployee = employee;
    }

    @Override
    public void closeScreen() {
        this.isScreenClosed = true;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    // --- Accessor methods for verification during testing ---

    public Employee getDisplayedEmployee() {
        return displayedEmployee;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isScreenClosed() {
        return isScreenClosed;
    }
}