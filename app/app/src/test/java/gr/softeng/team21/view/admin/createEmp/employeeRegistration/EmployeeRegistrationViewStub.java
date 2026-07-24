package gr.softeng.team21.view.admin.createEmp.employeeRegistration;

import gr.softeng.team21.domain.Employee;

/**
 * A stub implementation of the {@link EmployeeRegistrationView} interface for unit testing.
 * Captures dynamic UI changes, form inputs, and verification dialogs.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeRegistrationViewStub implements EmployeeRegistrationView {

    private String headerTitle = "";
    private boolean delivererFieldsVisible = false;
    private String username = "", password = "", firstName = "", lastName = "", email = "";
    private String salary = "", workingHours = "", maxQuantity = "";

    private Employee confirmedEmployee;
    private String errorMessage = "";
    private String successMessage = "";
    private boolean isFinished = false;

    // --- Setters for test simulation ---
    public void setUsername(String v) { this.username = v; }
    public void setPassword(String v) { this.password = v; }
    public void setFirstName(String v) { this.firstName = v; }
    public void setLastName(String v) { this.lastName = v; }
    public void setEmail(String v) { this.email = v; }
    public void setSalary(String v) { this.salary = v; }
    public void setWorkingHours(String v) { this.workingHours = v; }
    public void setMaxQuantity(String v) { this.maxQuantity = v; }

    /** {@inheritDoc} */
    @Override public void setHeaderTitle(String title) { this.headerTitle = title; }

    /** {@inheritDoc} */
    @Override public void showDelivererSpecificFields() { this.delivererFieldsVisible = true; }

    /** {@inheritDoc} */
    @Override public String getUsername() { return username; }

    /** {@inheritDoc} */
    @Override public String getPassword() { return password; }

    /** {@inheritDoc} */
    @Override public String getFirstName() { return firstName; }

    /** {@inheritDoc} */
    @Override public String getLastName() { return lastName; }

    /** {@inheritDoc} */
    @Override public String getEmail() { return email; }

    /** {@inheritDoc} */
    @Override public String getSalary() { return salary; }

    /** {@inheritDoc} */
    @Override public String getWorkingHours() { return workingHours; }

    /** {@inheritDoc} */
    @Override public String getMaxQuantity() { return maxQuantity; }

    /** {@inheritDoc} */
    @Override
    public void showConfirmDialog(Employee employee) { this.confirmedEmployee = employee; }

    /** {@inheritDoc} */
    @Override
    public void showSuccessMessage(String message) { this.successMessage = message; }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) { this.errorMessage = message; }

    /** {@inheritDoc} */
    @Override
    public void finishActivity() { this.isFinished = true; }

    // --- Getters for Assertions ---
    public String getHeaderTitle() { return headerTitle; }
    public boolean isDelivererFieldsVisible() { return delivererFieldsVisible; }
    public Employee getConfirmedEmployee() { return confirmedEmployee; }
    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage; }
    public boolean isFinished() { return isFinished; }
}