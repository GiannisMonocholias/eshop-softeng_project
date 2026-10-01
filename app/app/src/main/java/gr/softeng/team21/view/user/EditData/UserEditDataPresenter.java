package gr.softeng.team21.view.user.EditData;

import android.net.Uri;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ImageStorageDAO;
import gr.softeng.team21.domain.User;

/**
 * Presenter handling the asynchronous business logic for editing unified user data.
 * Validates inputs, coordinates DAOs, handles profile image uploads via ImageStorageDAO,
 * and tracks data snapshots for unsaved changes detection.
 *
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataPresenter {
    private final UserEditDataView view;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private final ImageStorageDAO imageStorageDAO;
    private User currentUser;

    // Snapshot variables to detect unsaved changes
    private String origUsername = "", origPassword = "", origEmail = "";
    private String origFirstName = "", origLastName = "", origPhone = "";
    private String origStreet = "", origStreetNo = "", origCity = "", origZip = "", origCountry = "";

    /**
     * Constructs the presenter with injected dependencies.
     *
     * @param view            The view interface for UI updates.
     * @param customerDAO     DAO for customer data operations.
     * @param employeeDAO     DAO for employee data operations.
     * @param imageStorageDAO DAO for handling image uploads and deletions.
     */
    public UserEditDataPresenter(UserEditDataView view, CustomerDAO customerDAO, EmployeeDAO employeeDAO, ImageStorageDAO imageStorageDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.employeeDAO = employeeDAO;
        this.imageStorageDAO = imageStorageDAO;
    }

    /**
     * Asynchronously fetches the user data from Firebase and populates the View.
     * Takes a snapshot of the initial data for comparison.
     *
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
                            view.showMessage("User not found.");
                            view.finishView();
                        }
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showMessage("Error: " + e.getMessage());
                    return null;
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Error: " + e.getMessage());
            return null;
        });
    }

    /**
     * Captures the initial state of the user data for tracking unsaved changes,
     * and passes the data to the View for rendering.
     */
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

        String profileImgUrl = currentUser.getProfileImageUrl();

        view.showUserData(origUsername, origPassword, origEmail, origFirstName, origLastName, origPhone,
                origStreet, origStreetNo, origCity, origZip, origCountry, profileImgUrl);
    }

    /**
     * Validates user inputs, manages profile image uploads or deletions,
     * and saves the updated domain model to the database.
     *
     * @param username       The updated username.
     * @param password       The updated password.
     * @param email          The updated email address.
     * @param fName          The updated first name.
     * @param lName          The updated last name.
     * @param phone          The updated phone number.
     * @param street         The updated street name.
     * @param streetNo       The updated street number.
     * @param city           The updated city.
     * @param zip            The updated zip code.
     * @param country        The updated country.
     * @param isPhotoRemoved True if the user requested to remove their profile photo.
     * @param newImageUri    The local URI of a newly selected photo, or null if unchanged.
     * @param userId         The unique ID of the user.
     */
    public void onSaveClicked(String username, String password, String email, String fName, String lName, String phone,
                              String street, String streetNo, String city, String zip, String country,
                              boolean isPhotoRemoved, Uri newImageUri, String userId) {

        if (currentUser == null) {
            if (view != null) view.showMessage("Error: User data is not loaded.");
            return;
        }

        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            if (view != null) view.showMessage("Please fill in the required fields (Username, Password, Email).");
            return;
        }

        if (password.length() < 8) {
            if (view != null) view.showMessage("Password must be at least 8 characters long.");
            return;
        }

        if (!phone.trim().isEmpty() && phone.length() != 10) {
            if (view != null) view.showMessage("Phone number must contain exactly 10 digits.");
            return;
        }

        // Update the local domain object with new data
        currentUser.setUsername(username);
        currentUser.setPassword(password);
        currentUser.setEmailAddress(new EmailAddress(email));
        currentUser.setFirstname(fName);
        currentUser.setLastname(lName);
        currentUser.setPhonenumber(phone);

        Address newAddress = new Address(street, streetNo, city, country, zip);
        currentUser.setAddress(newAddress);

        // Update the snapshot to prevent false unsaved changes warnings
        origUsername = username; origPassword = password; origEmail = email;
        origFirstName = fName; origLastName = lName; origPhone = phone;
        origStreet = street; origStreetNo = streetNo; origCity = city;
        origZip = zip; origCountry = country;

        // Handle Image Upload / Deletion logic via DAO
        if (isPhotoRemoved) {
            // User requested to remove the photo
            currentUser.setProfileImageUrl(null);

            // Instruct the DAO to delete the file asynchronously
            imageStorageDAO.deleteProfileImage(userId);

            // Save the user data to Firestore
            saveUserToDatabase();

        } else if (newImageUri != null) {
            // User selected a new photo to upload
            if (view != null) view.showMessage("Uploading image, please wait...");

            // Upload via ImageStorageDAO
            imageStorageDAO.uploadProfileImage(userId, newImageUri).thenAccept(downloadUrl -> {
                // Upon successful upload, set the retrieved URL
                currentUser.setProfileImageUrl(downloadUrl);
                // Save the user data to Firestore
                saveUserToDatabase();
            }).exceptionally(e -> {
                if (view != null) view.showMessage("Image upload failed: " + e.getMessage());
                return null;
            });

        } else {
            // No changes were made to the profile picture
            saveUserToDatabase();
        }
    }

    /**
     * Helper method to persist the updated user domain object to the database (Firestore)
     * using the appropriate DAO based on the user's role.
     */
    private void saveUserToDatabase() {
        if (currentUser instanceof gr.softeng.team21.domain.Customer) {
            customerDAO.addCustomer((gr.softeng.team21.domain.Customer) currentUser).thenAccept(aVoid -> {
                if (view != null) {
                    view.showMessage("Profile updated successfully!");
                    view.finishView();
                }
            }).exceptionally(e -> {
                if (view != null) view.showMessage("Error saving data: " + e.getMessage());
                return null;
            });
        } else if (currentUser instanceof gr.softeng.team21.domain.Employee) {
            employeeDAO.addEmployee((gr.softeng.team21.domain.Employee) currentUser).thenAccept(aVoid -> {
                if (view != null) {
                    view.showMessage("Profile updated successfully!");
                    view.finishView();
                }
            }).exceptionally(e -> {
                if (view != null) view.showMessage("Error saving data: " + e.getMessage());
                return null;
            });
        }
    }

    /**
     * Checks if current UI inputs differ from the loaded snapshot when the back button is pressed.
     *
     * @param username The current username in the UI.
     * @param password The current password in the UI.
     * @param email    The current email in the UI.
     * @param fName    The current first name in the UI.
     * @param lName    The current last name in the UI.
     * @param phone    The current phone in the UI.
     * @param street   The current street in the UI.
     * @param streetNo The current street number in the UI.
     * @param city     The current city in the UI.
     * @param zip      The current zip code in the UI.
     * @param country  The current country in the UI.
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