package gr.softeng.team21.view.contact.editdata.Address;

public interface AddressView {
    void SaveSuccess(String message);
    void showError(String message);

    void setAddressDetails(String street, String number, String zip, String city, String country);

    void finishView();
}