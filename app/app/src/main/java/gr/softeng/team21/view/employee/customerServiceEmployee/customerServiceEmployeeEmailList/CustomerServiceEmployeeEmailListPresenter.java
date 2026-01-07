package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Email List screen.
 * Handles the logic of retrieving emails and processing user interactions
 * before triggering navigation commands to the view.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListPresenter {
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployeeEmailListView view;

    /**
     * Constructs a presenter with the necessary DAO and view references.
     * @param view The view implementation (Activity).
     * @param employeeDAO The data access object for employee information.
     */
    public CustomerServiceEmployeeEmailListPresenter(CustomerServiceEmployeeEmailListView view, EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
        this.view = view;
    }

    /**
     * Retrieves all incoming emails for a specific employee.
     * @param employeeId The ID of the employee.
     * @return An ArrayList of EmailMessage objects.
     */
    public ArrayList<EmailMessage> getInbox(String employeeId){
        CustomerServiceEmployee employee = (CustomerServiceEmployee) employeeDAO.getEmployee(employeeId);
        return employee.getEmailProvider().getInboxEmails();
    }

    /**
     * Called when the user wants to compose a new email.
     * @param employeeId The current employee's ID.
     */
    public void onCreateNewMsgSelected(String employeeId){
        view.navigateToCreateNewMsg(employeeId);
    }

    /**
     * Handles the selection of a specific email. Marks the email as read
     * and triggers navigation to the details screen.
     * @param email The selected email message.
     * @param Id The current employee's ID.
     */
    public void onEmailSelected(EmailMessage email, String Id){
        email.setRead(true);
        view.navigateToEmailDetails(
                email.getSubject(), email.getBody(),
                email.getFrom().toString(), email.getTo().toString(),
                Id
        );
    }
}