package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.AuthenticationSystem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

/**
 * Presenter for the Login screen.
 * Coordinates with the {@link AuthenticationSystem} to validate credentials
 * and determines the user's role to trigger appropriate navigation.
 * @author Γιάννης Μονοχολιάς
 */
public class LoginPresenter {
    private LoginView view;
    private AuthenticationSystem authenticationSystem;

    /**
     * Initializes the presenter and connects to the singleton AuthenticationSystem.
     * @param view The login view implementation.
     */
    public LoginPresenter(LoginView view) {
        this.view = view;
        authenticationSystem = AuthenticationSystem.getInstance();
    }

    /**
     * Handles the login logic. Validates input, attempts authentication,
     * and maps the resulting User object to a role-specific UserType for navigation.
     */
    public void onLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            view.showErrorMessage("Σφάλμα", "Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        try {
            User user = authenticationSystem.login(username, password);
            view.showSuccessMessage("Επιτυχής σύνδεση!");

            // Identify user role through instance checking
            UserType usertype = null;
            if (user instanceof Customer) {
                usertype = UserType.CUSTOMER;
            } else if (user instanceof CustomerServiceEmployee) {
                usertype = UserType.CUSTOMER_SERVICE_EMPLOYEE;
            } else if (user instanceof Deliverer) {
                usertype = UserType.DELIVERER;
            } else if (user instanceof OrderPreparationEmployee) {
                usertype = UserType.ORDER_PREPARATION_EMPLOYEE;
            } else if (user instanceof UpdateCatalogueEmployee) {
                usertype = UserType.UPDATE_CATALOGUE_EMPLOYEE;
            } else if (user instanceof Admin){
                usertype = UserType.ADMIN;
            }



            if (usertype != null) {
                view.navigateUserToHomePage(usertype, user);
            } else {
                view.showErrorMessage("Unsupported User", "This user type is not supported by the system.");
            }

        } catch (SecurityException e) {
            view.showErrorMessage("Αποτυχία σύνδεσης", "Λάθος όνομα χρήστη ή κωδικός.");
        }
    }

    /**
     * Triggers navigation to the registration screen.
     */
    public void onRegister() {
        view.navigateToRegister();
    }

    /**
     * Requests the view to clear all input fields.
     */
    public void loginReset(){
        view.resetFields();
    }
}