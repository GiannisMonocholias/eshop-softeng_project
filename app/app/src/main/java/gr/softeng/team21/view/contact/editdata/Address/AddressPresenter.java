package gr.softeng.team21.view.contact.editdata.Address;

import android.widget.Toast;

import gr.softeng.team21.domain.Address;
import gr.softeng.team21.domain.Customer;

public class AddressPresenter {
    private AddressView view;
    private Customer customer;

    public AddressPresenter(AddressView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void saveAddressClicked(String street, String number, String zip, String city, String country) {
        if (street.isEmpty() || number.isEmpty() || zip.isEmpty() || city.isEmpty() || country.isEmpty()) {
            view.showError("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης");
            return;
        }

        if (zip.length() != 5) {
            view.showError("Ο ΤΚ πρέπει να είναι 5 ψηφία");
            return;
        }
        try {
            Address newAddress = new Address(street, number, city, zip, country);
            customer.editData("3",null,newAddress,null);
            view.SaveSuccess("Η διεύθυνση ενημερώθηκε επιτυχώς!");

        } catch (Exception e) {
            view.showError("Σφάλμα: " + e.getMessage());
        }
    }
}
