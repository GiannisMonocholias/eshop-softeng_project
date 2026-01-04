package gr.softeng.team21.view.contact.editdata.Phone;

import android.widget.Toast;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.view.contact.editdata.Username.UsernameView;

public class PhonePresenter {
    private PhoneView view;
    private Customer customer;



    public PhonePresenter(PhoneView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void savePhoneClicked(String phone) {
        if (phone.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε τηλέφωνο");
            return;
        }
        if (phone.length() != 10) {
            view.showError("Το τηλέφωνο πρέπει να έχει 10 ψηφία");
            return;
        }
        try {
            customer.editData("5", phone, null, null);
            view.SaveSuccess("Το τηλέφωνο ενημερώθηκε!");

        } catch (Exception e) {
         view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}
