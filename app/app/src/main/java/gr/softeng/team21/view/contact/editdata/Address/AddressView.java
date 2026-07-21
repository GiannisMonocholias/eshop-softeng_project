package gr.softeng.team21.view.contact.editdata.Address;

/**
 * Interface for the Address Edit View.
 * Defines the methods for displaying messages and updating the address form.
 * @author PAVLOS GRATSANIS
 */
public interface AddressView {
    /**
     * Displays a success message to the user.
     * @param msg The message to display.
     */
    void SaveSuccess(String msg);

    /**
     * Displays an error message to the user.
     * @param msg The message to display.
     */
    void showError(String msg);

    /**
     * Completes the view with the user's current address details.
     * @param street The street name.
     * @param number The street number.
     * @param city The city name.
     * @param country The country name.
     * @param zip The zip code.
     */
    void setAddressDetails(String street, String number, String city, String country, String zip);

    /**
     * Finishes the current activity.
     */
    void finishView();
}