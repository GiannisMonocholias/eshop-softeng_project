package gr.softeng.team21.view.user.emailComposition;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.User;

/**
 * Presenter for the Email Composition screen.
 * Handles user identification, recipient lookup across different user types
 * (Customer/Employee), and executes the email sending logic.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionPresenter {
    private EmailCompositionView view;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private User sender;

    /**
     * Initializes the presenter with required DAOs and view interface.
     */
    public EmailCompositionPresenter(EmailCompositionView view, CustomerDAO customerDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Identifies the sender by checking both Customer and Employee repositories.
     * Updates the view with sender details upon successful identification.
     * @param userId The ID of the user composing the email.
     */
    public void onViewCreated(String userId) {
        sender = customerDAO.getCustomer(userId);

        if (sender == null) {
            sender = employeeDAO.getEmployee(userId);
        }

        if (sender != null) {
            String emailStr = (sender.getEmailAddress() != null) ? sender.getEmailAddress().toString() : "";
            view.setSenderDetails(
                    sender.getFirstname() + " " + sender.getLastname(),
                    emailStr
            );
        } else {
            view.showErrorMessage("Σφάλμα: δεν βρέθηκε χρήστης με ID" + userId + ".");
            view.finishActivity();
        }
    }

    /**
     * Validates input fields and performs a cross-repository search for the recipient.
     * If found, invokes the domain-level sendEmail logic.
     */
    public void onSendClicked() {
        String recipientEmailStr = view.getRecipientEmail();
        String subject = view.getSubject();
        String body = view.getBody();

        // Basic validation
        if (recipientEmailStr.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            view.showErrorMessage("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        User recipient = null;

        // Search for recipient in Customers
        for (Customer c : customerDAO.getCustomers().values()) {
            if (c.getEmailAddress() != null &&
                    c.getEmailAddress().toString().equals(recipientEmailStr)) {
                recipient = c;
                break;
            }
        }

        // Search for recipient in Employees if not found in Customers
        if (recipient == null) {
            for (Employee e : employeeDAO.getEmployees().values()) {
                if (e.getEmailAddress() != null &&
                        e.getEmailAddress().toString().equals(recipientEmailStr)) {
                    recipient = e;
                    break;
                }
            }
        }

        if (recipient == null) {
            view.showErrorMessage("Δεν βρέθηκε χρήστης με αυτό το email.");
            return;
        }

        // Execute domain logic
        if (sender != null) {
            sender.sendEmail(sender, recipient, subject, body, new Date());
            view.showSuccessMessage("Το μήνυμα εστάλη!");
            view.finishActivity();
        }
    }
}