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
 * (Customer/Employee) asynchronously, and executes the email sending logic.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionPresenter {
    private final EmailCompositionView view;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
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
     * Identifies the sender asynchronously by checking both Customer and Employee repositories.
     * Updates the view with sender details upon successful identification.
     * @param userId The ID of the user composing the email.
     */
    public void onViewCreated(String userId) {
        customerDAO.getCustomer(userId).thenAccept(customer -> {
            if (customer != null) {
                this.sender = customer;
                updateSenderView();
            } else {
                employeeDAO.getEmployee(userId).thenAccept(employee -> {
                    if (employee != null) {
                        this.sender = employee;
                        updateSenderView();
                    } else {
                        if (view != null) {
                            view.showErrorMessage("Σφάλμα: δεν βρέθηκε χρήστης με ID " + userId + ".");
                            view.finishActivity();
                        }
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showErrorMessage("Error: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Error: " + e.getMessage());
            return null;
        });
    }

    private void updateSenderView() {
        if (sender != null && view != null) {
            String emailStr = (sender.getEmailAddress() != null) ? sender.getEmailAddress().toString() : "";
            view.setSenderDetails(sender.getFirstname() + " " + sender.getLastname(), emailStr);
        }
    }

    /**
     * Validates input fields and performs an asynchronous cross-repository search for the recipient.
     * If found, invokes the domain-level sendEmail logic.
     */
    public void onSendClicked() {
        String recipientEmailStr = view.getRecipientEmail();
        String subject = view.getSubject();
        String body = view.getBody();

        // Basic validation
        if (recipientEmailStr.trim().isEmpty() || subject.trim().isEmpty() || body.trim().isEmpty()) {
            if (view != null) view.showErrorMessage("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        // Search for recipient in Customers asynchronously
        customerDAO.getCustomers().thenAccept(customersMap -> {
            User recipient = null;
            for (Customer c : customersMap.values()) {
                if (c.getEmailAddress() != null && c.getEmailAddress().toString().equals(recipientEmailStr)) {
                    recipient = c;
                    break;
                }
            }

            if (recipient != null) {
                finalizeSending(recipient, subject, body);
            } else {
                // Search for recipient in Employees if not found in Customers
                employeeDAO.getEmployees().thenAccept(employeesMap -> {
                    User empRecipient = null;
                    for (Employee e : employeesMap.values()) {
                        if (e.getEmailAddress() != null && e.getEmailAddress().toString().equals(recipientEmailStr)) {
                            empRecipient = e;
                            break;
                        }
                    }

                    if (empRecipient != null) {
                        finalizeSending(empRecipient, subject, body);
                    } else {
                        if (view != null) view.showErrorMessage("Δεν βρέθηκε χρήστης με αυτό το email.");
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showErrorMessage("Error searching employees: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Error searching customers: " + e.getMessage());
            return null;
        });
    }

    /**
     * Executes domain logic to send the email after successful validation and search.
     */
    private void finalizeSending(User recipient, String subject, String body) {
        if (sender != null) {
            sender.sendEmail(sender, recipient, subject, body, new Date());
            if (view != null) {
                view.showSuccessMessage("Το μήνυμα εστάλη!");
                view.finishActivity();
            }
        }
    }
}