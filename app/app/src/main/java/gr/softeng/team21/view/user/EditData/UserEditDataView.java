package gr.softeng.team21.view.user.EditData;

/**
 * Interface for the User Edit Data.
 * Defines the navigation methods to the specific data editing screens.
 * @author PAVLOS GRATSANIS
 */
public interface UserEditDataView {
    /**
     * Navigates to the username editing screen.
     */
    void goToUsername();

    /**
     * Navigates to the password editing screen.
     */
    void goToPassword();

    /**
     * Navigates to the address editing screen.
     */
    void goToAddress();

    /**
     * Navigates to the email editing screen.
     */
    void goToEmail();

    /**
     * Navigates to the phone editing screen.
     */
    void goToPhone();
}