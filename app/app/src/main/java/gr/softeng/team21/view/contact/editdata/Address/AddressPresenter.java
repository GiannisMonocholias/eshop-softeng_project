package gr.softeng.team21.view.contact.editdata.Address;

import android.util.Log;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Presenter for the Address Edit activity.
 * Handles interactions between the {@link AddressView} and the User domain model.
 * @author PAVLOS GRATSANIS
 */
public class AddressPresenter {
    private AddressView view;
    private User user;

    /**
     * Initializes the presenter with the view and attempts to find the user by ID.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     */
    public AddressPresenter(AddressView view, String userId) {
        this.view = view;
        findUser(userId);
    }

    /**
     * Searches for the user in both Customer and Employee repositories.
     * If found, it completes the view with existing address data.
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

    /**
     * Validates the input and saves the new address for the user.
     * @param street The street name.
     * @param number The street number.
     * @param city The city name.
     * @param country The country name.
     * @param zip The postal code , it must be 5 digits.
     */
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