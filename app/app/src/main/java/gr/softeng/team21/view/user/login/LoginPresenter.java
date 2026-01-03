package gr.softeng.team21.view.user.login;

import gr.softeng.team21.domain.AuthenticationSystem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.view.util.UserType;

public class LoginPresenter {
    private LoginView view;
    private AuthenticationSystem authenticationSystem;
    public LoginPresenter(LoginView view) {
        this.view = view;
        authenticationSystem = AuthenticationSystem.getInstance();
    }

    public void onLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()
        || username == null || password == null) {
            view.showErrorMessage("Σφάλμα", "Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        try {
            User user = authenticationSystem.login(username,password);

            view.showSuccessMessage("Επιτυχής σύνδεση!");

            UserType usertype = null;
            if(user instanceof Customer){
                usertype = UserType.CUSTOMER;
            } else if (user instanceof CustomerServiceEmployee){
                usertype = UserType.CUSTOMER_SERVICE_EMPLOYEE;
            } else if (user instanceof Deliverer){
                usertype = UserType.DELIVERER;
            } else if (user instanceof OrderPreparationEmployee){
                usertype = UserType.ORDER_PREPARATION_EMPLOYEE;
            } else if (user instanceof UpdateCatalogueEmployee){
                usertype = UserType.UPDATE_CATALOGUE_EMPLOYEE;
            }

            if(user != null)
                view.navigateUserToHomePage(usertype,user);
            else
                view.showErrorMessage("Μη υποστηριζόμενος τύπος χρήστη", "Αυτός ο τύπος χρήστη δεν υποστηρίζεται");

        } catch (SecurityException e) {
            view.showErrorMessage("Αποτυχία Σύνδεσης", "Λάθος όνομα χρήστη ή κωδικός.");
        }
    }

    public void onRegister() {
        view.navigateToRegister();
    }

    public void loginReset(){
        view.getUserNameEdtText().setText("");
        view.getPasswordEdtText().setText("");
    }
}