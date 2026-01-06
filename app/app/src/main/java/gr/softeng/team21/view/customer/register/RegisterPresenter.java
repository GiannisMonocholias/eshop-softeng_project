package gr.softeng.team21.view.customer.register;

import java.util.UUID;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;


public class RegisterPresenter {

    private RegisterView view;
    private CustomerDAO customerDAO;

    public RegisterPresenter(RegisterView view, CustomerDAO customerDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
    }

    public void register(String username, String firstname, String password,
                         String lastname, String phone, String emailStr) {

        if (username.isEmpty() || password.isEmpty() || emailStr.isEmpty()) {
            view.showErrorMessage("Παρακαλώ συμπληρώστε τα απαραίτητα πεδία.");
            return;
        }

        try {
            EmailAddress emailObj = new EmailAddress(emailStr);

            String randomId = UUID.randomUUID().toString();

            Date currentDate = new Date();

            Customer newCustomer = new Customer(username, firstname, password, lastname,
                    phone, emailObj, randomId, currentDate);

            customerDAO.addCustomer(newCustomer);
            UserCredentialsDAOMemory.getInstance().addUser(newCustomer);

            view.showSuccessMessage("Επιτυχής εγγραφή! ID: " + randomId);
            view.clearInputFields();

        } catch (Exception e) {
            view.showErrorMessage("Σφάλμα κατά την εγγραφή: " + e.getMessage());
        }
    }
}