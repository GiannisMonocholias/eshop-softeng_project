package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

public class LoginViewStub implements LoginView {

    private String usernameInput = "";
    private String passwordInput = "";

    private String errorMessage = "";
    private String successMessage = "";
    private UserType navigatedUserType;
    private boolean registerCalled = false;
    private boolean fieldsReset = false;

    public void setUsername(String username) { this.usernameInput = username; }
    public void setPassword(String password) { this.passwordInput = password; }

    @Override
    public String getUsername() { return usernameInput; }

    @Override
    public String getPassword() { return passwordInput; }

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

    @Override
    public void navigateUserToHomePage(UserType userType, User user) {
        this.navigatedUserType = userType;
    }

    @Override
    public void navigateToRegister() { this.registerCalled = true; }

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage; }
    public UserType getNavigatedUserType() { return navigatedUserType; }
    public boolean isRegisterCalled() { return registerCalled; }
    public boolean isFieldsReset() { return fieldsReset; }
}