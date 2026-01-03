package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class UpdateCatalogueEmployeeMenuPresenter {
    private UpdateCatalogueEmployeeMenuView view;
    private EmployeeDAO employeeDAO;

    public UpdateCatalogueEmployeeMenuPresenter(UpdateCatalogueEmployeeMenuView view, EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
        this.view = view;
    }

    public void onViewCreated(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee != null) {
            view.showEmployeeName(employee.getFirstname() + " " + employee.getLastname());
        }
    }

    public void onClickAssignedRequests(String employeeId){
        view.navigateToAssignedRequests(employeeId);
    }

    public void onClickAvailableRequestsToAssign(String employeeId) {
        view.navigateToAvailableRequestsToAssign(employeeId);
    }


    public void onDeleteAccountSelected() {
        view.showDeleteAccountConfirmation();
    }

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