package gr.softeng.team21.view.contact.editdata.Address;

public class AddressViewStub implements AddressView {
    private String street, number, zip, city, country, zipcode;
    private String message;

    public String getStreet() {
        return street;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void SaveSuccess(String msg) {
        message = msg;
    }

    @Override
    public void showError(String msg) {
        message = msg;
    }

    @Override
    public void setAddressDetails(String street, String number, String zip, String city, String country) {
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    @Override
    public void finishView() {

    }
}
