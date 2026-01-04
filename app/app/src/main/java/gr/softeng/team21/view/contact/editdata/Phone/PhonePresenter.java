package gr.softeng.team21.view.contact.editdata.Phone;

import android.util.Log;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class PhonePresenter {
    private PhoneView view;
    private User user;

    public PhonePresenter(PhoneView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    private void findUser(String userId) {
        user = CustomerDAOMemory.getInstance().getCustomer(userId);

        if (user == null) {
            user = EmployeeDAOMemory.getInstance().getEmployee(userId);
        }

        if (userId != null) {
            Log.d("PhonePresenter", "User ID: " + userId);
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

        try {
            user.editData("5", phone, null, null);
            view.SaveSuccess("Το τηλέφωνο ενημερώθηκε επιτυχώς!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}