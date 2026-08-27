package gr.softeng.team21.view.user.EditData;

/**
 * Stub implementation of {@link UserEditDataView} for unit testing purposes.
 * Captures UI updates, messages, and state changes triggered by the Presenter.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataViewStub implements UserEditDataView {

    private String username, password, email, firstName, lastName, phone;
    private String street, streetNo, city, zip, country;

    private String message = "";
    private boolean finishCalled = false;
    private boolean unsavedDialogCalled = false;

    @Override
    public void showUserData(String username, String password, String email, String firstName,
                             String lastName, String phone, String street, String streetNo,
                             String city, String zip, String country) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.street = street;
        this.streetNo = streetNo;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    @Override
    public void showMessage(String message) {
        this.message = message;
    }

    @Override
    public void showUnsavedChangesDialog() {
        this.unsavedDialogCalled = true;
    }

    @Override
    public void finishView() {
        this.finishCalled = true;
    }

    // Getters for Tests
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getMessage() { return message; }
    public boolean isFinishCalled() { return finishCalled; }
    public boolean isUnsavedDialogCalled() { return unsavedDialogCalled; }
}