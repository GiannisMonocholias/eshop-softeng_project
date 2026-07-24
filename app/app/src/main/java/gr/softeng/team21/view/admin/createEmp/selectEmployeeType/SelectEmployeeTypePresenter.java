package gr.softeng.team21.view.admin.createEmp.selectEmployeeType;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the Employee Type Selection screen.
 * Responsible for fetching the current employee counts from the DAO
 * and routing the user's choice to the registration form.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class SelectEmployeeTypePresenter {

    private SelectEmployeeTypeView view;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the presenter with its view and the required DAO.
     * @param view The UI interface implementation.
     * @param employeeDAO The Data Access Object for employees.
     */
    public SelectEmployeeTypePresenter(SelectEmployeeTypeView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously loads all employees from the database, categorizes them,
     * counts them, and updates the View with the results.
     */
    public void loadEmployeeCounts() {
        employeeDAO.getEmployees().thenAccept(employees -> {
            int csCount = 0, prepCount = 0, catCount = 0, delCount = 0;

            for (Employee e : employees.values()) {
                if (e instanceof CustomerServiceEmployee) {
                    csCount++;
                } else if (e instanceof OrderPreparationEmployee) {
                    prepCount++;
                } else if (e instanceof UpdateCatalogueEmployee) {
                    catCount++;
                } else if (e instanceof Deliverer) {
                    delCount++;
                }
            }

            view.showEmployeeCounts(csCount, prepCount, catCount, delCount);
        }).exceptionally(e -> {
            view.showErrorMessage("Αποτυχία φόρτωσης στατιστικών: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when the user clicks on a specific employee category card.
     * @param employeeType The chosen role constant.
     */
    public void onTypeSelected(String employeeType) {
        view.navigateToRegistrationForm(employeeType);
    }
}