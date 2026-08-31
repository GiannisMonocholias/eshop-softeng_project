package gr.softeng.team21.view.user.emailComposition;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.User;

/**
 * Presenter for the Email Composition screen.
 * Handles user identification, recipient lookup across different user types
 * (Customer/Employee) asynchronously, and executes the unified email dispatch logic via EmailDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionPresenter {
    private final EmailCompositionView view;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private final EmailDAO emailDAO;
    private User sender;

    /**
     * Initializes the presenter with required DAOs and view interface.
     */
    public EmailCompositionPresenter(EmailCompositionView view, CustomerDAO customerDAO, EmployeeDAO employeeDAO, EmailDAO emailDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
        this.emailDAO = emailDAO;
    }

    /**
     * Identifies the sender asynchronously by checking both Customer and Employee repositories.
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
     * If found, constructs and dispatches the email via the centralized EmailDAO.
     */
    public void onSendClicked() {
        if (view == null) return;

        String recipientEmailStr = view.getRecipientEmail();
        String subject = view.getSubject();
        String body = view.getBody();

        boolean hasError = false;

        // Validation & highlight empty fields
        if (recipientEmailStr.trim().isEmpty()) {
            view.showInputError("recipient", "Το email παραλήπτη είναι υποχρεωτικό.");
            hasError = true;
        }
        if (subject.trim().isEmpty()) {
            view.showInputError("subject", "Το θέμα είναι υποχρεωτικό.");
            hasError = true;
        }
        if (body.trim().isEmpty()) {
            view.showInputError("body", "Το κείμενο μηνύματος δεν μπορεί να είναι κενό.");
            hasError = true;
        }

        if (hasError) return;

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
                        view.showErrorMessage("Δεν βρέθηκε χρήστης με αυτό το email.");
                    }
                }).exceptionally(e -> {
                    view.showErrorMessage("Error searching employees: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            view.showErrorMessage("Error searching customers: " + e.getMessage());
            return null;
        });
    }

    /**
     * Constructs the EmailMessage object and delegates persistence to the EmailDAO.
     * A single database entry is created for both users (unified collection).
     */
    private void finalizeSending(User recipient, String subject, String body) {
        if (sender != null) {
            EmailMessage email = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(), subject, body, new Date());

            // A single save operation handles the dispatch
            emailDAO.saveEmail(email).thenAccept(v -> {
                if (view != null) {
                    view.showSuccessMessage("Το μήνυμα εστάλη επιτυχώς!");
                }
            }).exceptionally(e -> {
                if (view != null) view.showErrorMessage("Αποτυχία αποστολής: " + e.getMessage());
                return null;
            });
        }
    }
}