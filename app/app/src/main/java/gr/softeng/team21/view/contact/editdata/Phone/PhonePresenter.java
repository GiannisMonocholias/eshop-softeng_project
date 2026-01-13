package gr.softeng.team21.view.contact.editdata.Phone;

import android.util.Log;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Presenter for the Phone Edit activity.
 * Handles interactions between the {@link PhoneView} and the User domain model.
 * @author PAVLOS GRATSANIS
 */
public class PhonePresenter {
    private PhoneView view;
    private User user;

    /**
     * Initializes the presenter with the view and attempts to find the user by ID.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     */
    public PhonePresenter(PhoneView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    /**
     * Searches for the user in both Customer and Employee repositories.
     * If found, it completes the view with the existing phone number.
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

        if (user.getPhonenumber() != null) {
            view.setPhone(user.getPhonenumber());
        }
    }

    /**
     * Validates the input and saves the new phone number for the user.
     * @param phone The phone number to save, it must be 10 digits.
     */
    public void savePhoneClicked(String phone) {
        if (user == null) return;

        if (phone.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε τηλέφωνο");
            return;
        }

        if (phone.length() != 10) {
            view.showError("Το τηλέφωνο πρέπει να έχει 10 ψηφία");
            return;
        }
        user.editData("5", phone, null, null);
        view.SaveSuccess("Το τηλέφωνο ενημερώθηκε επιτυχώς!");
    }
}