package gr.softeng.team21.view.user.login;

import android.widget.TextView;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

public interface LoginView {
    String getUsername();
    String getPassword();

    TextView getUserNameEdtText();
    TextView getPasswordEdtText();

    void showErrorMessage(String title, String message);
    void showSuccessMessage(String message);

    void navigateUserToHomePage(UserType userType, User user);

    void navigateToRegister();
}