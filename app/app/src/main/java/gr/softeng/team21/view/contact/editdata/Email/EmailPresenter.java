package gr.softeng.team21.view.contact.editdata.Email;

import android.util.Patterns;

import gr.softeng.team21.domain.Customer;

public class EmailPresenter {
    private EmailView view;
    private Customer customer;

    public EmailPresenter(EmailView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void saveEmailClicked(String mailtxt) {
        if (mailtxt.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε Email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mailtxt).matches()) {
            view.showError("Μη έγκυρη μορφή Email");
            return;
        }

        try {
            customer.editData("4", mailtxt, null, null);

            view.SaveSuccess("Το Email ενημερώθηκε!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}

