package gr.softeng.team21.view.customer.register;

import java.util.UUID;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the Customer Registration screen.
 * Coordinates the logic for creating new customer instances, generating
 * unique identifiers, and ensuring data is saved in both domain and auth DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterPresenter {

    private RegisterView view;
    private CustomerDAO customerDAO;

    /**
     * Initializes the presenter with the view and customer data access layer.
     * @param view The registration view implementation.
     * @param customerDAO Data source for customer records.
     */
    public RegisterPresenter(RegisterView view, CustomerDAO customerDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
    }

    /**
     * Orchestrates the registration process.
     * Validates required fields, creates a new {@link Customer},
     * generates a {@link UUID}, and persists credentials.
     * @param username  The desired login name.
     * @param firstname The user's given name.
     * @param password The account password.
     * @param lastname The user's family name.
     * @param phone The contact telephone number.
     * @param emailStr  The raw email address string.
     */
    public void register(String username, String firstname, String password,
                         String lastname, String phone, String emailStr) {

        // Basic validation for mandatory fields
        if (username.isEmpty() || password.isEmpty() || emailStr.isEmpty()) {
            view.showErrorMessage("Παρακαλώ συμπληρώστε τα απαραίτητα πεδία.");
            return;
        }

        try {
            EmailAddress emailObj = new EmailAddress(emailStr);
            String randomId = UUID.randomUUID().toString();
            Date currentDate = new Date();

            // Create Domain Model instance
            Customer newCustomer = new Customer(username, firstname, password, lastname,
                    phone, emailObj, randomId, currentDate);

            // Persist in both Customer and Credentials repositories
            customerDAO.addCustomer(newCustomer);
            UserCredentialsDAOMemory.getInstance().addUser(newCustomer);

            view.showSuccessMessage("Επιτυχής εγγραφή! ID: " + randomId);
            view.clearInputFields();

        } catch (Exception e) {
            view.showErrorMessage("Registration error: " + e.getMessage());
        }
    }
}