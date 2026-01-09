package gr.softeng.team21.view.contact.emailDetails;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Presenter for the Email Details screen.
 * Handles the logic for resolving sender and receiver identities (Names and Roles)
 * across different user categories (Employee, Customer, Admin).
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDetailsPresenter {
    private EmailDetailsView view;
    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;

    /**
     * Initializes the presenter with view and data access repositories.
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
     * Processes the raw email data and coordinates with the view to display resolved information.
     * @param subject The subject of the email.
     * @param senderEmailAddress The email address of the sender.
     * @param receiverEmailAddress The email address of the receiver.
     * @param body The body content of the email.
     * @param employeeId The unique ID of the current user (receiver).
     */
    public void onViewCreated(String subject, String senderEmailAddress, String receiverEmailAddress, String body, String employeeId) {
        String senderName = findSenderName(senderEmailAddress);

        view.displaySubject(subject != null ? subject : "");
        view.displaySenderName(senderName != null ? senderName : "Άγνωστο όνομα αποστολέα");
        view.displayReceiverName(findReceiverName(employeeId));
        view.displayBody(body != null ? body : "");
        view.displaySenderEmail(senderEmailAddress);
        view.displayReceiverEmail(receiverEmailAddress);
    }

    /**
     * Resolves the receiver's name by checking Employee and Customer repositories.
     * @param userId The unique identifier of the user.
     * @return The full name of the receiver or an empty string if not found.
     */
    public String findReceiverName(String userId){
        Employee employee = employeeDAO.getEmployee(userId);
        if(employee != null){
            return employee.getFirstname() + " " +  employee.getLastname();
        }

        Customer customer = customerDAO.getCustomer(userId);
        if(customer != null){
            return customer.getFirstname() + " " + customer.getLastname();
        }
        return "";
    }

    /**
     * Resolves the sender's identity and role label based on their email address.
     * Checks in order: Employees, Customers, and finally the Admin singleton.
     * @param senderEmailAddress The email address to search for in the DAOs.
     * @return A string containing the full name and role label, or null if no match exists.
     */
    public String findSenderName(String senderEmailAddress){
        Employee employee = ((EmployeeDAOMemory)employeeDAO).getEmployeeByEmail(senderEmailAddress);
        if(employee != null){
            return employee.getFirstname() + " " +  employee.getLastname() + " (Υπάλληλος)";
        }

        Customer customer = ((CustomerDAOMemory)customerDAO).getCustomerByEmail(senderEmailAddress);
        if(customer != null){
            return customer.getFirstname() + " " +  customer.getLastname() + " (Πελάτης)";
        }

        Admin admin = Admin.getInstance();
        if(admin.getEmailAddress().toString().equals(senderEmailAddress)){
            return admin.getFirstname() + " " + admin.getLastname() + " (Διαχειριστής)";
        }

        return null;
    }
}