package gr.softeng.team21.view.contact.editdata.Address;

import android.util.Log;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class AddressPresenter {
    private AddressView view;
    private User user;

    public AddressPresenter(AddressView view, String userId) {
        this.view = view;
        findUser(userId);
    }

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

        if (user.getAddress() != null) {
            Address addr = user.getAddress();
            view.setAddressDetails(
                    addr.getStreet(),
                    addr.getNumber(),
                    addr.getZipcode(),
                    addr.getCity(),
                    addr.getCountry()
            );
        }
    }


    public void saveAddressClicked(String street, String number, String city, String country, String zip) {
        if (user == null) return;

        if (street.isEmpty() || number.isEmpty() || zip.isEmpty() || city.isEmpty() || country.isEmpty()) {
            view.showError("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης");
            return;
        }

        if (zip.length() != 5) {
            view.showError("Ο ΤΚ πρέπει να είναι 5 ψηφία");
            return;
        }
        Address newAddress = new Address(street, number, city, country, zip);
        user.editData("3", null, newAddress, null);
        view.SaveSuccess("Η διεύθυνση ενημερώθηκε επιτυχώς!");
    }
}