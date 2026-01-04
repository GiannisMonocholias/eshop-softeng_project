package gr.softeng.team21.view.contact.editdata.Password;

import android.util.Log;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class PasswordPresenter {
    private PasswordView view;
    private User user;
    private String currentPassword;

    public PasswordPresenter(PasswordView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    private void findUser(String userId) {
        user = CustomerDAOMemory.getInstance().getCustomer(userId);

        if (user == null) {
            user = EmployeeDAOMemory.getInstance().getEmployee(userId);
        }

        // Debug log
        if (userId != null) {
            Log.d("PasswordPresenter", "User ID: " + userId);
        }

        if (user == null) {
            view.showError("Ο χρήστης δεν βρέθηκε.");
            view.finishView();
            return;
        }

        if (user.getPassword() != null) {
            currentPassword = user.getPassword();
            view.setPassword(currentPassword);
        }
    }

    public void savePasswordClicked(String password) {
        if (user == null) return;

        if (password.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε κωδικό");
            return;
        }

        if (password.length() < 8) {
            view.showError("Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες");
            return;
        }

        if (currentPassword != null && currentPassword.equals(password)) {
            view.showError("Ο νέος κωδικός δεν μπορεί να είναι ίδιος με τον παλιό");
            return;
        }

        try {
            user.editData("2", password, null, null);
            view.SaveSuccess("Ο κωδικός ενημερώθηκε επιτυχώς!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}