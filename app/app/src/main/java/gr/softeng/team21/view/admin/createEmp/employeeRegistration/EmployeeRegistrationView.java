package gr.softeng.team21.view.admin.createEmp.employeeRegistration;

import gr.softeng.team21.domain.Employee;

/**
 * View contract for the dynamic Employee Registration Form.
 * Handles UI setup, retrieving inputs, and displaying asynchronous feedback and dialogs.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface EmployeeRegistrationView {

    /**
     * Dynamically updates the title of the registration form.
     * @param title The new header title based on the employee type.
     */
    void setHeaderTitle(String title);

    /**
     * Reveals the maximum quantity field, specifically required when registering a Deliverer.
     */
    void showDelivererSpecificFields();

    /** @return the entered username. */
    String getUsername();

    /** @return the entered password. */
    String getPassword();

    /** @return the entered first name. */
    String getFirstName();

    /** @return the entered last name. */
    String getLastName();

    /** @return the entered email address. */
    String getEmail();

    /** @return the entered salary as a string. */
    String getSalary();

    /** @return the entered weekly working hours as a string. */
    String getWorkingHours();

    /** @return the entered max quantity (only used if employee is a Deliverer). */
    String getMaxQuantity();

    /**
     * Displays a Material Alert confirmation dialog to confirm the registration.
     * @param employee The fully constructed employee domain object ready to be saved.
     */
    void showConfirmDialog(Employee employee);

    /**
     * Displays a transient success notification.
     * @param message The success message content.
     */
    void showSuccessMessage(String message);

    /**
     * Displays an error alert dialog or toast.
     * @param message The error message content.
     */
    void showErrorMessage(String message);

    /**
     * Safely closes the registration screen and returns to the previous menu.
     */
    void finishActivity();
}