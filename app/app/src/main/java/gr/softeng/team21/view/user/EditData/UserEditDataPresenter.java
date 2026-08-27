package gr.softeng.team21.view.user.EditData;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.User;

/**
 * Presenter handling the asynchronous business logic for editing unified user data.
 * Validates inputs, coordinates DAOs, and tracks data snapshots for unsaved changes detection.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataPresenter {
    private final UserEditDataView view;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private User currentUser;

    // Snapshot variables to detect unsaved changes
    private String origUsername = "", origPassword = "", origEmail = "";
    private String origFirstName = "", origLastName = "", origPhone = "";
    private String origStreet = "", origStreetNo = "", origCity = "", origZip = "", origCountry = "";

    public UserEditDataPresenter(UserEditDataView view, CustomerDAO customerDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously fetches the user data from Firebase and populates the View.
     * Takes a snapshot of the initial data for comparison.
     * @param userId The unique ID of the user.
     */
    public void loadUserData(String userId) {
        customerDAO.getCustomer(userId).thenAccept(customer -> {
            if (customer != null) {
                this.currentUser = customer;
                takeSnapshotAndShow();
            } else {
                employeeDAO.getEmployee(userId).thenAccept(employee -> {
                    if (employee != null) {
                        this.currentUser = employee;
                        takeSnapshotAndShow();
                    } else {
                        if (view != null) {
                            view.showMessage("Ο χρήστης δεν βρέθηκε.");
                            view.finishView();
                        }
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
            return null;
        });
    }

    private void takeSnapshotAndShow() {
        if (currentUser == null || view == null) return;

        origUsername = currentUser.getUsername() != null ? currentUser.getUsername() : "";
        origPassword = currentUser.getPassword() != null ? currentUser.getPassword() : "";
        origEmail = currentUser.getEmailAddress() != null ? currentUser.getEmailAddress().toString() : "";
        origFirstName = currentUser.getFirstname() != null ? currentUser.getFirstname() : "";
        origLastName = currentUser.getLastname() != null ? currentUser.getLastname() : "";
        origPhone = currentUser.getPhonenumber() != null ? currentUser.getPhonenumber() : "";

        if (currentUser.getAddress() != null) {
            origStreet = currentUser.getAddress().getStreet() != null ? currentUser.getAddress().getStreet() : "";
            origStreetNo = currentUser.getAddress().getNumber() != null ? currentUser.getAddress().getNumber() : "";
            origCity = currentUser.getAddress().getCity() != null ? currentUser.getAddress().getCity() : "";
            origZip = currentUser.getAddress().getZipcode() != null ? currentUser.getAddress().getZipcode() : "";
            origCountry = currentUser.getAddress().getCountry() != null ? currentUser.getAddress().getCountry() : "";
        }

        view.showUserData(origUsername, origPassword, origEmail, origFirstName, origLastName, origPhone,
                origStreet, origStreetNo, origCity, origZip, origCountry);
    }

    /**
     * Validates and saves the user inputs to the domain model.
     */
    public void onSaveClicked(String username, String password, String email, String fName, String lName, String phone,
                              String street, String streetNo, String city, String zip, String country) {
        if (currentUser == null) {
            if (view != null) view.showMessage("Σφάλμα: Δεν έχει φορτωθεί ο χρήστης.");
            return;
        }

        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            if (view != null) view.showMessage("Συμπληρώστε τα υποχρεωτικά πεδία (Όνομα Χρήστη, Κωδικός, Email).");
            return;
        }

        if (password.length() < 8) {
            if (view != null) view.showMessage("Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες.");
            return;
        }

        if (!phone.trim().isEmpty() && phone.length() != 10) {
            if (view != null) view.showMessage("Το τηλέφωνο πρέπει να έχει 10 ψηφία.");
            return;
        }

        currentUser.setUsername(username);
        currentUser.setPassword(password);
        currentUser.setEmailaddress(new EmailAddress(email));
        currentUser.setFirstname(fName);
        currentUser.setLastname(lName);
        currentUser.setPhonenumber(phone);

        if (!street.isEmpty() || !city.isEmpty()) {
            Address newAddress = new Address(street, streetNo, city, country, zip);
            currentUser.setAddress(newAddress);
        }

        // Update the snapshot so the Back button won't trigger the unsaved changes warning
        origUsername = username; origPassword = password; origEmail = email;
        origFirstName = fName; origLastName = lName; origPhone = phone;
        origStreet = street; origStreetNo = streetNo; origCity = city;
        origZip = zip; origCountry = country;

        if (view != null) {
            view.showMessage("Τα στοιχεία σας ενημερώθηκαν επιτυχώς!");
            view.finishView();
        }
    }

    /**
     * Checks if current UI inputs differ from the loaded snapshot.
     */
    public void onBackPressed(String username, String password, String email, String fName, String lName, String phone,
                              String street, String streetNo, String city, String zip, String country) {

        boolean hasChanges = !origUsername.equals(username) || !origPassword.equals(password) || !origEmail.equals(email) ||
                !origFirstName.equals(fName) || !origLastName.equals(lName) || !origPhone.equals(phone) ||
                !origStreet.equals(street) || !origStreetNo.equals(streetNo) || !origCity.equals(city) ||
                !origZip.equals(zip) || !origCountry.equals(country);

        if (hasChanges && view != null) {
            view.showUnsavedChangesDialog();
        } else if (view != null) {
            view.finishView();
        }
    }
}