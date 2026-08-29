package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Email List screen.
 * Retrieves emails directly and asynchronously from the EmailDAO, ensuring that
 * domain entities remain decoupled from data access logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListPresenter {
    private final EmployeeDAO employeeDAO;
    private final EmailDAO emailDAO;
    private final CustomerServiceEmployeeEmailListView view;

    public CustomerServiceEmployeeEmailListPresenter(CustomerServiceEmployeeEmailListView view, EmployeeDAO employeeDAO, EmailDAO emailDAO) {
        this.employeeDAO = employeeDAO;
        this.emailDAO = emailDAO;
        this.view = view;
    }

    public void loadInbox(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof CustomerServiceEmployee) {
                // Fetch emails cleanly from the DAO instead of the Domain Object
                emailDAO.getInboxEmails().thenAccept(inbox -> {
                    if (view != null) view.updateEmailList(inbox);
                });
            } else {
                if (view != null) view.showError("Employee not found or invalid employee type.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Error loading inbox: " + e.getMessage());
            return null;
        });
    }

    public void onCreateNewMsgSelected(String employeeId){
        if (view != null) view.navigateToCreateNewMsg(employeeId);
    }

    public void onEmailSelected(EmailMessage email, String employeeId){
        email.setRead(true);
        if (view != null) {
            view.navigateToEmailDetails(
                    email.getSubject(), email.getBody(),
                    email.getFrom().toString(), email.getTo().toString(),
                    employeeId
            );
        }
    }
}