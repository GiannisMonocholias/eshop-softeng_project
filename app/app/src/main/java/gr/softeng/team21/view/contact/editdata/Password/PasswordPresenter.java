package gr.softeng.team21.view.contact.editdata.Password;

import android.widget.Toast;

import gr.softeng.team21.domain.Customer;

public class PasswordPresenter {
    private PasswordView view;
    private Customer customer;

    public PasswordPresenter(PasswordView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void savePasswordClicked(String password) {
        if (password.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε κωδικό");
            return;
        }

        if (password.length() < 8) {
            view.showError("Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες");
            return;
        }

        try {
            // Η επιλογή "2" αντιστοιχεί στο Password (βάσει της λίστας στο μενού)
            customer.editData("2", password, null, null);

            view.SaveSuccess("Ο κωδικός ενημερώθηκε!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}
