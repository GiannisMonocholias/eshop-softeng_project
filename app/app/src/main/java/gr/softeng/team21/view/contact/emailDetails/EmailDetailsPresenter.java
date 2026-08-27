package gr.softeng.team21.view.contact.emailDetails;

import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;

/**
 * Presenter for the Email Details screen.
 * Handles the logic for resolving sender and receiver identities (Names and Roles)
 * across different user categories (Employee, Customer, Admin) using asynchronous DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDetailsPresenter {
    private final EmailDetailsView view;
    private final EmployeeDAO employeeDAO;
    private final CustomerDAO customerDAO;

    /**
     * Initializes the presenter with view and data access repositories via Dependency Injection.
     * @param view The view implementation to be updated.
     * @param employeeDAO Data access for employee-related information.
     * @param customerDAO Data access for customer-related information.
     */
    public EmailDetailsPresenter(EmailDetailsView view, EmployeeDAO employeeDAO, CustomerDAO customerDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.customerDAO = customerDAO;
    }

    /**
     * Processes the raw email data and coordinates with the view to display resolved information asynchronously.
     * @param subject The subject of the email.
     * @param senderEmailAddress The email address of the sender.
     * @param receiverEmailAddress The email address of the receiver.
     * @param body The body content of the email.
     * @param employeeId The unique ID of the current user (receiver).
     */
    public void onViewCreated(String subject, String senderEmailAddress, String receiverEmailAddress, String body, String employeeId) {
        // Resolve receiver asynchronously, then sender asynchronously
        findReceiverName(employeeId).thenAccept(receiverName -> {
            findSenderName(senderEmailAddress).thenAccept(senderName -> {
                if (view != null) {
                    view.displaySubject(subject != null ? subject : "");
                    view.displaySenderName(senderName != null ? senderName : "Άγνωστο όνομα αποστολέα");
                    view.displayReceiverName(receiverName != null ? receiverName : "");
                    view.displayBody(body != null ? body : "");
                    view.displaySenderEmail(senderEmailAddress);
                    view.displayReceiverEmail(receiverEmailAddress);
                }
            });
        });
    }

    /**
     * Resolves the receiver's name asynchronously by checking Employee and Customer repositories.
     * @param userId The unique identifier of the user.
     * @return A CompletableFuture containing the full name of the receiver or an empty string if not found.
     */
    public CompletableFuture<String> findReceiverName(String userId) {
        return employeeDAO.getEmployee(userId).thenCompose(employee -> {
            if (employee != null) {
                return CompletableFuture.completedFuture(employee.getFirstname() + " " + employee.getLastname());
            } else {
                return customerDAO.getCustomer(userId).thenApply(customer -> {
                    if (customer != null) {
                        return customer.getFirstname() + " " + customer.getLastname();
                    }
                    return "";
                });
            }
        });
    }

    /**
     * Resolves the sender's identity and role label based on their email address asynchronously.
     * Checks in order: Employees, Customers, and finally the Admin singleton.
     * @param senderEmailAddress The email address to search for in the DAOs.
     * @return A CompletableFuture containing the full name and role label, or null if no match exists.
     */
    public CompletableFuture<String> findSenderName(String senderEmailAddress) {
        return employeeDAO.getEmployees().thenCompose(employeesMap -> {
            for (Employee e : employeesMap.values()) {
                if (e.getEmailAddress() != null && e.getEmailAddress().toString().equals(senderEmailAddress)) {
                    return CompletableFuture.completedFuture(e.getFirstname() + " " + e.getLastname() + " (Υπάλληλος)");
                }
            }

            return customerDAO.getCustomers().thenApply(customersMap -> {
                for (Customer c : customersMap.values()) {
                    if (c.getEmailAddress() != null && c.getEmailAddress().toString().equals(senderEmailAddress)) {
                        return c.getFirstname() + " " + c.getLastname() + " (Πελάτης)";
                    }
                }

                Admin admin = Admin.getInstance();
                if (admin.getEmailAddress() != null && admin.getEmailAddress().toString().equals(senderEmailAddress)) {
                    return admin.getFirstname() + " " + admin.getLastname() + " (Διαχειριστής)";
                }

                return null;
            });
        });
    }
}