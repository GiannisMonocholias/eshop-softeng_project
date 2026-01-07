package gr.softeng.team21.view.user;

import gr.softeng.team21.view.user.EditData.UserEditDataView;

public class UserEditDataViewStub implements UserEditDataView {
    private int UsernameCount = 0;
    private int PasswordCount = 0;

    private int EmailCount = 0;

    private int PhoneCount = 0;
    private int AddressCount = 0;

    public int getAddressCount() {
        return AddressCount;
    }

    public int getPhoneCount() {
        return PhoneCount;
    }

    public int getEmailCount() {
        return EmailCount;
    }

    public int getPasswordCount() {
        return PasswordCount;
    }

    public int getUsernameCount() {
        return UsernameCount;
    }

    @Override
    public void goToUsername() {
        UsernameCount++;

    }

    @Override
    public void goToPassword() {
        PasswordCount++;

    }

    @Override
    public void goToAddress() {
        AddressCount++;

    }

    @Override
    public void goToEmail() {
        EmailCount++;
    }

    @Override
    public void goToPhone() {
        PhoneCount++;
    }
}
