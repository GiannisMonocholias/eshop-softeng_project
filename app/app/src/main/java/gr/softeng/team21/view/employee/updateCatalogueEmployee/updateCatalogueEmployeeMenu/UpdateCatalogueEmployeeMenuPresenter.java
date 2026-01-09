package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the Update Catalogue Employee Menu.
 * Coordinates data retrieval for the employee profile and handles the logic
 * for navigation and account management transactions.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeMenuPresenter {
    private UpdateCatalogueEmployeeMenuView view;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the presenter with the view interface and employee repository.
     * @param view The view implementation (Activity).
     * @param employeeDAO The data access object for employees.
     */
    public UpdateCatalogueEmployeeMenuPresenter(UpdateCatalogueEmployeeMenuView view, EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
        this.view = view;
    }

    /**
     * Prepares the view with the employee's information upon UI creation.
     * @param employeeId The ID of the currently logged-in employee.
     */
    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    /**
     * Triggered when the user selects 'Assigned Requests'.
     */
    public void onClickAssignedRequests(String employeeId){
        view.navigateToAssignedRequests(employeeId);
    }

    /**
     * Triggered when the user selects 'Process Account'.
     */
    public void onProcessAccountSelected(String employeeId) {
        view.navigateToProcessAccount(employeeId);
    }

    /**
     * Triggered when the user selects 'Available Requests'.
     */
    public void onClickAvailableRequestsToAssign(String employeeId) {
        view.navigateToAvailableRequestsToAssign(employeeId);
    }

    /**
     * Triggers a deletion confirmation request to the view.
     */
    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

    /**
     * Finalizes account deletion by removing credentials and employee records.
     * @param employeeId The ID of the employee to be removed.
     */
    public void onDeleteAccountConfirmed(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);

        if (employee != null) {
            UserCredentialsDAOMemory.getInstance().removeUser(employee.getUsername());
            employeeDAO.removeEmployee(employee);

            view.showMessage("Ο λογαριασμός διαγράφηκε επιτυχώς.");
            view.navigateToLogin();
        } else {
            view.showMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
        }
    }
}