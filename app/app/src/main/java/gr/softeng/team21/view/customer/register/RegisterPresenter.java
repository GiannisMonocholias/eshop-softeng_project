package gr.softeng.team21.view.customer.register;

import java.util.UUID;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;

/**
 * Presenter for the Customer Registration screen.
 * Coordinates the logic for creating new customer instances, generating
 * unique identifiers, and ensuring data is saved asynchronously in both domain and auth DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterPresenter {

    private final RegisterView view;
    private final CustomerDAO customerDAO;
    private final UserCredentialsDAO credentialsDAO;

    /**
     * Initializes the presenter with the view and data access layers via Dependency Injection.
     * @param view The registration view implementation.
     * @param customerDAO Data source for customer records.
     * @param credentialsDAO Data source for user authentication credentials.
     */
    public RegisterPresenter(RegisterView view, CustomerDAO customerDAO, UserCredentialsDAO credentialsDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.credentialsDAO = credentialsDAO;
    }

    /**
     * Orchestrates the asynchronous registration process.
     * Validates fields, creates a new {@link Customer}, generates a {@link UUID},
     * and persists data in Firebase/Memory asynchronously.
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
        if (username.trim().isEmpty() || password.trim().isEmpty() || emailStr.trim().isEmpty()) {
            if (view != null) view.showErrorMessage("Παρακαλώ συμπληρώστε τα απαραίτητα πεδία.");
            return;
        }

        try {
            EmailAddress emailObj = new EmailAddress(emailStr);
            String randomId = UUID.randomUUID().toString();
            Date currentDate = new Date();

            Customer newCustomer = new Customer(username, firstname, password, lastname,
                    phone, emailObj, randomId, currentDate);

            // Asynchronous saving chain: Save customer -> then save credentials -> then update UI
            customerDAO.addCustomer(newCustomer)
                    .thenCompose(aVoid -> credentialsDAO.addUser(newCustomer))
                    .thenAccept(aVoid -> {
                        if (view != null) {
                            view.showSuccessMessage("Επιτυχής εγγραφή! ID: " + randomId);
                            view.clearInputFields();
                        }
                    })
                    .exceptionally(e -> {
                        if (view != null) view.showErrorMessage("Registration error: " + e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            if (view != null) view.showErrorMessage("Σφάλμα δεδομένων: " + e.getMessage());
        }
    }
}