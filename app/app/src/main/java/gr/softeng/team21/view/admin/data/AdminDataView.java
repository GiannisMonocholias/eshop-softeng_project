package gr.softeng.team21.view.admin.data;

/**
 * View contract for editing Admin details.
 * Defines methods for retrieving user input, populating fields, and
 * showing feedback or confirmation dialogs.
 * @author Αλέξανδρος Δρακάκης
 */
public interface AdminDataView {

    String getUsername();
    String getPassword();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getStreet();
    String getStreetNo();
    String getCity();
    String getZip();

    /** Fills the UI fields with the loaded admin data. */
    void setAdminData(String username, String password, String email, String firstName, String lastName, String phone,
                      String street, String streetNo, String city, String zip);

    /** Shows an error message dialog. */
    void showError(String message);

    /** Shows a success message dialog. */
    void showSuccessMessage(String message);

    /** Shows a warning dialog about unsaved changes. */
    void showUnsavedChangesDialog();

    /** Closes the screen safely. */
    void finishActivity();
}