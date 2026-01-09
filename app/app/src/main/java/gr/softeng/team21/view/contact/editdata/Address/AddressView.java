package gr.softeng.team21.view.contact.editdata.Address;

public interface AddressView {
    void SaveSuccess(String msg);
    void showError(String msg);

    void setAddressDetails(String street, String number, String city, String country, String zip);
    void finishView();
}