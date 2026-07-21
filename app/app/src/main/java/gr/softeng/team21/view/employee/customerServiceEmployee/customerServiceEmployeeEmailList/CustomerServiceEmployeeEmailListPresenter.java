package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Email List screen.
 * Handles the logic of retrieving emails asynchronously and processing user interactions
 * before triggering navigation or update commands to the view.
 * Utilizes Dependency Injection to decouple the data source from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListPresenter {
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployeeEmailListView view;

    /**
     * Constructs a presenter with the necessary DAO and view references.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data access object for employee information.
     */
    public CustomerServiceEmployeeEmailListPresenter(CustomerServiceEmployeeEmailListView view, EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
        this.view = view;
    }

    /**
     * Asynchronously retrieves all incoming emails for a specific employee and updates the view.
     * Handles potential errors if the employee is not found or is of the wrong type.
     * @param employeeId The ID of the employee.
     */
    public void loadInbox(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof CustomerServiceEmployee) {
                ArrayList<EmailMessage> inbox = ((CustomerServiceEmployee) employee).getEmailProvider().getInboxEmails();
                view.updateEmailList(inbox);
            } else {
                view.showError("Employee not found or invalid employee type.");
            }
        }).exceptionally(e -> {
            view.showError("Error loading inbox: " + e.getMessage());
            return null;
        });
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