package gr.softeng.team21.view.contact.editdata.Email;

import java.util.regex.Pattern;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.User;

/**
 * Presenter for the Email Edit activity.
 * Handles interactions between the {@link EmailView} and the User domain model.
 * Handles asynchronous data retrieval.
 * @author PAVLOS GRATSANIS
 */
public class EmailPresenter {
    private EmailView view;
    private User user;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Initializes the presenter with the view and required DAOs, then attempts to find the user by ID asynchronously.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     * @param customerDAO The DAO for accessing customer data.
     * @param employeeDAO The DAO for accessing employee data.
     */
    public EmailPresenter(EmailView view, String userId, CustomerDAO customerDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
        findUser(userId);
    }

    /**
     * Searches for the user asynchronously in the Customer repository.
     * If not found, it falls back to the Employee repository.
     * @param userId The ID of the user.
     */
    private void findUser(String userId) {
        customerDAO.getCustomer(userId).thenAccept(customer -> {

            if (customer != null) {
                this.user = customer;
                populateEmailDetails();
            } else {
                employeeDAO.getEmployee(userId).thenAccept(employee -> {
                    if (employee != null) {
                        this.user = employee;
                        populateEmailDetails();
                    } else {
                        view.showError("Ο χρήστης δεν βρέθηκε.");
                        view.finishView();
                    }
                }).exceptionally(e -> {
                    view.showError("Σφάλμα κατά την ανάκτηση υπαλλήλου: " + e.getMessage());
                    return null;
                });
            }

        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την ανάκτηση πελάτη: " + e.getMessage());
            return null;
        });
    }

    /**
     * Helper method to populate the view with the user's email address details.
     */
    private void populateEmailDetails() {
        if (user != null && user.getEmailAddress() != null && !user.getEmailAddress().toString().isEmpty()) {
            view.setEmail(user.getEmailAddress().toString());
        }
    }

    /**
     * Validates the input and saves the new email address for the user.
     * Checks if the email format is valid.
     * @param mailtxt The new email address to save.
     */
    public void saveEmailClicked(String mailtxt) {
        if (user == null) {
            view.showError("Δεν έχει φορτωθεί ο χρήστης.");
            return;
        }

        if (mailtxt.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε Email");
            return;
        }

        if (!EMAIL_PATTERN.matcher(mailtxt).matches()) {
            view.showError("Μη έγκυρη μορφή email");
            return;
        }

        user.editData("4", mailtxt, null, new EmailAddress(mailtxt));
        view.SaveSuccess("Το email ενημερώθηκε επιτυχώς!");
    }
}