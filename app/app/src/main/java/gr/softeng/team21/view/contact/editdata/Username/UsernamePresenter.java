package gr.softeng.team21.view.contact.editdata.Username;

import gr.softeng.team21.domain.Customer;

public class UsernamePresenter {
    private UsernameView view;
    private Customer customer;
    public UsernamePresenter(UsernameView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void SaveUsernameClicked(String name) {
        if (name.isEmpty()) {
            view.showError("Παρακαλώ συμπληρώστε τo πεδίo");
            return;
        }
        try {
            customer.editData("1", name, null, null);
            view.SaveSuccess("To username ενημερώθηκε!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}
