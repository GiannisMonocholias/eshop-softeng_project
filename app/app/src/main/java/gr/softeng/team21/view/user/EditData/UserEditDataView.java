package gr.softeng.team21.view.user.EditData;

/**
 * Interface defining the UI operations for the unified User Edit Data screen.
 * @author PAVLOS GRATSANIS
 */
public interface UserEditDataView {
    /**
     * Populates the input fields with the user's data fetched from the database.
     */
    void showUserData(String username, String password, String email, String firstName,
                      String lastName, String phone, String street, String streetNo,
                      String city, String zip, String country);

    /**
     * Displays a general feedback message (error or success) to the user.
     * @param message The message to display.
     */
    void showMessage(String message);

    /**
     * Shows a warning dialog when the user attempts to leave with unsaved changes.
     */
    void showUnsavedChangesDialog();

    /**
     * Closes the activity and returns to the previous screen.
     */
    void finishView();
}