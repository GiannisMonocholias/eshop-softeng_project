package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

/**
 * View contract for the login screen.
 * Defines the requirements for credential input, field management,
 * and role-based navigation.
 * @author Γιάννης Μονοχολιάς
 */
public interface LoginView {

    /**
     * @return username input.
     */
    String getUsername();

    /**
     * @return password input.
     */
    String getPassword();

    /**
     * Clears the input fields for username and password.
     */
    void resetFields();

    /**
     * Displays an error alert dialog.
     * @param title   The title of the error.
     * @param message The detailed error description.
     */
    void showErrorMessage(String title, String message);

    /**
     * Displays a success feedback message.
     * @param message The success description.
     */
    void showSuccessMessage(String message);

    /**
     * Navigates the authenticated user to their specific home dashboard.
     * @param userType The identified role of the user.
     * @param user     The user domain object containing profile data.
     */
    void navigateUserToHomePage(UserType userType, User user);

    /** Navigates to the user registration screen. */
    void navigateToRegister();
}