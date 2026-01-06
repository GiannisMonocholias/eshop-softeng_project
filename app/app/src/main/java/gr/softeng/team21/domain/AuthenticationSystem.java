package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.util.Date;

public class AuthenticationSystem {
    private static AuthenticationSystem instance;
    private UserCredentialsDAOMemory repo;

    private AuthenticationSystem() {
        repo = UserCredentialsDAOMemory.getInstance();
    }

    public static AuthenticationSystem getInstance() {
        if (instance == null) {
            instance = new AuthenticationSystem();
        }
        return instance;
    }


    public User login(String username, String password) {
        return repo.validateAndGetUser(username, password);
    }

    public void registerCustomer(String username, String firstname, String password, String lastname, String phoneNumber,
                                 EmailAddress emailaddress, String customer_id, Date registdate) {
        Customer customer = new Customer(username, firstname, password, lastname, phoneNumber, emailaddress, customer_id,
                registdate);
        repo.addUser(customer);
    }

    public void removeUser(String username) {
        repo.removeUser(username);
    }

}
