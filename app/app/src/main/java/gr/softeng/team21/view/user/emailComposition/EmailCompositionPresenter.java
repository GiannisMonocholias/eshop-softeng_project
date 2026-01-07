package gr.softeng.team21.view.user.emailComposition;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.User;

public class EmailCompositionPresenter {
    private EmailCompositionView view;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;

    private User loggedInUser;

    private User sender;

    public EmailCompositionPresenter(EmailCompositionView view, CustomerDAO customerDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
    }

    public void onViewCreated(String userId) {
        sender = customerDAO.getCustomer(userId);

        if (sender == null) {
            sender = employeeDAO.getEmployee(userId);
        }

        if (sender != null) {
            String emailStr = (sender.getEmailAddress() != null) ? sender.getEmailAddress().toString() : "";
            view.setSenderDetails(
                    sender.getFirstname() + " " + sender.getLastname(),
                    emailStr
            );
        } else {
            view.showErrorMessage("Σφάλμα: Ο χρήστης με ID " + userId + " δεν βρέθηκε.");
            view.finishActivity();
        }
    }

    public void onSendClicked() {
        String recipientEmailStr = view.getRecipientEmail();
        String subject = view.getSubject();
        String body = view.getBody();

        if (recipientEmailStr.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            view.showErrorMessage("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        User recipient = null;

        for (Customer c : customerDAO.getCustomers().values()) {
            if (c.getEmailAddress() != null &&
                    c.getEmailAddress().toString().equals(recipientEmailStr)) {
                recipient = c;
                break;
            }
        }

        if (recipient == null) {
            for (Employee e : employeeDAO.getEmployees().values()) {
                if (e.getEmailAddress() != null &&
                        e.getEmailAddress().toString().equals(recipientEmailStr)) {
                    recipient = e;
                    break;
                }
            }
        }

        if (recipient == null) {
            view.showErrorMessage("Δεν βρέθηκε χρήστης με αυτό το email.");
            return;
        }

        if (sender != null) {
                sender.sendEmail(sender, recipient, subject, body, new Date());
                view.showSuccessMessage("Το μήνυμα εστάλη!");
                view.finishActivity();
        }
    }
}