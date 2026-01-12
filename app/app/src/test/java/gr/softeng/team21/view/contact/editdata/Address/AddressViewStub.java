package gr.softeng.team21.view.contact.editdata.Address;

/**
 * Stub implementation of {@link AddressView} for testing purposes.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI updates (address details) during the address editing flow.
 * @author PAVLOS GRATSANIS
 */
public class AddressViewStub implements AddressView {
    private String street, number, zip, city, country;
    private String message;

    /**
     * Returns the street name stored.
     * Used for verification in tests.
     * @return The street name.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Returns the last message (success or error) shown to the user.
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     * Stores the success message in a variable for verification.
     */
    @Override
    public void SaveSuccess(String msg) {
        message = msg;
    }

    /**
     * {@inheritDoc}
     * Stores the error message in a variable for verification.
     */
    @Override
    public void showError(String msg) {
        message = msg;
    }

    /**
     * {@inheritDoc}
     * Stores the address details in local variables to simulate the UI population.
     */
    @Override
    public void setAddressDetails(String street, String number, String zip, String city, String country) {
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    /**
     * {@inheritDoc}
     * Not used in tests
     */
    @Override
    public void finishView() {

    }
}