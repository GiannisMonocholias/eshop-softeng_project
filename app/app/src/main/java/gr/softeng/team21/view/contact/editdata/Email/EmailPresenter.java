package gr.softeng.team21.view.contact.editdata.Email;

import android.util.Log;
import android.util.Patterns;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class EmailPresenter {
    private EmailView view;
    private User user;

    public EmailPresenter(EmailView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    private void findUser(String userId) {
        user = CustomerDAOMemory.getInstance().getCustomer(userId);

        if (user == null) {
            user = EmployeeDAOMemory.getInstance().getEmployee(userId);
        }

        if (userId != null) {
            Log.d("EmailPresenter", "User ID: " + userId);
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

        if (!Patterns.EMAIL_ADDRESS.matcher(mailtxt).matches()) {
            view.showError("Μη έγκυρη μορφή Email");
            return;
        }

        try {
            user.editData("4", mailtxt, null, new EmailAddress(mailtxt));
            view.SaveSuccess("Το Email ενημερώθηκε επιτυχώς!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}