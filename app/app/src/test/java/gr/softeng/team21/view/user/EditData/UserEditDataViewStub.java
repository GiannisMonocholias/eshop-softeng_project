package gr.softeng.team21.view.user.EditData;

/**
 * Stub implementation of {@link UserEditDataView} for testing purposes.
 * It tracks the number of times each navigation method is called to verify the presenter's logic.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataViewStub implements UserEditDataView {
    private int UsernameCount = 0;
    private int PasswordCount = 0;
    private int EmailCount = 0;
    private int PhoneCount = 0;
    private int AddressCount = 0;

    /**
     * Returns the number of times the address navigation was triggered.
     * Used for verification in tests.
     * @return The count of address navigation calls.
     */
    public int getAddressCount() {
        return AddressCount;
    }

    /**
     * Returns the number of times the phone navigation was triggered.
     * Used for verification in tests.
     * @return The count of phone navigation calls.
     */
    public int getPhoneCount() {
        return PhoneCount;
    }

    /**
     * Returns the number of times the email navigation was triggered.
     * Used for verification in tests.
     * @return The count of email navigation calls.
     */
    public int getEmailCount() {
        return EmailCount;
    }

    /**
     * Returns the number of times the password navigation was triggered.
     * Used for verification in tests.
     * @return The count of password navigation calls.
     */
    public int getPasswordCount() {
        return PasswordCount;
    }

    /**
     * Returns the number of times the username navigation was triggered.
     * Used for verification in tests.
     * @return The count of username navigation calls.
     */
    public int getUsernameCount() {
        return UsernameCount;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for username navigation.
     */
    @Override
    public void goToUsername() {
        UsernameCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for password navigation.
     */
    @Override
    public void goToPassword() {
        PasswordCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for address navigation.
     */
    @Override
    public void goToAddress() {
        AddressCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for email navigation.
     */
    @Override
    public void goToEmail() {
        EmailCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for phone navigation.
     */
    @Override
    public void goToPhone() {
        PhoneCount++;
    }
}