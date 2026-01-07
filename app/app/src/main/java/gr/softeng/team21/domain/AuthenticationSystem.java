package gr.softeng.team21.domain;

import java.util.NoSuchElementException;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.util.Date;

public class AuthenticationSystem {
    private static AuthenticationSystem instance;
    private UserCredentialsDAOMemory repo;

    /**
     * Private default constructor.
     * it is used to avoid external initialization.
     * initializes the repo variable as a reference to singleton instance
     * of UserCredentialsDAOMemory.
     */
    private AuthenticationSystem() {
        repo = UserCredentialsDAOMemory.getInstance();
    }


    /**
     * Returns the singleton instance of the AuthenticationSystem.
     * Initializes the instance it it has not been initialized
     * @return the unique instance of AuthenticationSystem
     */
    public static AuthenticationSystem getInstance() {
        if (instance == null) {
            instance = new AuthenticationSystem();
        }
        return instance;
    }


    /**
     * Authenticates a user based on their credentials.
     * @param username the given username to validate
     * @param password the given password to validate
     * @return the User who corresponds to the given username and password
     * @throws SecurityException if the username is not found or the password is incorrect
     */
    public User login(String username, String password) {
        return repo.validateAndGetUser(username, password);
    }

    /**
     * Registers a new customer in the system using their identification data.
     * @param username  account's unique identifier
     * @param firstname  first name
     * @param password  password
     * @param lastname  last name
     * @param phoneNumber  phone number
     * @param emailaddress  email address
     * @param customer_id unique business id assigned to the customer
     * @param registdate date when registration took place
     * @throws IllegalArgumentException if the username is already occupied from another account
     */
    public void registerCustomer(String username, String firstname, String password, String lastname, String phoneNumber,
                                 EmailAddress emailaddress, String customer_id, Date registdate) {
        Customer customer = new Customer(username, firstname, password, lastname, phoneNumber, emailaddress, customer_id,
                registdate);
        repo.addUser(customer);
    }


    /**
     * Removes the account associated with the specified username.
     * @param username the unique identifier of the account to be removed
     * @throws NoSuchElementException if no account with the given username exists
     */
    public void removeUser(String username) {
        repo.removeUser(username);
    }

}
