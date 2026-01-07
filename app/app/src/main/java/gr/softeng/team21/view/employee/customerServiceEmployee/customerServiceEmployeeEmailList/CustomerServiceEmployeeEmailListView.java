package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

/**
 * The view interface for the email list screen of a Customer Service Employee.
 * Defines the methods that the email list activity must implement.
 * @author Γιάννης Μονοχολιάς
 */
public interface CustomerServiceEmployeeEmailListView {

    /**
     * Navigates to the email composition screen.
     * @param employeeId The ID of the employee sending the message.
     */
    void navigateToCreateNewMsg(String employeeId);

    /**
     * Navigates to the details screen of a specific email.
     * @param subject    The email subject.
     * @param body       The email body content.
     * @param sender     The sender's email address.
     * @param receiver   The recipient's email address.
     * @param employeeId The current user's ID for context.
     */
    void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId);
}