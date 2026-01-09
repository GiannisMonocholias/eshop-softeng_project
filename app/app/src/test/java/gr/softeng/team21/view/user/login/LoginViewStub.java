package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

/**
 * A stub implementation of the {@link LoginView} interface for unit testing.
 * It provides the necessary setters and getters to simulate user input and
 * verify the navigation logic and message reporting during the authentication process.
 * @author Γιάννης Μονοχολιάς
 */
public class LoginViewStub implements LoginView {

    private String usernameInput = "";
    private String passwordInput = "";

    private String errorMessage = "";
    private String successMessage = "";
    private UserType navigatedUserType;
    private boolean registerCalled = false;
    private boolean fieldsReset = false;

    /**
     * Sets the username input for test simulations.
     * @param username The username string.
     */
    public void setUsername(String username) { this.usernameInput = username; }

    /**
     * Sets the password input for test simulations.
     * @param password The password string.
     */
    public void setPassword(String password) { this.passwordInput = password; }

    @Override
    public String getUsername() { return usernameInput; }

    @Override
    public String getPassword() { return passwordInput; }

    /**
     * Resets the input fields and marks the reset action as performed.
     */
    @Override
    public void resetFields() {
        this.usernameInput = "";
        this.passwordInput = "";
        this.fieldsReset = true;
    }

    @Override
    public void showErrorMessage(String title, String message) { this.errorMessage = message; }

    @Override
    public void showSuccessMessage(String message) { this.successMessage = message; }

    /**
     * Captures the user type for navigation verification after a successful login.
     * @param userType The type of user (e.g., Customer, Deliverer).
     * @param user The user object.
     */
    @Override
    public void navigateUserToHomePage(UserType userType, User user) {
        this.navigatedUserType = userType;
    }

    /**
     * Marks the navigation to the registration screen as called.
     */
    @Override
    public void navigateToRegister() { this.registerCalled = true; }

    // --- Accessor methods for verification during assertions ---

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage; }
    public UserType getNavigatedUserType() { return navigatedUserType; }
    public boolean isRegisterCalled() { return registerCalled; }
    public boolean isFieldsReset() { return fieldsReset; }
}