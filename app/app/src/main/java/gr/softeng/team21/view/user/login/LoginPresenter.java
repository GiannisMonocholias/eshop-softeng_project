package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.AuthenticationSystem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.view.util.UserType;

/**
 * Presenter for the Login screen.
 * Coordinates with the {@link AuthenticationSystem} to asynchronously validate credentials
 * and determines the user's role to trigger appropriate navigation.
 * Uses Dependency Injection for the AuthenticationSystem.
 * @author Γιάννης Μονοχολιάς
 */
public class LoginPresenter {
    private LoginView view;
    private AuthenticationSystem authenticationSystem;

    /**
     * Initializes the presenter with the view and the authentication domain service.
     * @param view The login view implementation (Activity or Stub).
     * @param authenticationSystem The domain service handling authentication.
     */
    public LoginPresenter(LoginView view, AuthenticationSystem authenticationSystem) {
        this.view = view;
        this.authenticationSystem = authenticationSystem;
    }

    /**
     * Handles the login logic asynchronously. Validates input, attempts authentication,
     * and maps the resulting User object to a role-specific UserType for navigation.
     */
    public void onLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            view.showErrorMessage("Σφάλμα", "Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        authenticationSystem.login(username, password).thenAccept(user -> {
            view.showSuccessMessage("Επιτυχής σύνδεση!");

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
                view.showErrorMessage("Σφάλμα", "Ο τύπος του χρήστη δεν υποστηρίζεται από το σύστημα.");
            }
        }).exceptionally(e -> {
            view.showErrorMessage("Αποτυχία σύνδεσης", "Λάθος όνομα χρήστη ή κωδικός.");
            return null;
        });
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