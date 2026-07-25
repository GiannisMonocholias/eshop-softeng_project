package gr.softeng.team21.view.admin.deleteEmp;

import gr.softeng.team21.domain.Employee;

/**
 * Interface defining the UI operations for the Employee Info and Deletion Confirmation screen.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface EmpInfoView {

    /**
     * Updates the UI with the specific details of the selected employee.
     * @param employee The Employee object containing the details to display.
     */
    void showEmployeeDetails(Employee employee);

    /**
     * Closes the current screen and returns to the previous menu.
     */
    void closeScreen();

    /**
     * Displays an error message to the user.
     * @param message The specific error description.
     */
    void showError(String message);
}