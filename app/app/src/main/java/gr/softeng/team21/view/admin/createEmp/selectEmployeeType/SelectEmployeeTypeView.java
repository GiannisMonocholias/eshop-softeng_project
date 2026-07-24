package gr.softeng.team21.view.admin.createEmp.selectEmployeeType;

/**
 * View contract for the Employee Type Selection screen.
 * Handles updating the UI with the active employee counts for each category
 * and managing navigation.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface SelectEmployeeTypeView {

    /**
     * Updates the UI with the current active count of employees per category.
     * @param customerServiceCount Count of Customer Service employees.
     * @param orderPrepCount Count of Order Preparation employees.
     * @param updateCatCount Count of Update Catalogue employees.
     * @param delivererCount Count of Deliverers.
     */
    void showEmployeeCounts(int customerServiceCount, int orderPrepCount, int updateCatCount, int delivererCount);

    /**
     * Displays an error message via Toast or Dialog.
     * @param message The error description.
     */
    void showErrorMessage(String message);

    /**
     * Navigates to the dynamic registration form passing the chosen type.
     * @param type The role constant (e.g., "DELIVERER").
     */
    void navigateToRegistrationForm(String type);
}