package gr.softeng.team21.view.contact.editdata.Email;

import java.util.regex.Pattern;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Presenter for the Email Edit activity.
 * Handles interactions between the {@link EmailView} and the User domain model.
 * @author PAVLOS GRATSANIS
 */
public class EmailPresenter {
    private EmailView view;
    private User user;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Initializes the presenter with the view and attempts to find the user by ID.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     */
    public EmailPresenter(EmailView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    /**
     * Searches for the user in both Customer and Employee repositories.
     * If found, it completes the view with the existing email address.
     * @param userId The ID of the user.
     */
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

    /**
     * Validates the input and saves the new email address for the user.
     * Checks if the email format is valid.
     * @param mailtxt The new email address to save.
     */
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