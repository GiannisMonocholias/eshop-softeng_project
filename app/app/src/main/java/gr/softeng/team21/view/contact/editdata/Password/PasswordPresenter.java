package gr.softeng.team21.view.contact.editdata.Password;

import android.util.Log;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Presenter for the Password Edit activity.
 * Handles interactions between the {@link PasswordView} and the User domain model.
 * @author PAVLOS GRATSANIS
 */
public class PasswordPresenter {
    private PasswordView view;
    private User user;
    private String currentPassword;

    /**
     * Initializes the presenter with the view and attempts to find the user by ID.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     */
    public PasswordPresenter(PasswordView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    /**
     * Searches for the user in both Customer and Employee repositories.
     * If found, it completes the view with the existing password.
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

        if (user.getPassword() != null) {
            currentPassword = user.getPassword();
            view.setPassword(currentPassword);
        }
    }

    /**
     * Validates the input and saves the new password for the user.
     * @param password The new password to save, it must be at least 8 chars.
     */
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
        user.editData("2", password, null, null);
        view.SaveSuccess("Ο κωδικός ενημερώθηκε επιτυχώς!");
    }
}