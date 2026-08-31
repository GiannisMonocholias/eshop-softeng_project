package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Employee Email List screen.
 * Retrieves emails directly and asynchronously from the EmailDAO by querying
 * the unified collection with the employee's exact email address.
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

    /**
     * Loads the inbox by first identifying the employee's email address and then
     * fetching matching documents from the centralized EmailDAO.
     *
     * @param employeeId The ID of the logged-in employee.
     */
    public void loadInbox(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof CustomerServiceEmployee && employee.getEmailAddress() != null) {

                String employeeEmailAddress = employee.getEmailAddress().toString();

                // Fetch emails cleanly from the centralized DAO index
                emailDAO.getEmailsForUser(employeeEmailAddress).thenAccept(inbox -> {
                    if (view != null) view.updateEmailList(inbox);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Error loading emails: " + e.getMessage());
                    return null;
                });

            } else {
                if (view != null) view.showError("Employee not found or missing email address.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Error identifying employee: " + e.getMessage());
            return null;
        });
    }

    public void onCreateNewMsgSelected(String employeeId){
        if (view != null) view.navigateToCreateNewMsg(employeeId);
    }

    /**
     * Handles the selection of a specific email.
     * Marks the email as read locally, updates the state in the database asynchronously,
     * and navigates to the details screen ONLY if the update succeeds.
     */
    public void onEmailSelected(EmailMessage email, String employeeId){
        email.setRead(true);

        emailDAO.updateEmail(email).thenAccept(v -> {
            if (view != null) {
                view.navigateToEmailDetails(
                        email.getSubject(), email.getBody(),
                        email.getFrom().toString(), email.getTo().toString(),
                        employeeId
                );
            }
        }).exceptionally(e -> {
            if (view != null) {
                view.showError("Αποτυχία ενημέρωσης του μηνύματος σε 'Διαβασμένο': " + e.getMessage());
            }
            return null;
        });
    }
}