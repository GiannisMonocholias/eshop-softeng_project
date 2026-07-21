package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;

/**
 * The view interface for the email list screen of a Customer Service Employee.
 * Defines the methods that the email list activity must implement to handle
 * asynchronous data retrieval, navigation, and error displaying.
 * @author Γιάννης Μονοχολιάς
 */
public interface CustomerServiceEmployeeEmailListView {

    /**
     * Updates the UI with the retrieved list of emails.
     * @param emails An ArrayList of EmailMessage objects to be displayed.
     */
    void updateEmailList(ArrayList<EmailMessage> emails);

    /**
     * Displays an error message to the user, usually triggered by failed async operations.
     * @param message The error message to display.
     */
    void showError(String message);

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