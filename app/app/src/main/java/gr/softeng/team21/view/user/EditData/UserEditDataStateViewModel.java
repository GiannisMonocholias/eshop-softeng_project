package gr.softeng.team21.view.user.EditData;

import androidx.lifecycle.ViewModel;

/**
 * A lightweight ViewModel used strictly as a State Holder.
 * It retains the user's input across configuration changes (e.g., screen rotations)
 * so that uncommitted text is not lost and the database isn't queried multiple times.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataStateViewModel extends ViewModel {

    /** Flag to indicate if the data has already been fetched from the database. */
    public boolean isDataLoaded = false;

    // Fields to hold the temporary UI state of the EditTexts
    public String username = "";
    public String password = "";
    public String email = "";
    public String firstName = "";
    public String lastName = "";
    public String phone = "";

    public String street = "";
    public String streetNo = "";
    public String city = "";
    public String zip = "";
    public String country = "";
}