package gr.softeng.team21.view.admin.createEmp.employeeRegistration;

import java.util.UUID;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.util.Date;

/**
 * Presenter responsible for validating and processing new employee registrations.
 * Generates the correct Domain Object dynamically based on the selected type
 * and persists it asynchronously using Dependency Injection (DAOs).
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmployeeRegistrationPresenter {

    private EmployeeRegistrationView view;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;
    private String employeeType;

    /**
     * Initializes the presenter with its required View interface and DAOs.
     * @param view The UI interface implementation.
     * @param employeeDAO The Data Access Object for storing employees.
     * @param userCredentialsDAO The Data Access Object for storing authentication credentials.
     */
    public EmployeeRegistrationPresenter(EmployeeRegistrationView view, EmployeeDAO employeeDAO, UserCredentialsDAO userCredentialsDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.userCredentialsDAO = userCredentialsDAO;
    }

    /**
     * Adapts the View's UI dynamically depending on the selected employee role.
     * @param type The employee type constant (e.g., "CUSTOMER_SERVICE", "DELIVERER").
     */
    public void setupUIForType(String type) {
        this.employeeType = type;
        switch (type) {
            case "CUSTOMER_SERVICE":
                view.setHeaderTitle("Εγγραφή Εξυπηρέτησης Πελατών");
                break;
            case "ORDER_PREPARATION":
                view.setHeaderTitle("Εγγραφή Υπαλλήλου Παραγγελιών");
                break;
            case "UPDATE_CATALOGUE":
                view.setHeaderTitle("Εγγραφή Υπαλλήλου Καταλόγου");
                break;
            case "DELIVERER":
                view.setHeaderTitle("Εγγραφή Διανομέα");
                view.showDelivererSpecificFields();
                break;
            default:
                view.setHeaderTitle("Εγγραφή Υπαλλήλου");
        }
    }

    /**
     * Validates all input fields, instantiates the correct subclass of {@link Employee},
     * and triggers a confirmation dialog if all data is valid.
     */
    public void onSubmitClicked() {
        String username = view.getUsername();
        String pass = view.getPassword();
        String name = view.getFirstName();
        String surname = view.getLastName();
        String email = view.getEmail();
        String salaryStr = view.getSalary();
        String hoursStr = view.getWorkingHours();

        if (username.isEmpty() || pass.isEmpty() || name.isEmpty() || surname.isEmpty() || salaryStr.isEmpty()) {
            view.showErrorMessage("Παρακαλώ συμπληρώστε όλα τα βασικά πεδία.");
            return;
        }

        try {
            int salary = Integer.parseInt(salaryStr);
            int hours = Integer.parseInt(hoursStr);
            String empId = "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            EmailAddress emailAddress = new EmailAddress(email);
            Date hireDate = new Date();

            Employee newEmployee = null;

            switch (employeeType) {
                case "CUSTOMER_SERVICE":
                    newEmployee = new CustomerServiceEmployee(username, name, pass, surname, "N/A", emailAddress, empId, 0, salary, hours, EmployeeState.ACTIVE, hireDate);
                    break;
                case "ORDER_PREPARATION":
                    newEmployee = new OrderPreparationEmployee(username, name, pass, surname, "N/A", emailAddress, empId, 0, salary, hours, EmployeeState.ACTIVE, hireDate);
                    break;
                case "UPDATE_CATALOGUE":
                    newEmployee = new UpdateCatalogueEmployee(username, name, pass, surname, "N/A", emailAddress, empId, 0, salary, hours, EmployeeState.ACTIVE, hireDate);
                    break;
                case "DELIVERER":
                    String maxQStr = view.getMaxQuantity();
                    if (maxQStr.isEmpty()) {
                        view.showErrorMessage("Δώστε μέγιστο αριθμό παραγγελιών για τον διανομέα.");
                        return;
                    }
                    int maxQ = Integer.parseInt(maxQStr);
                    newEmployee = new Deliverer(username, name, pass, surname, "N/A", emailAddress, empId, 0, salary, hours, EmployeeState.ACTIVE, hireDate, maxQ, true);
                    break;
            }

            if (newEmployee != null) {
                view.showConfirmDialog(newEmployee);
            }

        } catch (NumberFormatException e) {
            view.showErrorMessage("Ελέγξτε τα αριθμητικά πεδία (Μισθός, Ώρες).");
        } catch (IllegalArgumentException e) {
            view.showErrorMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Executes the final asynchronous save operation to both the Employee and Credentials databases
     * upon receiving positive user confirmation.
     * @param employee The constructed employee object to be persisted.
     */
    public void onRegistrationConfirmed(Employee employee) {
        employeeDAO.addEmployee(employee).thenAccept(v1 -> {
            userCredentialsDAO.addUser(employee).thenAccept(v2 -> {
                view.showSuccessMessage("Ο υπάλληλος προστέθηκε επιτυχώς!");
                view.finishActivity();
            }).exceptionally(e -> {
                view.showErrorMessage("Σφάλμα αποθήκευσης κωδικών: " + e.getMessage());
                return null;
            });
        }).exceptionally(e -> {
            view.showErrorMessage("Σφάλμα αποθήκευσης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }
}