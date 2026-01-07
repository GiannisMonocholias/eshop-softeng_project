package gr.softeng.team21.view.contact.editdata.Email;

import java.util.regex.Pattern;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class EmailPresenter {
    private EmailView view;
    private User user;

    // Ορίζουμε ένα Pattern για email χρησιμοποιώντας καθαρή Java
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public EmailPresenter(EmailView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    private void findUser(String userId) {
        user = CustomerDAOMemory.getInstance().getCustomer(userId);

        if (user == null) {
            user = EmployeeDAOMemory.getInstance().getEmployee(userId);
        }

        if (user == null) {
            view.showError("Ο χρήστης δεν βρέθηκε.");
            view.finishView();
            return;
        }

        if (user.getEmailAddress() != null && !user.getEmailAddress().toString().isEmpty()) {
            view.setEmail(user.getEmailAddress().toString());
        }
    }

    public void saveEmailClicked(String mailtxt) {
        if (user == null) return;
        if (mailtxt.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε Email");
            return;
        }

        if (!EMAIL_PATTERN.matcher(mailtxt).matches()) {
            view.showError("Μη έγκυρη μορφή email");
            return;
        }
            user.editData("4", mailtxt, null, new EmailAddress(mailtxt));
            view.SaveSuccess("Το email ενημερώθηκε επιτυχώς!");
        }

}