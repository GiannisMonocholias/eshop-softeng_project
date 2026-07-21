package gr.softeng.team21.view.contact.editdata.Address;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.User;

/**
 * Presenter for the Address Edit activity.
 * Handles interactions between the {@link AddressView} and the User domain model.
 * Utilizes Dependency Injection for DAOs and handles asynchronous data retrieval.
 * @author PAVLOS GRATSANIS
 */
public class AddressPresenter {
    private AddressView view;
    private User user;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;

    /**
     * Initializes the presenter with the view and required DAOs, then attempts to find the user by ID asynchronously.
     * @param view The view interface.
     * @param userId The ID of the user to edit.
     * @param customerDAO The DAO for accessing customer data.
     * @param employeeDAO The DAO for accessing employee data.
     */
    public AddressPresenter(AddressView view, String userId, CustomerDAO customerDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
        findUser(userId);
    }

    /**
     * Searches for the user asynchronously in the Customer repository.
     * If not found, it falls back to the Employee repository.
     * @param userId The ID of the user.
     */
    private void findUser(String userId) {

        customerDAO.getCustomer(userId).thenAccept(customer -> {
            if (customer != null) {
                this.user = customer;
                populateAddressDetails();
            } else {
                employeeDAO.getEmployee(userId).thenAccept(employee -> {
                    if (employee != null) {
                        this.user = employee;
                        populateAddressDetails();
                    } else {
                        view.showError("Ο χρήστης δεν βρέθηκε.");
                        view.finishView();
                    }
                }).exceptionally(e -> {
                    view.showError("Σφάλμα κατά την ανάκτηση υπαλλήλου: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την ανάκτηση πελάτη: " + e.getMessage());
            return null;
        });

    }

    /**
     * Helper method to populate the view with the user's address details.
     */
    private void populateAddressDetails() {
        if (user != null && user.getAddress() != null) {
            Address addr = user.getAddress();
            view.setAddressDetails(
                    addr.getStreet(),
                    addr.getNumber(),
                    addr.getCity(),
                    addr.getCountry(),
                    addr.getZipcode()
            );
        }
    }

    /**
     * Validates the input and saves the new address for the user.
     * @param street The street name.
     * @param number The street number.
     * @param city The city name.
     * @param country The country name.
     * @param zip The postal code, it must be 5 digits.
     */
    public void saveAddressClicked(String street, String number, String city, String country, String zip) {
        if (user == null) {
            view.showError("Δεν έχει φορτωθεί ο χρήστης.");
            return;
        }

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