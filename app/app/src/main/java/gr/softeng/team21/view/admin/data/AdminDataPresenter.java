package gr.softeng.team21.view.admin.data;

import android.net.Uri;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.Admin;

/**
 * Presenter handling the logic for loading and saving the Administrator's profile data.
 * It tracks unsaved changes to prevent accidental data loss upon pressing the back button.
 * Interacts directly with the Admin Singleton and handles profile image updates.
 *
 * @author Alexandros Drakakis
 */
public class AdminDataPresenter {

    private AdminDataView view;
    private Admin currentAdmin;

    // Snapshot of original data for comparison
    private String origUsername = "", origPassword = "", origEmail = "", origFirstName = "", origLastName = "", origPhone = "";
    private String origStreet = "", origStreetNo = "", origCity = "", origZip = "";
    private String origProfileImageUrl = "";

    public AdminDataPresenter(AdminDataView view) {
        this.view = view;
        this.currentAdmin = Admin.getInstance();
    }

    /**
     * Loads the admin data from the Singleton and populates the view.
     * Takes a snapshot of the loaded data to track unsaved changes later.
     */
    public void loadAdminData() {
        origUsername = currentAdmin.getUsername() != null ? currentAdmin.getUsername() : "";
        origPassword = currentAdmin.getPassword() != null ? currentAdmin.getPassword() : "";
        origEmail = currentAdmin.getEmailAddress() != null ? currentAdmin.getEmailAddress().toString() : "";
        origFirstName = currentAdmin.getFirstname() != null ? currentAdmin.getFirstname() : "";
        origLastName = currentAdmin.getLastname() != null ? currentAdmin.getLastname() : "";
        origPhone = currentAdmin.getPhonenumber() != null ? currentAdmin.getPhonenumber() : "";
        origProfileImageUrl = currentAdmin.getProfileImageUrl() != null ? currentAdmin.getProfileImageUrl() : "";

        if (currentAdmin.getAddress() != null) {
            origStreet = currentAdmin.getAddress().getStreet() != null ? currentAdmin.getAddress().getStreet() : "";
            origStreetNo = currentAdmin.getAddress().getNumber() != null ? currentAdmin.getAddress().getNumber() : "";
            origCity = currentAdmin.getAddress().getCity() != null ? currentAdmin.getAddress().getCity() : "";
            origZip = currentAdmin.getAddress().getZipcode() != null ? currentAdmin.getAddress().getZipcode() : "";
        }

        view.setAdminData(origUsername, origPassword, origEmail, origFirstName, origLastName, origPhone,
                origStreet, origStreetNo, origCity, origZip);

        // Load the image into the view
        view.loadProfileImage(origProfileImageUrl);
    }

    /**
     * Validates and saves the updated data to the Singleton.
     * Updates the internal snapshot upon success so the back button doesn't trigger a warning.
     *
     * @param selectedImageUri The new image URI if the admin changed their photo, or null.
     */
    public void onSaveClicked(Uri selectedImageUri) {
        currentAdmin.setUsername(view.getUsername());
        currentAdmin.setPassword(view.getPassword());
        currentAdmin.setEmailAddress(new EmailAddress(view.getEmail()));
        currentAdmin.setFirstname(view.getFirstName());
        currentAdmin.setLastname(view.getLastName());
        currentAdmin.setPhonenumber(view.getPhone());

        Address newAddress = new Address();
        newAddress.setStreet(view.getStreet());
        newAddress.setNumber(view.getStreetNo());
        newAddress.setCity(view.getCity());
        newAddress.setZipcode(view.getZip());
        currentAdmin.setAddress(newAddress);

        if (selectedImageUri != null) {
            currentAdmin.setProfileImageUrl(selectedImageUri.toString());
            origProfileImageUrl = selectedImageUri.toString();
        }

        // Update snapshot to prevent "unsaved changes" dialog after successful save
        origUsername = view.getUsername();
        origPassword = view.getPassword();
        origEmail = view.getEmail();
        origFirstName = view.getFirstName();
        origLastName = view.getLastName();
        origPhone = view.getPhone();
        origStreet = view.getStreet();
        origStreetNo = view.getStreetNo();
        origCity = view.getCity();
        origZip = view.getZip();

        view.showSuccessMessage("Τα στοιχεία σας ενημερώθηκαν επιτυχώς!");
    }

    /**
     * Checks if current input differs from the snapshot.
     * Triggers a warning dialog if changes are detected; otherwise, closes the view.
     *
     * @param selectedImageUri The URI of the currently selected image, to detect unsaved photo changes.
     */
    public void onBackPressed(Uri selectedImageUri) {
        boolean hasChanges = !view.getUsername().equals(origUsername) ||
                !view.getPassword().equals(origPassword) ||
                !view.getEmail().equals(origEmail) ||
                !view.getFirstName().equals(origFirstName) ||
                !view.getLastName().equals(origLastName) ||
                !view.getPhone().equals(origPhone) ||
                !view.getStreet().equals(origStreet) ||
                !view.getStreetNo().equals(origStreetNo) ||
                !view.getCity().equals(origCity) ||
                !view.getZip().equals(origZip);

        // If the user selected a new image but hasn't saved yet, it's considered an unsaved change
        if (selectedImageUri != null) {
            hasChanges = true;
        }

        if (hasChanges) {
            view.showUnsavedChangesDialog();
        } else {
            view.finishActivity();
        }
    }

    /**
     * Triggered when the user confirms discarding changes.
     */
    public void onDiscardChangesConfirmed() {
        view.finishActivity();
    }
}